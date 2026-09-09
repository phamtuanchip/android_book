package vn.example.ch34aidlserver;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * App này không cần giao diện phức tạp — chỉ cần được CÀI ĐẶT (không nhất
 * thiết phải đang mở) để CalculatorService sẵn sàng cho app :client bind vào.
 * Android tự khởi động tiến trình của app này khi có app khác gọi bindService()
 * nếu nó chưa chạy sẵn.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
