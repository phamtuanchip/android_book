package vn.example.ch08intentnav;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import vn.example.ch08intentnav.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "extra_message";
    public static final String EXTRA_REPLY = "extra_reply";

    private ActivityMainBinding binding;

    // Activity Result API: cách hiện đại thay cho startActivityForResult() đã lỗi thời.
    // Đăng ký launcher ở đây (phải đăng ký TRƯỚC khi Activity resume), gọi launcher.launch() lúc cần.
    private final ActivityResultLauncher<Intent> secondActivityLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String reply = result.getData().getStringExtra(EXTRA_REPLY);
                    binding.textResultFromSecond.setText(getString(R.string.result_reply_format, reply));
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonSend.setOnClickListener(v -> {
            String message = binding.editMessage.getText().toString().trim();
            // Explicit Intent: chỉ đích danh class Activity muốn mở — dùng khi mở
            // màn hình NGAY TRONG app của mình.
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            secondActivityLauncher.launch(intent);
        });

        binding.buttonOpenBrowser.setOnClickListener(v -> {
            // Implicit Intent: chỉ mô tả HÀNH ĐỘNG (ACTION_VIEW) + dữ liệu (URI), để hệ
            // thống tự tìm app nào xử lý được — có thể là app của Google, Cốc Cốc, hay
            // bất kỳ trình duyệt nào người dùng đã cài, mình không cần biết trước.
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com"));
            startActivity(intent);
        });
    }
}
