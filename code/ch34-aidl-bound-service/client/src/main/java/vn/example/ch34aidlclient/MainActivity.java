package vn.example.ch34aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.appcompat.app.AppCompatActivity;

import vn.example.ch34aidlclient.databinding.ActivityMainBinding;
import vn.example.ch34aidlserver.ICalculatorService;

/**
 * App RIÊNG BIỆT (package vn.example.ch34aidlclient) — không chứa dòng code
 * nào của app Calculator Server, chỉ "biết" file .aidl (chép sang, cùng gói
 * vn.example.ch34aidlserver để mã Java sinh ra khớp interface) và action dùng
 * để bind.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ICalculatorService calculatorService;
    private boolean isBound = false;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            // asInterface(): bọc IBinder thô nhận được qua ranh giới tiến trình
            // thành lời gọi hàm Java thông thường — mọi việc tuần tự hoá/gọi
            // Binder transaction thật sự đều ẩn phía sau dòng này.
            calculatorService = ICalculatorService.Stub.asInterface(binder);
            isBound = true;
            binding.textConnectionStatus.setText(R.string.status_connected);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // Gọi khi tiến trình SERVER bị hệ thống kill đột ngột — KHÁC hẳn
            // unbindService() chủ động từ phía mình (Chương 16 chỉ gặp trường
            // hợp cùng tiến trình nên ít khi thấy callback này kích hoạt thật).
            isBound = false;
            calculatorService = null;
            binding.textConnectionStatus.setText(R.string.status_disconnected_unexpectedly);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonAdd.setOnClickListener(v -> compute(true));
        binding.buttonMultiply.setOnClickListener(v -> compute(false));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent("vn.example.ch34aidlserver.action.BIND_CALCULATOR");
        // setPackage() BẮT BUỘC cho implicit intent nhắm tới app khác từ Android
        // 5.0 trở lên — thiếu dòng này, bindService() không tìm thấy Service nào
        // cả, dù action khớp chính xác.
        intent.setPackage("vn.example.ch34aidlserver");
        boolean started = bindService(intent, connection, Context.BIND_AUTO_CREATE);
        if (!started) {
            binding.textConnectionStatus.setText(R.string.status_server_not_installed);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }

    private void compute(boolean isAdd) {
        if (!isBound) {
            binding.textResult.setText(R.string.status_not_connected);
            return;
        }
        try {
            int a = Integer.parseInt(binding.editA.getText().toString());
            int b = Integer.parseInt(binding.editB.getText().toString());
            // Lời gọi này TRÔNG như một hàm Java bình thường, nhưng thật ra đang
            // đi qua Binder sang TIẾN TRÌNH KHÁC và quay lại — luôn có thể ném
            // RemoteException nếu tiến trình bên kia chết giữa chừng.
            int result = isAdd ? calculatorService.add(a, b) : calculatorService.multiply(a, b);
            int callCount = calculatorService.getCallCount();
            binding.textResult.setText(getString(R.string.result_format, result, callCount));
        } catch (NumberFormatException e) {
            binding.textResult.setText(R.string.error_invalid_number);
        } catch (RemoteException e) {
            binding.textResult.setText(getString(R.string.error_remote_format, e.getMessage()));
        }
    }
}
