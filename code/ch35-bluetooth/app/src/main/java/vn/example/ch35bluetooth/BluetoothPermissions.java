package vn.example.ch35bluetooth;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

/**
 * Gom logic "quyền nào cần cho việc gì, tuỳ API level" vào một chỗ — đúng tinh
 * thần PrefsManager (Chương 12): mọi Activity khác chỉ gọi qua class này, không
 * tự rải điều kiện Build.VERSION.SDK_INT khắp nơi.
 */
public final class BluetoothPermissions {

    /** Quyền cần để BẮT ĐẦU quét (Classic discovery hoặc BLE scan). */
    public static String[] scanPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return new String[]{Manifest.permission.BLUETOOTH_SCAN};
        }
        return new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    }

    /** Quyền cần để KẾT NỐI/lấy tên thiết bị đã ghép đôi. */
    public static String[] connectPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return new String[]{Manifest.permission.BLUETOOTH_CONNECT};
        }
        // Trước Android 12, BLUETOOTH/BLUETOOTH_ADMIN là quyền "normal" — đã đủ
        // qua khai báo manifest, không cần xin runtime gì thêm ở đây.
        return new String[0];
    }

    public static boolean hasAll(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private BluetoothPermissions() {
    }
}
