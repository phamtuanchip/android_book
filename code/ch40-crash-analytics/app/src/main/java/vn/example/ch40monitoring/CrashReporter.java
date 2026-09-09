package vn.example.ch40monitoring;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Cài đặt tối giản cho đúng cơ chế mà Firebase Crashlytics (và các dịch vụ
 * tương tự) tự động hoá: bắt MỌI exception không được xử lý (uncaught) TRƯỚC
 * KHI hệ thống buộc phải giết tiến trình, ghi lại đầy đủ ngữ cảnh, rồi (ở đây)
 * lưu xuống file cục bộ — một dịch vụ thật sẽ thay bước cuối bằng việc TẢI LÊN
 * SERVER ngay khi có mạng trở lại (Chương 21-23 đã dạy đủ kỹ thuật cần cho việc
 * đó: OkHttp, WorkManager để đảm bảo tải lên được kể cả app đã đóng).
 */
public final class CrashReporter {

    private static final String TAG = "CrashReporter";
    private static final String CRASH_DIR = "crash_logs";

    public static void install(Context context) {
        Context appContext = context.getApplicationContext();
        // GIỮ LẠI handler mặc định của hệ thống — không bao giờ được phép "nuốt"
        // crash một cách âm thầm. Sau khi ghi log xong, PHẢI gọi lại handler gốc
        // để hệ thống tiếp tục quy trình chuẩn (hiện dialog "App đã dừng", dọn
        // dẹp tiến trình...). Bỏ qua bước này khiến app ở trạng thái treo bất
        // thường thay vì đóng gọn gàng.
        Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            try {
                writeCrashLog(appContext, thread, throwable);
            } catch (Exception loggingFailure) {
                // Chính việc GHI LOG cũng có thể lỗi — không được để lỗi này che
                // mất exception gốc hay ngăn app đóng, chỉ log ra Logcat rồi thôi.
                Log.e(TAG, "Không ghi được crash log", loggingFailure);
            }
            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, throwable);
            }
        });
    }

    private static void writeCrashLog(Context context, Thread thread, Throwable throwable) throws IOException {
        File dir = new File(context.getFilesDir(), CRASH_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(new Date());
        File logFile = new File(dir, "crash-" + timestamp + ".txt");

        StringWriter stackTraceWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stackTraceWriter));

        try (FileWriter writer = new FileWriter(logFile)) {
            writer.write("Thời điểm: " + new Date() + "\n");
            writer.write("Thread: " + thread.getName() + "\n");
            writer.write("App version: " + appVersionName(context) + "\n");
            writer.write("Thiết bị: " + Build.MANUFACTURER + " " + Build.MODEL
                    + " (Android " + Build.VERSION.RELEASE + ", API " + Build.VERSION.SDK_INT + ")\n");
            writer.write("--- Stack trace ---\n");
            writer.write(stackTraceWriter.toString());
        }
    }

    private static String appVersionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "unknown";
        }
    }

    /** Đọc lại các crash log CÒN TỒN ĐỌNG từ lần chạy trước — một dịch vụ thật
     * sẽ tự tải chúng lên server ở đây, rồi xoá bản cục bộ sau khi tải xong. */
    public static List<String> getPendingCrashLogs(Context context) {
        List<String> logs = new ArrayList<>();
        File dir = new File(context.getApplicationContext().getFilesDir(), CRASH_DIR);
        File[] files = dir.listFiles();
        if (files == null) {
            return logs;
        }
        for (File file : files) {
            try {
                logs.add(readFile(file));
            } catch (IOException ignored) {
            }
        }
        return logs;
    }

    public static void clearCrashLogs(Context context) {
        File dir = new File(context.getApplicationContext().getFilesDir(), CRASH_DIR);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    private static String readFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    private CrashReporter() {
    }
}
