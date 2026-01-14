<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.webp" alt="HUSTle Logo" width="120"/>
</p>

<h1 align="center">HUSTle - Career Development App</h1>

<p align="center">
  <strong>Ứng dụng phát triển nghề nghiệp dành cho sinh viên và nhà tuyển dụng</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green?style=for-the-badge&logo=android" alt="Platform"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-purple?style=for-the-badge&logo=kotlin" alt="Language"/>
  <img src="https://img.shields.io/badge/Min%20SDK-26-blue?style=for-the-badge" alt="Min SDK"/>
  <img src="https://img.shields.io/badge/AI-Gemini-orange?style=for-the-badge&logo=google" alt="AI"/>
</p>

---

## 📖 Giới thiệu

**HUSTle** là ứng dụng Android hỗ trợ phát triển nghề nghiệp, kết nối **ứng viên** với **nhà tuyển dụng (HR)**. Ứng dụng tích hợp **AI Gemini** để tạo lộ trình phát triển sự nghiệp cá nhân hóa.

### 🎯 Mục tiêu
- Giúp sinh viên và người tìm việc xây dựng hồ sơ chuyên nghiệp
- Kết nối ứng viên với các cơ hội việc làm phù hợp
- Sử dụng AI để tạo lộ trình phát triển nghề nghiệp

---

## ✨ Tính năng chính

### 👤 Dành cho Ứng viên (Applicant)
| Tính năng | Mô tả |
|-----------|-------|
| 📝 **Quản lý hồ sơ** | Tạo và chỉnh sửa thông tin cá nhân, kỹ năng, kinh nghiệm, học vấn |
| 💼 **Tìm kiếm việc làm** | Xem và ứng tuyển các vị trí công việc phù hợp |
| 🗺️ **Lộ trình AI** | Gemini AI tạo lộ trình phát triển nghề nghiệp cá nhân hóa |
| 💬 **Cộng đồng** | Đăng bài, thích, bình luận và chia sẻ với cộng đồng |

### 🏢 Dành cho Nhà tuyển dụng (HR)
| Tính năng | Mô tả |
|-----------|-------|
| 📋 **Quản lý việc làm** | Tạo, chỉnh sửa và đóng các tin tuyển dụng |
| 👥 **Quản lý ứng viên** | Xem hồ sơ, shortlist hoặc từ chối ứng viên |
| 📊 **Thống kê** | Xem analytics về số lượng đơn, lượt xem, tỷ lệ chuyển đổi |
| 💬 **Cộng đồng** | Tương tác với ứng viên qua mạng xã hội nội bộ |

---

## 🏗️ Kiến trúc ứng dụng

```
📦 HustleApp
├── 📂 data/                     # Data Layer
│   ├── 📂 local/
│   │   ├── 📂 dao/              # Room DAOs
│   │   ├── 📂 entity/           # Room Entities
│   │   └── AppDatabase.kt       # Room Database
│   ├── 📂 remote/
│   │   └── GeminiService.kt     # Gemini AI API
│   └── 📂 repository/           # Repositories
│
├── 📂 ui/                       # Presentation Layer
│   ├── 📂 auth/                 # Login/Register
│   ├── 📂 applicant/            # Ứng viên screens
│   │   ├── 📂 home/             # Social Feed
│   │   ├── 📂 jobs/             # Job Listing
│   │   ├── 📂 profile/          # Profile Management
│   │   └── 📂 roadmap/          # AI Roadmap
│   ├── 📂 hr/                   # HR screens
│   │   ├── 📂 analytics/        # Analytics Dashboard
│   │   ├── 📂 applicants/       # Applicant Management
│   │   └── 📂 jobs/             # Job Management
│   └── 📂 components/           # Reusable UI Components
│
└── 📂 utils/                    # Utilities
    ├── BindingAdapters.kt
    ├── DateUtils.kt
    └── SessionManager.kt
```

### 🛠️ Công nghệ sử dụng

| Thành phần | Công nghệ |
|------------|-----------|
| **Language** | Kotlin |
| **UI** | XML + Data Binding + View Binding |
| **Architecture** | MVVM (Model-View-ViewModel) |
| **Database** | Room (SQLite) |
| **Navigation** | Jetpack Navigation Component |
| **Async** | Kotlin Coroutines + Flow |
| **DI** | Manual (Application class) |
| **AI Integration** | Google Gemini REST API |
| **Network** | OkHttp + Retrofit |
| **Image Loading** | Glide |

---

## 🚀 Cài đặt & Chạy

### Yêu cầu
- Android Studio Hedgehog (2023.1.1) trở lên
- JDK 17
- Android SDK 26+ (Android 8.0)

### Các bước cài đặt

1. **Clone repository**
   ```bash
   git clone https://github.com/your-username/HUSTle-MobileProgramming.git
   cd HUSTle-MobileProgramming
   ```

2. **Cấu hình API Key**
   ```bash
   cp local.properties.example local.properties
   ```
   Mở file `local.properties` và thêm Gemini API key:
   ```properties
   GEMINI_API_KEY=your_api_key_here
   ```
   > 💡 Lấy API key tại: https://makersuite.google.com/app/apikey

3. **Build & Run**
   ```bash
   ./gradlew assembleDebug
   ```
   Hoặc mở project trong Android Studio và nhấn **Run**.

---

## 📱 Screenshots

| Đăng nhập | Trang chủ | Việc làm | Lộ trình AI |
|:---------:|:---------:|:--------:|:-----------:|
| Login Screen | Social Feed | Job Listing | AI Roadmap |

---

## 🤖 Tích hợp AI Gemini

Ứng dụng sử dụng **Google Gemini 2.5 Flash** để tạo lộ trình phát triển nghề nghiệp:

1. Người dùng nhập thông tin:
   - Vị trí hiện tại
   - Vị trí mục tiêu
   - Kỹ năng hiện có
   - Số năm kinh nghiệm

2. Gemini AI phân tích và tạo lộ trình gồm các bước với:
   - Tiêu đề và mô tả
   - Thời gian dự kiến
   - Kỹ năng cần học

3. Kết quả được hiển thị dưới dạng **Flowchart** với Custom View

---

## 👨‍💻 Tác giả

<table>
  <tr>
    <td align="center">
      <strong>Nguyễn Hữu Hoàng Hải Anh</strong><br/>
      <sub>Sinh viên Global ICT</sub><br/>
      <sub>Đại học Bách khoa Hà Nội (HUST)</sub>
    </td>
  </tr>
</table>

---

## 📄 License

Project này được phát triển cho mục đích học tập trong môn **Lập trình Di động (Mobile Programming)**.

---

<p align="center">
  <strong>🎓 Mobile Programming Project</strong>
</p>
