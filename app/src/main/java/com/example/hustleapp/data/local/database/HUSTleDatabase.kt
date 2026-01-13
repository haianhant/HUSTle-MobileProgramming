package com.example.hustleapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hustleapp.data.local.dao.*
import com.example.hustleapp.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        User::class,
        Skill::class,
        Experience::class,
        Education::class,
        Job::class,
        Application::class,
        Post::class,
        Comment::class,
        PostLike::class,
        RoadmapStep::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HUSTleDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun skillDao(): SkillDao
    abstract fun experienceDao(): ExperienceDao
    abstract fun educationDao(): EducationDao
    abstract fun jobDao(): JobDao
    abstract fun applicationDao(): ApplicationDao
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    abstract fun roadmapDao(): RoadmapDao
    
    companion object {
        @Volatile
        private var INSTANCE: HUSTleDatabase? = null
        
        fun getDatabase(context: Context): HUSTleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HUSTleDatabase::class.java,
                    "hustle_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
    
    private class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateSampleData(database)
                }
            }
        }
        
        private suspend fun populateSampleData(database: HUSTleDatabase) {
            // Create sample HR user
            val hrUser = User(
                email = "hr@company.com",
                password = "123456",
                name = "Nguyễn Văn HR",
                role = UserRole.HR,
                headline = "HR Manager tại TechCorp",
                about = "Chuyên gia tuyển dụng với 5 năm kinh nghiệm"
            )
            val hrId = database.userDao().insert(hrUser)
            
            // Create sample Applicant user
            val applicantUser = User(
                email = "user@gmail.com",
                password = "123456",
                name = "Trần Thị Ứng Viên",
                role = UserRole.APPLICANT,
                headline = "Software Developer | Java | Kotlin",
                about = "Sinh viên năm cuối ngành Công nghệ thông tin, đam mê phát triển ứng dụng di động."
            )
            val applicantId = database.userDao().insert(applicantUser)
            
            // Add skills for applicant
            val skills = listOf(
                Skill(userId = applicantId, name = "Kotlin"),
                Skill(userId = applicantId, name = "Java"),
                Skill(userId = applicantId, name = "Android"),
                Skill(userId = applicantId, name = "Git"),
                Skill(userId = applicantId, name = "SQL")
            )
            database.skillDao().insertAll(skills)
            
            // Add experience for applicant
            val experience = Experience(
                userId = applicantId,
                title = "Mobile Developer Intern",
                company = "TechStartup",
                startDate = System.currentTimeMillis() - 180L * 24 * 60 * 60 * 1000,
                endDate = null,
                description = "Phát triển ứng dụng Android sử dụng Kotlin và Jetpack libraries"
            )
            database.experienceDao().insert(experience)
            
            // Add education for applicant
            val education = Education(
                userId = applicantId,
                degree = "Cử nhân Công nghệ thông tin",
                school = "Đại học Bách khoa Hà Nội",
                startDate = System.currentTimeMillis() - 4L * 365 * 24 * 60 * 60 * 1000,
                endDate = null
            )
            database.educationDao().insert(education)
            
            // Create sample jobs
            val jobs = listOf(
                Job(
                    hrUserId = hrId,
                    title = "Android Developer",
                    company = "TechCorp Vietnam",
                    salary = "15-25 triệu VNĐ",
                    location = "Hà Nội",
                    description = "Chúng tôi đang tìm kiếm Android Developer có kinh nghiệm để tham gia vào đội ngũ phát triển sản phẩm.\n\nMô tả công việc:\n- Phát triển ứng dụng Android mới\n- Bảo trì và nâng cấp ứng dụng hiện có\n- Làm việc với team Backend để tích hợp API\n- Tham gia code review và đảm bảo chất lượng code",
                    requirements = "- 1-3 năm kinh nghiệm Android\n- Thành thạo Kotlin\n- Hiểu biết về MVVM, Clean Architecture\n- Kinh nghiệm với Retrofit, Room, Coroutines\n- Có khả năng làm việc nhóm tốt",
                    viewCount = 150
                ),
                Job(
                    hrUserId = hrId,
                    title = "Junior Backend Developer",
                    company = "TechCorp Vietnam",
                    salary = "12-18 triệu VNĐ",
                    location = "Hồ Chí Minh",
                    description = "Tuyển dụng Backend Developer có đam mê và mong muốn phát triển.\n\nMô tả công việc:\n- Phát triển API RESTful\n- Thiết kế và tối ưu database\n- Viết unit tests\n- Tham gia vào quy trình CI/CD",
                    requirements = "- Fresher hoặc 1 năm kinh nghiệm\n- Biết Java hoặc Node.js\n- Hiểu biết cơ bản về SQL\n- Có tinh thần học hỏi",
                    viewCount = 89
                ),
                Job(
                    hrUserId = hrId,
                    title = "UI/UX Designer",
                    company = "Creative Agency",
                    salary = "18-30 triệu VNĐ",
                    location = "Hà Nội",
                    description = "Tìm kiếm UI/UX Designer sáng tạo cho các dự án web và mobile.",
                    requirements = "- 2+ năm kinh nghiệm UI/UX\n- Thành thạo Figma, Adobe XD\n- Portfolio ấn tượng\n- Hiểu biết về Design System",
                    viewCount = 234
                )
            )
            jobs.forEach { database.jobDao().insert(it) }
            
            // Create sample posts
            val posts = listOf(
                Post(
                    authorId = applicantId,
                    content = "Vừa hoàn thành dự án đầu tiên với Kotlin Coroutines! Thật sự rất impressed với cách nó handle async tasks một cách clean và readable. Ai có tips gì hay về Coroutines share với mình nhé! 🚀",
                    likeCount = 24,
                    commentCount = 5
                ),
                Post(
                    authorId = hrId,
                    content = "📢 TechCorp đang tuyển dụng Android Developer!\n\nNếu bạn đam mê mobile development và muốn làm việc trong môi trường năng động, hãy apply ngay nhé!\n\n#hiring #android #kotlin",
                    likeCount = 45,
                    commentCount = 12
                ),
                Post(
                    authorId = applicantId,
                    content = "Tips phỏng vấn Android Developer:\n\n1. Hiểu rõ Activity/Fragment lifecycle\n2. Nắm vững MVVM architecture\n3. Biết cách handle configuration changes\n4. Có project demo trên GitHub\n\nChúc mọi người thành công! 💪",
                    likeCount = 156,
                    commentCount = 28
                )
            )
            posts.forEach { database.postDao().insert(it) }
            
            // Create sample roadmap
            val roadmapSteps = listOf(
                RoadmapStep(
                    userId = applicantId,
                    targetRole = "Senior Android Developer",
                    stepNumber = 1,
                    title = "Học Kotlin cơ bản",
                    description = "Hoàn thành khóa học Kotlin trên Coursera",
                    isCompleted = true
                ),
                RoadmapStep(
                    userId = applicantId,
                    targetRole = "Senior Android Developer",
                    stepNumber = 2,
                    title = "Xây dựng app MVVM",
                    description = "Tạo một ứng dụng hoàn chỉnh sử dụng MVVM architecture",
                    isCompleted = true
                ),
                RoadmapStep(
                    userId = applicantId,
                    targetRole = "Senior Android Developer",
                    stepNumber = 3,
                    title = "Học Coroutines & Flow",
                    description = "Master async programming với Kotlin Coroutines",
                    isCompleted = false
                ),
                RoadmapStep(
                    userId = applicantId,
                    targetRole = "Senior Android Developer",
                    stepNumber = 4,
                    title = "Contribute Open Source",
                    description = "Đóng góp vào ít nhất 2 dự án Android open source",
                    isCompleted = false
                ),
                RoadmapStep(
                    userId = applicantId,
                    targetRole = "Senior Android Developer",
                    stepNumber = 5,
                    title = "Publish app lên Play Store",
                    description = "Phát hành ứng dụng cá nhân lên Google Play Store",
                    isCompleted = false
                )
            )
            database.roadmapDao().insertAll(roadmapSteps)
        }
    }
}
