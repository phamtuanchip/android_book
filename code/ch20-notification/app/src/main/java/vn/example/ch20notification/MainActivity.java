package vn.example.ch20notification;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import vn.example.ch20notification.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final int REMINDER_NOTIFICATION_ID = 100;

    private ActivityMainBinding binding;

    private final ActivityResultLauncher<String> requestPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    sendReminderNotification();
                } else {
                    binding.textStatus.setText(R.string.permission_denied);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonSend.setOnClickListener(v -> sendWithPermissionCheck());

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // MainActivity khai báo launchMode="singleTop" trong manifest — bấm vào
        // notification khi app đã mở sẵn sẽ gọi onNewIntent() thay vì tạo Activity
        // mới chồng lên, xem lại Chương 6 nếu cần ôn lại vòng đời liên quan.
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent.getBooleanExtra(NotificationHelper.EXTRA_OPENED_FROM_NOTIFICATION, false)) {
            binding.textStatus.setText(R.string.opened_from_notification);
        }
    }

    private void sendWithPermissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission.launch(Manifest.permission.POST_NOTIFICATIONS);
        } else {
            sendReminderNotification();
        }
    }

    private void sendReminderNotification() {
        NotificationHelper.showReminder(
                this,
                REMINDER_NOTIFICATION_ID,
                getString(R.string.reminder_title),
                getString(R.string.reminder_body));
        binding.textStatus.setText(R.string.notification_sent);
    }
}
