package vn.example.ch20notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Xử lý khi người dùng bấm nút hành động NGAY TRÊN thông báo, không cần mở app.
 * Đây là ví dụ thực tế của BroadcastReceiver (Chương 17) phối hợp với Notification.
 */
public class MarkReadReceiver extends BroadcastReceiver {

    public static final String ACTION_MARK_READ = "vn.example.ch20notification.ACTION_MARK_READ";
    public static final String EXTRA_NOTIFICATION_ID = "extra_notification_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1);
        if (notificationId != -1) {
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.cancel(notificationId);
        }
        Toast.makeText(context, R.string.marked_read_toast, Toast.LENGTH_SHORT).show();
    }
}
