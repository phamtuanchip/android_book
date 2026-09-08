package vn.example.ch20notification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ReminderApplication extends Application {

    public static final String CHANNEL_ID = "reminders";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return; // NotificationChannel chỉ tồn tại từ Android 8 (API 26) trở lên
        }
        // Tạo channel MỘT LẦN, càng sớm càng tốt (ở đây là lúc app khởi động) — gọi
        // lại createNotificationChannel() với cùng ID nhiều lần sau đó là AN TOÀN
        // (không tạo trùng), nhưng KHÔNG THỂ đổi importance của channel đã tồn tại
        // bằng code — người dùng phải tự đổi trong Settings nếu muốn.
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(getString(R.string.channel_description));

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }
}
