package vn.example.ch35bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import vn.example.ch35bluetooth.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements BluetoothChatManager.Listener {

    private ActivityMainBinding binding;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothChatManager chatManager;
    private PairedDeviceAdapter deviceAdapter;

    private final ActivityResultLauncher<String[]> requestConnectPermissions =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), grants -> {
                if (allGranted(grants)) {
                    loadPairedDevices();
                } else {
                    appendLog(getString(R.string.log_permission_denied));
                }
            });

    private final ActivityResultLauncher<String[]> requestServerPermissions =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), grants -> {
                if (allGranted(grants)) {
                    startServer();
                } else {
                    appendLog(getString(R.string.log_permission_denied));
                }
            });

    private boolean allGranted(Map<String, Boolean> grants) {
        for (Boolean granted : grants.values()) {
            if (!granted) return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null) {
            binding.textStatus.setText(R.string.status_no_bluetooth_hardware);
            binding.buttonStartServer.setEnabled(false);
            binding.buttonScanBle.setEnabled(false);
            return;
        }

        chatManager = new BluetoothChatManager(bluetoothAdapter);
        chatManager.setListener(this);

        deviceAdapter = new PairedDeviceAdapter(this::connectToDevice);
        binding.recyclerPairedDevices.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerPairedDevices.setAdapter(deviceAdapter);

        binding.buttonStartServer.setOnClickListener(v -> checkPermissionsThenStartServer());
        binding.buttonScanBle.setOnClickListener(v ->
                startActivity(new Intent(this, BleScanActivity.class)));
        binding.buttonSend.setOnClickListener(v -> {
            String message = binding.editMessage.getText().toString();
            if (!message.isEmpty()) {
                chatManager.sendMessage(message);
                appendLog(getString(R.string.log_sent_format, message));
                binding.editMessage.setText("");
            }
        });

        checkPermissionsThenLoadPaired();
    }

    private void checkPermissionsThenLoadPaired() {
        String[] permissions = BluetoothPermissions.connectPermissions();
        if (permissions.length == 0 || BluetoothPermissions.hasAll(this, permissions)) {
            loadPairedDevices();
        } else {
            requestConnectPermissions.launch(permissions);
        }
    }

    private void checkPermissionsThenStartServer() {
        String[] permissions = BluetoothPermissions.connectPermissions();
        if (permissions.length == 0 || BluetoothPermissions.hasAll(this, permissions)) {
            startServer();
        } else {
            requestServerPermissions.launch(permissions);
        }
    }

    @SuppressLint("MissingPermission") // đã kiểm tra ở checkPermissionsThenLoadPaired()
    private void loadPairedDevices() {
        Set<BluetoothDevice> bonded = bluetoothAdapter.getBondedDevices();
        // getBondedDevices() chỉ trả về thiết bị đã GHÉP ĐÔI (pairing) TỪ TRƯỚC
        // trong Settings hệ thống — code mẫu này không tự thực hiện quy trình
        // ghép đôi (yêu cầu nhập mã PIN qua UI hệ thống), chỉ dùng thiết bị đã
        // ghép đôi sẵn để tập trung vào phần kết nối/truyền dữ liệu.
        deviceAdapter.submitList(new ArrayList<>(bonded));
    }

    private void startServer() {
        chatManager.startServer();
        binding.textStatus.setText(R.string.status_waiting_for_connection);
    }

    private void connectToDevice(BluetoothDevice device) {
        binding.textStatus.setText(R.string.status_connecting);
        chatManager.connectTo(device);
    }

    private void appendLog(String line) {
        String time = DateFormat.format("HH:mm:ss", System.currentTimeMillis()).toString();
        binding.textLog.append("[" + time + "] " + line + "\n");
    }

    @Override
    public void onConnected(String deviceName) {
        binding.textStatus.setText(getString(R.string.status_connected_format, deviceName));
        appendLog(getString(R.string.log_connected_format, deviceName));
    }

    @Override
    public void onMessageReceived(String message) {
        appendLog(getString(R.string.log_received_format, message));
    }

    @Override
    public void onError(String message) {
        appendLog(getString(R.string.log_error_format, message));
    }

    @Override
    public void onDisconnected() {
        binding.textStatus.setText(R.string.status_idle);
        appendLog(getString(R.string.log_disconnected));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chatManager != null) {
            chatManager.stopAll();
        }
    }
}
