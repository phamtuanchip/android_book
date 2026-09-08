package vn.example.ch19async;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import vn.example.ch19async.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    // Một thread nền riêng cho tác vụ tính toán nặng — KHÔNG BAO GIỜ chạy việc này
    // trực tiếp trên main thread, sẽ làm đứng UI hoàn toàn tới khi tính xong.
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future<?> currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonStart.setOnClickListener(v -> startCounting());
        binding.buttonCancel.setOnClickListener(v -> cancelCounting());
    }

    private void startCounting() {
        int limit;
        try {
            limit = Integer.parseInt(binding.editLimit.getText().toString().trim());
        } catch (NumberFormatException e) {
            binding.textStatus.setText(R.string.status_invalid_input);
            return;
        }

        binding.progressBar.setProgress(0);
        binding.textStatus.setText(R.string.status_running);

        // submit() trả về Future — cần giữ lại để có thể cancel(true) sau này.
        // PrimeCounter.countPrimes chạy TRÊN THREAD NỀN của executor, nên callback
        // onProgress() ở đây KHÔNG được đụng trực tiếp vào View — phải quay lại
        // main thread bằng runOnUiThread().
        currentTask = executor.submit(() -> {
            int result = PrimeCounter.countPrimes(limit,
                    percent -> runOnUiThread(() -> binding.progressBar.setProgress(percent)));

            boolean wasCancelled = Thread.currentThread().isInterrupted();
            runOnUiThread(() -> binding.textStatus.setText(
                    wasCancelled
                            ? getString(R.string.status_cancelled, result)
                            : getString(R.string.status_done, result)));
        });
    }

    private void cancelCounting() {
        if (currentTask != null) {
            // cancel(true): nếu task đang chạy, gọi interrupt() lên thread đang thực
            // thi nó — PrimeCounter.countPrimes() kiểm tra cờ này mỗi vòng lặp để
            // dừng SỚM một cách chủ động, không phải bị "giết" ngay lập tức.
            currentTask.cancel(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // shutdownNow(): huỷ mọi task đang chờ/đang chạy — Activity không còn tồn
        // tại thì không có lý do gì để executor tiếp tục chiếm tài nguyên.
        executor.shutdownNow();
    }
}
