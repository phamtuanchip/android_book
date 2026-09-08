package vn.example.ch16service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

/**
 * Một Service vừa là "started" (khởi động bằng startForegroundService(), tự chạy
 * độc lập kể cả khi không Activity nào bind vào nó) vừa là "bound" (Activity có
 * thể bind() để lấy dữ liệu trực tiếp qua Binder, xem mục 16.4).
 */
public class CounterService extends Service {

    private static final String CHANNEL_ID = "counter_channel";
    private static final int NOTIFICATION_ID = 1;

    public interface CounterListener {
        void onCountChanged(int count);
    }

    /** Binder trả về chính instance Service — chỉ dùng được vì client và Service
     * cùng chạy trong một tiến trình (không qua ranh giới process/app khác, đó là
     * việc của AIDL ở Chương 34). */
    public class LocalBinder extends Binder {
        public CounterService getService() {
            return CounterService.this;
        }
    }

    private final IBinder binder = new LocalBinder();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private int count = 0;
    private boolean running = false;
    private CounterListener listener;

    private final Runnable tick = new Runnable() {
        @Override
        public void run() {
            if (!running) {
                return;
            }
            count++;
            updateNotification();
            if (listener != null) {
                listener.onCountChanged(count);
            }
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // BẮT BUỘC gọi startForeground() trong vài giây đầu sau khi Service khởi
        // động bằng startForegroundService() — không gọi kịp, hệ thống sẽ tự dừng
        // Service và ném ANR (Android 8.0 trở lên).
        startForeground(NOTIFICATION_ID, buildNotification());
        if (!running) {
            running = true;
            handler.post(tick);
        }
        // START_STICKY: nếu hệ thống kill tiến trình để giải phóng bộ nhớ, tự khởi
        // động lại Service sau đó (với intent = null) — phù hợp cho tác vụ nên tiếp
        // tục chạy. START_NOT_STICKY thì không tự khởi động lại.
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setListener(@Nullable CounterListener listener) {
        this.listener = listener;
    }

    public int getCount() {
        return count;
    }

    public void stopCounting() {
        running = false;
        handler.removeCallbacks(tick);
        stopForeground(true);
        stopSelf();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Counter Service", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private Notification buildNotification() {
        Intent openAppIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, openAppIntent,
                PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text_format, count))
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
    }

    private void updateNotification() {
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.notify(NOTIFICATION_ID, buildNotification());
    }
}
