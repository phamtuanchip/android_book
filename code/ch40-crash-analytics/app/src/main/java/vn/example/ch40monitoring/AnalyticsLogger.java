package vn.example.ch40monitoring;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Minh hoạ đúng kỹ thuật các SDK analytics thật (Firebase Analytics, Amplitude,
 * Mixpanel...) đều dùng: KHÔNG gửi một request mạng cho MỖI sự kiện (quá tốn
 * pin/dữ liệu di động) — gom (batch) nhiều sự kiện lại, gửi ĐỊNH KỲ một lần.
 */
public final class AnalyticsLogger {

    public interface EventListener {
        void onBatchFlushed(List<String> events);
    }

    private static final String TAG = "AnalyticsLogger";
    private static final long FLUSH_INTERVAL_SECONDS = 10;

    private static final List<String> pendingEvents = new ArrayList<>();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static EventListener listener;
    private static boolean started = false;

    public static synchronized void start(EventListener eventListener) {
        listener = eventListener;
        if (started) {
            return;
        }
        started = true;
        // Gửi định kỳ, KHÔNG gửi ngay mỗi lần logEvent() được gọi — đây chính
        // là điểm khác biệt so với cách viết log thông thường (Log.d chạy tức
        // thời), và là lý do vì sao đôi khi sự kiện analytics "xuất hiện trễ"
        // trên dashboard thật, không phải lỗi của SDK.
        scheduler.scheduleWithFixedDelay(AnalyticsLogger::flush,
                FLUSH_INTERVAL_SECONDS, FLUSH_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public static synchronized void logEvent(String name, String... paramsKeyValue) {
        String timestamp = new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date());
        StringBuilder line = new StringBuilder("[" + timestamp + "] " + name);
        for (int i = 0; i + 1 < paramsKeyValue.length; i += 2) {
            line.append(" ").append(paramsKeyValue[i]).append("=").append(paramsKeyValue[i + 1]);
        }
        pendingEvents.add(line.toString());
        Log.d(TAG, "Đã xếp hàng: " + line + " (chờ đợt gửi tiếp theo)");
    }

    private static synchronized void flush() {
        if (pendingEvents.isEmpty()) {
            return;
        }
        List<String> batch = new ArrayList<>(pendingEvents);
        pendingEvents.clear();
        Log.d(TAG, "Gửi đợt gồm " + batch.size() + " sự kiện (giả lập — thực tế sẽ là 1 request mạng)");
        if (listener != null) {
            listener.onBatchFlushed(batch);
        }
    }

    private AnalyticsLogger() {
    }
}
