package vn.example.ch18workmanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import vn.example.ch18workmanager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private WorkManager workManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        workManager = WorkManager.getInstance(this);

        binding.buttonSyncNow.setOnClickListener(v -> enqueueOneTimeSync());
        binding.buttonSchedulePeriodic.setOnClickListener(v -> schedulePeriodicSync());
    }

    private void enqueueOneTimeSync() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SyncWorker.class)
                .setConstraints(constraints)
                .build();

        workManager.enqueue(request);
        binding.textStatus.setText(R.string.status_enqueued);

        // Quan sát trạng thái công việc theo thời gian thực: ENQUEUED → RUNNING →
        // SUCCEEDED/FAILED. WorkManager tự lưu trạng thái này xuống database nội bộ,
        // tồn tại cả khi Activity bị huỷ và tạo lại.
        workManager.getWorkInfoByIdLiveData(request.getId()).observe(this, this::onWorkInfoChanged);
    }

    private void onWorkInfoChanged(WorkInfo workInfo) {
        if (workInfo == null) {
            return;
        }
        switch (workInfo.getState()) {
            case RUNNING:
                binding.textStatus.setText(R.string.status_running);
                break;
            case SUCCEEDED:
                String syncedAt = workInfo.getOutputData().getString(SyncWorker.KEY_SYNCED_AT);
                binding.textStatus.setText(getString(R.string.status_succeeded, syncedAt));
                break;
            case FAILED:
                binding.textStatus.setText(R.string.status_failed);
                break;
            default:
                // ENQUEUED, BLOCKED, CANCELLED — không cần xử lý riêng trong demo này
                break;
        }
    }

    private void schedulePeriodicSync() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // 15 phút là chu kỳ TỐI THIỂU WorkManager cho phép — đặt số nhỏ hơn sẽ bị
        // tự động nâng lên 15. Không phù hợp để "test thấy ngay" như OneTimeWorkRequest.
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(SyncWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        // enqueueUniquePeriodicWork: đảm bảo dù bấm nút này nhiều lần cũng chỉ có
        // ĐÚNG MỘT lịch chạy định kỳ tồn tại, không tạo thêm bản trùng lặp.
        workManager.enqueueUniquePeriodicWork(
                "periodic_sync", ExistingPeriodicWorkPolicy.KEEP, request);
        binding.textPeriodicNote.setText(R.string.note_periodic_scheduled);
    }
}
