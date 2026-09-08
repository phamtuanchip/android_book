package vn.example.ch11resources;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Không cần logic Java nào riêng — toàn bộ nội dung chương này thể hiện qua
        // việc Android tự chọn đúng file resource theo qualifier, xem README.
    }
}
