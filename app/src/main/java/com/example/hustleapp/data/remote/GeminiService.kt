package com.example.hustleapp.data.remote

import android.util.Log
import com.example.hustleapp.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Data class representing a single step in the roadmap
 */
data class RoadmapStepData(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val duration: String,
    val skills: List<String>
)

/**
 * Service for generating career roadmaps using Gemini AI REST API
 */
class GeminiService {
    
    companion object {
        private const val TAG = "GeminiService"
        // API key được lấy từ BuildConfig (đọc từ local.properties)
        private val API_KEY = BuildConfig.GEMINI_API_KEY
        private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent"
    }
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()
    
    /**
     * Generate a career roadmap based on user input
     */
    suspend fun generateRoadmap(
        currentRole: String,
        targetRole: String,
        currentSkills: String,
        yearsExperience: Int
    ): Result<List<RoadmapStepData>> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Starting roadmap generation via REST API...")
            
            val prompt = buildPrompt(currentRole, targetRole, currentSkills, yearsExperience)
            
            // Build request body
            val requestJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.7)
                    put("topK", 40)
                    put("topP", 0.95)
                    put("maxOutputTokens", 1024)
                    put("responseMimeType", "application/json")
                })
            }
            
            val url = "$BASE_URL?key=$API_KEY"
            val mediaType = "application/json".toMediaType()
            val requestBody = requestJson.toString().toRequestBody(mediaType)
            
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
            
            Log.d(TAG, "Calling Gemini API...")
            
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            
            Log.d(TAG, "Response code: ${response.code}")
            
            if (!response.isSuccessful) {
                Log.e(TAG, "API Error: $responseBody")
                val errorMsg = parseErrorMessage(responseBody) ?: "Lỗi API: ${response.code}"
                return@withContext Result.failure(Exception(errorMsg))
            }
            
            if (responseBody.isNullOrBlank()) {
                return@withContext Result.failure(Exception("Phản hồi trống từ API"))
            }
            
            Log.d(TAG, "Response: ${responseBody.take(500)}")
            
            // Parse the response
            val responseJson = JSONObject(responseBody)
            val candidates = responseJson.optJSONArray("candidates")
            
            if (candidates == null || candidates.length() == 0) {
                return@withContext Result.failure(Exception("Không có kết quả từ AI"))
            }
            
            val content = candidates.getJSONObject(0)
                .optJSONObject("content")
                ?.optJSONArray("parts")
                ?.getJSONObject(0)
                ?.optString("text", "")
            
            if (content.isNullOrBlank()) {
                return@withContext Result.failure(Exception("Phản hồi trống từ AI"))
            }
            
            val steps = parseResponse(content)
            Log.d(TAG, "Parsed ${steps.size} steps successfully")
            Result.success(steps)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error generating roadmap", e)
            val errorMessage = when {
                e.message?.contains("Unable to resolve host") == true -> "Không có kết nối mạng"
                e.message?.contains("timeout") == true -> "Hết thời gian chờ"
                else -> "Lỗi: ${e.message ?: "Không xác định"}"
            }
            Result.failure(Exception(errorMessage))
        }
    }
    
    private fun parseErrorMessage(responseBody: String?): String? {
        return try {
            responseBody?.let {
                val json = JSONObject(it)
                json.optJSONObject("error")?.optString("message")
            }
        } catch (e: Exception) {
            null
        }
    }
    
    private fun buildPrompt(
        currentRole: String,
        targetRole: String,
        currentSkills: String,
        yearsExperience: Int
    ): String {
        return """Tạo lộ trình nghề nghiệp từ "$currentRole" đến "$targetRole". Trả về JSON array với ĐÚNG 3 bước, mỗi bước có: stepNumber (số), title (tên ngắn), description (1 câu), duration (thời gian), skills (2 kỹ năng). Tiếng Việt.""".trimIndent()
    }
    
    private fun parseResponse(responseText: String): List<RoadmapStepData> {
        val steps = mutableListOf<RoadmapStepData>()
        
        try {
            Log.d(TAG, "Raw response text: ${responseText.take(200)}")
            
            // Clean up the response - remove markdown code blocks if present
            var cleanedText = responseText.trim()
            
            // Remove various markdown code block formats
            cleanedText = cleanedText
                .replace(Regex("```json\\s*"), "")
                .replace(Regex("```\\s*"), "")
                .replace(Regex("^\\s*json\\s*", RegexOption.IGNORE_CASE), "")
                .trim()
            
            // Try to find JSON array in the text using regex
            // This handles cases where there's text before or after the JSON
            val jsonArrayPattern = Regex("\\[\\s*\\{.*\\}\\s*\\]", RegexOption.DOT_MATCHES_ALL)
            val matchResult = jsonArrayPattern.find(cleanedText)
            
            val jsonText = if (matchResult != null) {
                Log.d(TAG, "Found JSON array via regex")
                matchResult.value
            } else {
                // Try to find the start of array and extract from there
                val startIndex = cleanedText.indexOf('[')
                val endIndex = cleanedText.lastIndexOf(']')
                if (startIndex != -1 && endIndex > startIndex) {
                    Log.d(TAG, "Found JSON array via bracket search")
                    cleanedText.substring(startIndex, endIndex + 1)
                } else {
                    Log.d(TAG, "Using cleaned text as-is")
                    cleanedText
                }
            }
            
            // Fix malformed JSON from Gemini 2.5 Flash - missing closing braces
            val fixedJsonText = jsonText
                .replace(Regex("\\]\\s*,\\s*\\{"), "},\n{")  // Fix: ],{ -> },{
                .replace(Regex("\\]\\s*\\n\\s*,"), "},")      // Fix: ]\n, -> },
            
            Log.d(TAG, "Attempting to parse fixed JSON: ${fixedJsonText.take(300)}")
            
            val jsonArray = JSONArray(fixedJsonText)
            
            for (i in 0 until jsonArray.length()) {
                val jsonStep = jsonArray.getJSONObject(i)
                
                val skillsList = mutableListOf<String>()
                val skillsArray = jsonStep.optJSONArray("skills")
                if (skillsArray != null) {
                    for (j in 0 until skillsArray.length()) {
                        skillsList.add(skillsArray.getString(j))
                    }
                }
                
                steps.add(
                    RoadmapStepData(
                        stepNumber = jsonStep.optInt("stepNumber", i + 1),
                        title = jsonStep.optString("title", "Bước ${i + 1}"),
                        description = jsonStep.optString("description", ""),
                        duration = jsonStep.optString("duration", ""),
                        skills = skillsList
                    )
                )
            }
            
            Log.d(TAG, "Successfully parsed ${steps.size} steps")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing response: ${e.message}", e)
            Log.e(TAG, "Response was: ${responseText.take(500)}")
            
            // If JSON parsing fails, create a user-friendly error message
            steps.add(
                RoadmapStepData(
                    stepNumber = 1,
                    title = "Không thể tải lộ trình",
                    description = "Đã xảy ra lỗi khi xử lý phản hồi từ AI. Vui lòng thử lại.",
                    duration = "",
                    skills = listOf("Nhấn nút + để tạo lộ trình mới")
                )
            )
        }
        
        return steps
    }
}
