package vn.example.ch16service;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import vn.example.ch16service.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements CounterService.CounterListener {

    private ActivityMainBinding binding;
    private CounterService boundService;
    private boolean isBound = false;

    private final ActivityResultLauncher<String> requestNotificationPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    startAndBindService();
                }
            });

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CounterService.LocalBinder localBinder = (CounterService.LocalBinder) service;
            boundService = localBinder.getService();
            boundService.setListener(MainActivity.this);
            isBound = true;
            binding.textCount.setText(getString(R.string.count_format, boundService.getCount()));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            boundService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonStart.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // Từ Android 13 (API 33), hiển thị Notification cũng cần xin quyền
                // runtime — xem chi tiết cơ chế permission ở Chương 26.
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                startAndBindService();
            }
        });

        binding.buttonStop.setOnClickListener(v -> {
            if (isBound) {
                boundService.stopCounting();
                unbindService(connection);
                isBound = false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bù lại việc onStop() luôn unbind: nếu Service VẪN ĐANG CHẠY từ trước (ví
        // dụ người dùng bấm Home rồi mở lại app, thay vì bấm Dừng), phải bind LẠI ở
        // đây — nếu không, isBound/boundService ở trạng thái "chưa kết nối" dù
        // Service (và notification) trên thực tế vẫn đang chạy, khiến nút Dừng
        // không còn tác dụng gì cho tới khi tự tay bấm Bắt đầu lại.
        if (!isBound && CounterService.isRunning()) {
            bindService(new Intent(this, CounterService.class), connection, 0);
        }
    }

    private void startAndBindService() {
        Intent intent = new Intent(this, CounterService.class);
        // startForegroundService(): báo hệ thống Service này SẼ gọi startForeground()
        // ngay sau đó — bắt buộc dùng thay vì startService() khi target API 26+.
        ContextCompat.startForegroundService(this, intent);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onCountChanged(int count) {
        // CounterService gọi callback này từ Handler gắn với main Looper (xem
        // CounterService.tick) nên ở đây đã an toàn để cập nhật UI trực tiếp,
        // không cần runOnUiThread().
        binding.textCount.setText(getString(R.string.count_format, count));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            boundService.setListener(null);
            unbindService(connection);
            isBound = false;
            // Lưu ý: unbind KHÔNG dừng Service nếu nó được khởi động bằng
            // startForegroundService() — Service (và notification) vẫn tiếp tục chạy
            // cho tới khi stopCounting()/stopSelf() được gọi. Đây chính là điểm khác
            // biệt giữa "started" và "bound" nói ở mục 16.4.
        }
    }
}
