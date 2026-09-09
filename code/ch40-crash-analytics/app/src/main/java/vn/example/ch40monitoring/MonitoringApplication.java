package vn.example.ch40monitoring;

import android.app.Application;

/**
 * CrashReporter.install() PHẢI chạy CÀNG SỚM CÀNG TỐT — Application.onCreate()
 * (Chương 6) là đúng chỗ, vì nó chạy trước mọi Activity. Cài đặt trễ hơn (ví dụ
 * trong MainActivity.onCreate()) có nguy cơ bỏ lỡ crash xảy ra sớm hơn thế.
 */
public class MonitoringApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReporter.install(this);
    }
}
