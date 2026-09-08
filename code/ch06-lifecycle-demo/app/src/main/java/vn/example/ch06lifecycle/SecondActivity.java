package vn.example.ch06lifecycle;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "Lifecycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "SecondActivity onCreate");
        setContentView(R.layout.activity_second);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "SecondActivity onResume — lúc này MainActivity đã onPause/onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "SecondActivity onDestroy");
    }
}
