package vn.example.ch35bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import vn.example.ch35bluetooth.databinding.ActivityBleScanBinding;

public class BleScanActivity extends AppCompatActivity {

    // BLE quét theo cách "quảng bá liên tục" (advertising) — không cần ghép đôi
    // để THẤY thiết bị, khác Bluetooth Classic ở MainActivity (phải ghép đôi từ
    // Settings trước khi getBondedDevices() liệt kê ra được).
    private static final long SCAN_DURATION_MS = 12_000;

    private ActivityBleScanBinding binding;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner scanner;
    private BleDeviceAdapter adapter;
    private boolean scanning = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    // key = địa chỉ MAC — dùng Map để mỗi thiết bị chỉ xuất hiện MỘT lần trong
    // danh sách dù quảng bá liên tục gửi nhiều gói tin trùng lặp.
    private final Map<String, BleDeviceItem> foundDevices = new LinkedHashMap<>();

    private final ActivityResultLauncher<String[]> requestScanPermissions =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), grants -> {
                boolean allGranted = true;
                for (Boolean granted : grants.values()) {
                    allGranted &= granted;
                }
                if (allGranted) {
                    startScan();
                } else {
                    binding.textScanStatus.setText(R.string.scan_status_permission_denied);
                }
            });

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, @NonNull ScanResult result) {
            String address = result.getDevice().getAddress();
            String name = result.getScanRecord() != null && result.getScanRecord().getDeviceName() != null
                    ? result.getScanRecord().getDeviceName()
                    : getString(R.string.ble_unnamed_device);
            foundDevices.put(address, new BleDeviceItem(name, address, result.getRssi()));
            adapter.submitList(new ArrayList<>(foundDevices.values()));
        }

        @Override
        public void onScanFailed(int errorCode) {
            binding.textScanStatus.setText(getString(R.string.scan_status_failed_format, errorCode));
            scanning = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBleScanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            binding.textScanStatus.setText(R.string.scan_status_no_ble);
            binding.buttonToggleScan.setEnabled(false);
        }

        adapter = new BleDeviceAdapter();
        binding.recyclerBleDevices.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerBleDevices.setAdapter(adapter);

        binding.buttonToggleScan.setOnClickListener(v -> {
            if (scanning) {
                stopScan();
            } else {
                String[] permissions = BluetoothPermissions.scanPermissions();
                if (BluetoothPermissions.hasAll(this, permissions)) {
                    startScan();
                } else {
                    requestScanPermissions.launch(permissions);
                }
            }
        });
    }

    @SuppressLint("MissingPermission") // đã kiểm tra quyền ngay trước khi gọi startScan()
    private void startScan() {
        // Lấy lại BluetoothLeScanner MỖI LẦN bấm quét (thay vì cache một lần ở
        // onCreate) — getBluetoothLeScanner() trả về null nếu Bluetooth đang tắt,
        // và người dùng hoàn toàn có thể bật/tắt Bluetooth trong lúc app đang mở.
        BluetoothLeScanner scanner = bluetoothAdapter != null ? bluetoothAdapter.getBluetoothLeScanner() : null;
        if (scanner == null) {
            binding.textScanStatus.setText(R.string.scan_status_no_ble);
            return;
        }
        this.scanner = scanner;
        foundDevices.clear();
        adapter.submitList(new ArrayList<>());
        scanning = true;
        binding.buttonToggleScan.setText(R.string.button_stop_scan);
        binding.textScanStatus.setText(R.string.scan_status_scanning);
        scanner.startScan(scanCallback);

        // Tự dừng sau một khoảng thời gian cố định — quét BLE liên tục vô thời
        // hạn tốn pin đáng kể, một app thật nên luôn giới hạn thời gian quét.
        handler.postDelayed(this::stopScan, SCAN_DURATION_MS);
    }

    @SuppressLint("MissingPermission")
    private void stopScan() {
        if (!scanning || scanner == null) return;
        scanner.stopScan(scanCallback);
        scanner = null;
        scanning = false;
        binding.buttonToggleScan.setText(R.string.button_start_scan);
        binding.textScanStatus.setText(getString(R.string.scan_status_done_format, foundDevices.size()));
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopScan(); // không bao giờ để quét chạy ngầm khi màn hình đã ẩn
    }
}
