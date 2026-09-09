package vn.example.ch33share;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import vn.example.ch33share.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        handleIncomingShare();

        binding.buttonShare.setOnClickListener(v -> shareMessage());
        binding.buttonDeepLink.setOnClickListener(v -> openDeepLink());
    }

    /**
     * Nếu Activity này được mở KHÔNG PHẢI từ icon app mà từ menu "Chia sẻ" của
     * app khác (Chrome, Ảnh, Ghi chú...) — hệ thống gửi tới đây một Intent
     * ACTION_SEND, khớp <intent-filter> khai báo trong manifest. Đây chính là
     * lý do app này XUẤT HIỆN trong Share sheet của các app khác.
     */
    private void handleIncomingShare() {
        Intent intent = getIntent();
        if (Intent.ACTION_SEND.equals(intent.getAction()) && intent.getType() != null
                && intent.getType().startsWith("text/")) {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            binding.textReceivedShare.setVisibility(android.view.View.VISIBLE);
            binding.textReceivedShare.setText(getString(R.string.received_share_format, sharedText));
        }
    }

    /**
     * Gửi MỘT Intent ACTION_SEND, để hệ thống tự liệt kê MỌI app đã cài có khai
     * báo khả năng nhận text/plain (Gmail, Messenger, Zalo, hay chính app này...)
     * — người dùng chọn nơi muốn chia sẻ, app của bạn không cần biết trước danh
     * sách đó.
     */
    private void shareMessage() {
        String message = binding.editMessage.getText().toString().trim();
        if (message.isEmpty()) {
            return;
        }
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);

        // createChooser(): LUÔN hiện hộp thoại "Chia sẻ qua" cho người dùng chọn,
        // kể cả khi chỉ có một app khớp — khác gọi startActivity(sendIntent) trực
        // tiếp (có thể tự mở thẳng app duy nhất khớp mà không hỏi, gây bất ngờ).
        startActivity(Intent.createChooser(sendIntent, getString(R.string.chooser_title)));
    }

    /**
     * Mở đúng URI mà một Deep Link thật sự sẽ gửi tới app (từ một thông báo, một
     * trang web, hay lệnh `adb shell am start -a android.intent.action.VIEW -d
     * "ch33share://note/42"`). Ở đây tự bắn Intent để demo không cần nguồn ngoài.
     */
    private void openDeepLink() {
        Uri uri = Uri.parse("ch33share://note/42");
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
