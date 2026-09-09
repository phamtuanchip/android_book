package vn.example.ch34aidlserver;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * Service này chạy TRONG TIẾN TRÌNH của app server — khi :client (app khác)
 * bind vào, hệ thống Binder tự động chuyển lời gọi phương thức QUA RANH GIỚI
 * TIẾN TRÌNH, tuần tự hoá tham số, gọi hàm thật ở đây, rồi trả kết quả ngược
 * lại — toàn bộ việc này ẩn sau code Java bình thường nhờ AIDL sinh ra.
 */
public class CalculatorService extends android.app.Service {

    private static final String TAG = "CalculatorService";
    private int callCount = 0;

    private final ICalculatorService.Stub binder = new ICalculatorService.Stub() {
        @Override
        public int add(int a, int b) throws RemoteException {
            callCount++;
            Log.d(TAG, "add(" + a + ", " + b + ") gọi từ tiến trình khác, lần thứ " + callCount);
            return a + b;
        }

        @Override
        public int multiply(int a, int b) throws RemoteException {
            callCount++;
            return a * b;
        }

        @Override
        public int getCallCount() throws RemoteException {
            return callCount;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
