package vn.example.ch06lifecycle;

import android.app.Application;
import android.util.Log;

/**
 * onCreate() ở đây chạy đúng 1 lần khi TIẾN TRÌNH của app được tạo — trước cả
 * Activity đầu tiên. Nơi phù hợp để khởi tạo thư viện dùng chung toàn app
 * (analytics, crash reporting...), KHÔNG phải nơi làm việc nặng/chặn UI.
 */
public class DemoApplication extends Application {

    private static final String TAG = "Lifecycle";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "DemoApplication onCreate — tiến trình app vừa khởi tạo");
    }
}
