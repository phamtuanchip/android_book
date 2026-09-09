package vn.example.ch40monitoring;

import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import vn.example.ch40monitoring.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showPendingCrashLogsIfAny();

        binding.buttonCrash.setOnClickListener(v -> {
            // Cố tình gây NullPointerException — CrashReporter.install() (đăng ký
            // trong MonitoringApplication) sẽ bắt được, ghi log, RỒI MỚI để hệ
            // thống đóng app như bình thường (không "cứu" được app khỏi crash,
            // vì CHỦ ĐÍCH của chương này là quan sát và ghi lại, không phải chặn).
            String value = null;
            appendLog(value.trim());
        });

        binding.buttonClearCrashLogs.setOnClickListener(v -> {
            CrashReporter.clearCrashLogs(this);
            appendLog(getString(R.string.log_crashes_cleared));
        });

        binding.buttonLogEvent.setOnClickListener(v -> {
            AnalyticsLogger.logEvent("button_clicked", "button_id", "log_event",
                    "screen", "MainActivity");
            appendLog(getString(R.string.log_event_queued));
        });

        AnalyticsLogger.start(batch -> runOnUiThread(() -> {
            appendLog(getString(R.string.log_batch_flushed_format, batch.size()));
            for (String event : batch) {
                appendLog("  " + event);
            }
        }));
    }

    private void showPendingCrashLogsIfAny() {
        List<String> logs = CrashReporter.getPendingCrashLogs(this);
        if (logs.isEmpty()) {
            appendLog(getString(R.string.log_no_previous_crash));
            return;
        }
        appendLog(getString(R.string.log_found_crashes_format, logs.size()));
        for (String log : logs) {
            appendLog(log);
        }
    }

    private void appendLog(String line) {
        String time = DateFormat.format("HH:mm:ss", System.currentTimeMillis()).toString();
        binding.textLog.append("[" + time + "] " + line + "\n");
    }
}
