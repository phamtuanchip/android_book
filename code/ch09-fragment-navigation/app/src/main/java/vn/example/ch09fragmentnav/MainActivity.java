package vn.example.ch09fragmentnav;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity chỉ đóng vai trò "vỏ" chứa NavHostFragment (khai báo trong
 * activity_main.xml) — mọi nội dung và điều hướng thật sự nằm ở các Fragment
 * và nav_graph.xml. Đây là mô hình "single-activity" phổ biến hiện nay.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
