package com.example.hustleapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.hustleapp.databinding.ActivityMainBinding

/**
 * MainActivity - Activity chính và duy nhất của ứng dụng
 * 
 * Ứng dụng sử dụng kiến trúc Single Activity Architecture:
 * - Chỉ có 1 Activity duy nhất
 * - Các màn hình khác nhau được thể hiện bằng các Fragment
 * - Navigation Component quản lý việc chuyển đổi giữa các Fragment
 * 
 * Ưu điểm của Single Activity:
 * - Dễ dàng chia sẻ dữ liệu giữa các màn hình thông qua ViewModel
 * - Quản lý navigation tập trung và nhất quán
 * - Hiệu suất tốt hơn vì không cần tạo mới Activity
 */
class MainActivity : AppCompatActivity() {
    
    // Binding cho layout activity_main.xml
    // Data Binding giúp truy cập các view mà không cần findViewById()
    private lateinit var binding: ActivityMainBinding
    
    /**
     * Được gọi khi Activity được tạo
     * Đây là nơi khởi tạo UI và thiết lập các listener
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Thiết lập layout sử dụng Data Binding
        // DataBindingUtil tự động inflate layout và tạo binding class
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        
        // Xử lý hiển thị edge-to-edge (tràn viền)
        // Cho phép nội dung hiển thị phía sau thanh status bar và navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            // Lấy kích thước của các thanh hệ thống (status bar, navigation bar)
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Thêm padding để nội dung không bị che bởi thanh hệ thống
            // Lưu ý: bottom = 0 vì Fragment sẽ tự xử lý bottom navigation
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }
}