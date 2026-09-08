package vn.example.ch17broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import vn.example.ch17broadcast.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements PingReceiver.Listener {

    private ActivityMainBinding binding;
    private int pingCount = 0;
    private int pingSequence = 0;

    private final PingReceiver pingReceiver = new PingReceiver(this);

    // ACTION_BATTERY_CHANGED là "sticky broadcast" — hệ thống luôn giữ sẵn giá trị
    // mới nhất, nên đăng ký receiver này TẠI BẤT KỲ THỜI ĐIỂM NÀO cũng nhận được
    // ngay giá trị hiện tại, không cần đợi lần thay đổi tiếp theo.
    private final BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            if (level >= 0 && scale > 0) {
                int percent = Math.round(level * 100f / scale);
                binding.textBattery.setText(getString(R.string.battery_format, percent));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonSendPing.setOnClickListener(v -> sendPingBroadcast());
    }

    private void sendPingBroadcast() {
        Intent intent = new Intent(PingReceiver.ACTION_PING);
        intent.putExtra(PingReceiver.EXTRA_SEQUENCE, ++pingSequence);
        // setPackage(getPackageName()): giới hạn broadcast này chỉ trong phạm vi app
        // của chính mình — từ Android 8 (API 26), broadcast KHÔNG chỉ định package
        // như vậy cho hầu hết action tự định nghĩa sẽ không tới được receiver nào
        // đăng ký qua manifest (chỉ receiver đăng ký ĐỘNG lúc runtime, như ở đây,
        // mới chắc chắn nhận được).
        intent.setPackage(getPackageName());
        sendBroadcast(intent);
    }

    @Override
    public void onPingReceived(int sequence) {
        pingCount++;
        binding.textPingCount.setText(getString(R.string.ping_count_format, pingCount, sequence));
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Đăng ký ĐỘNG (runtime) — cách DUY NHẤT còn hoạt động cho hầu hết broadcast
        // hệ thống từ Android 8 trở lên, xem mục 17.3.
        ContextCompat.registerReceiver(this, batteryReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED);
        ContextCompat.registerReceiver(this, pingReceiver,
                new IntentFilter(PingReceiver.ACTION_PING),
                ContextCompat.RECEIVER_NOT_EXPORTED);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Luôn hủy đăng ký khi không còn cần — receiver đăng ký động mà quên gỡ sẽ
        // rò rỉ (giữ Activity sống) và tiếp tục nhận broadcast dù màn hình đã ẩn.
        unregisterReceiver(batteryReceiver);
        unregisterReceiver(pingReceiver);
    }
}
