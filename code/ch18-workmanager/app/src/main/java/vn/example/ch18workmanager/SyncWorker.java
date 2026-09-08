package vn.example.ch18workmanager;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * doWork() chạy trên MỘT THREAD NỀN do WorkManager tự quản lý — không bao giờ
 * chạy trên main thread, nên có thể Thread.sleep()/gọi mạng trực tiếp ở đây mà
 * không cần tự tạo Executor như ở Chương 13/14.
 */
public class SyncWorker extends Worker {

    private static final String TAG = "SyncWorker";
    public static final String KEY_SYNCED_AT = "synced_at";

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Bắt đầu đồng bộ...");
        try {
            // Giả lập một tác vụ mạng/xử lý tốn thời gian
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Result.retry();
        }

        String syncedAt = DateFormat.format("HH:mm:ss", System.currentTimeMillis()).toString();
        Log.d(TAG, "Đồng bộ xong lúc " + syncedAt);

        Data output = new Data.Builder()
                .putString(KEY_SYNCED_AT, syncedAt)
                .build();
        return Result.success(output);
    }
}
