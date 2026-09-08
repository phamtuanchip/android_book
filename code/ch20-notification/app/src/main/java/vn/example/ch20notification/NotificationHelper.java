package vn.example.ch20notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public final class NotificationHelper {

    public static final String EXTRA_OPENED_FROM_NOTIFICATION = "opened_from_notification";

    public static void showReminder(Context context, int notificationId, String title, String longText) {
        // Tap vào notification -> mở MainActivity, kèm cờ để Activity biết nó được
        // mở từ đâu (xem MainActivity.onNewIntent).
        Intent contentIntent = new Intent(context, MainActivity.class);
        contentIntent.putExtra(EXTRA_OPENED_FROM_NOTIFICATION, true);
        contentIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(
                context, notificationId, contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Bấm nút hành động NGAY TRÊN thông báo -> gửi broadcast, không mở app.
        Intent markReadIntent = new Intent(context, MarkReadReceiver.class);
        markReadIntent.setAction(MarkReadReceiver.ACTION_MARK_READ);
        markReadIntent.putExtra(MarkReadReceiver.EXTRA_NOTIFICATION_ID, notificationId);
        PendingIntent markReadPendingIntent = PendingIntent.getBroadcast(
                context, notificationId, markReadIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ReminderApplication.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(longText)
                // BigTextStyle: cho phép hiển thị đầy đủ nội dung dài khi người dùng
                // kéo giãn thông báo, thay vì luôn bị cắt ngắn ở một dòng.
                .setStyle(new NotificationCompat.BigTextStyle().bigText(longText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)   // tự đóng notification khi người dùng bấm vào nó
                .addAction(android.R.drawable.ic_menu_send, context.getString(R.string.action_mark_read), markReadPendingIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationId, builder.build());
    }

    private NotificationHelper() {
    }
}
