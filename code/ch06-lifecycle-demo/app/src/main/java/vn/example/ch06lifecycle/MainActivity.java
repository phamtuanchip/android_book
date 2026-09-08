package vn.example.ch06lifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Mở app, bấm "Mở màn hình 2", rồi bấm Back và Home để xem thứ tự các callback
 * dưới đây in ra Logcat (lọc theo tag "Lifecycle"). Xem Chương 6.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Lifecycle";
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MainActivity onCreate — savedInstanceState=" + savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            counter = savedInstanceState.getInt("counter", 0);
        }

        Button openSecond = findViewById(R.id.buttonOpenSecond);
        openSecond.setOnClickListener(v -> startActivity(new Intent(this, SecondActivity.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "MainActivity onStart — activity sắp hiển thị (visible)");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity onResume — activity ở foreground, nhận tương tác người dùng");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "MainActivity onPause — activity khác che một phần/toàn bộ màn hình");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity onStop — activity không còn hiển thị");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "MainActivity onRestart — quay lại từ trạng thái Stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity onDestroy — activity bị huỷ hoàn toàn");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Được gọi trước khi hệ thống có thể huỷ Activity (vd xoay màn hình) — nơi lưu
        // trạng thái UI tạm thời để khôi phục lại trong onCreate(savedInstanceState).
        outState.putInt("counter", ++counter);
        Log.d(TAG, "MainActivity onSaveInstanceState — lưu counter=" + counter);
    }
}
