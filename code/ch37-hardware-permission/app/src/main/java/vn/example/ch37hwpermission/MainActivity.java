package vn.example.ch37hwpermission;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import vn.example.ch37hwpermission.databinding.ActivityMainBinding;

/**
 * Minh hoạ ĐÚNG trình tự bắt buộc từ Android 10/11: quyền vị trí NỀN
 * (ACCESS_BACKGROUND_LOCATION) PHẢI được xin RIÊNG, SAU KHI đã có quyền vị trí
 * lúc dùng app (foreground) — hệ thống không cho phép xin cả hai cùng lúc
 * trong một hộp thoại kể từ các phiên bản này.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private LocationManager locationManager;

    private final ActivityResultLauncher<String[]> foregroundLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), grants -> {
                boolean granted = Boolean.TRUE.equals(grants.get(Manifest.permission.ACCESS_FINE_LOCATION))
                        || Boolean.TRUE.equals(grants.get(Manifest.permission.ACCESS_COARSE_LOCATION));
                if (granted) {
                    startLocationUpdates();
                    binding.buttonBackground.setEnabled(true);
                } else {
                    binding.textLocation.setText(R.string.location_permission_denied);
                }
            });

    private final ActivityResultLauncher<String> backgroundLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                binding.textBackgroundStatus.setText(granted
                        ? getString(R.string.background_granted)
                        : getString(R.string.background_denied));
            });

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            binding.textLocation.setText(getString(R.string.location_format,
                    location.getLatitude(), location.getLongitude()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        locationManager = getSystemService(LocationManager.class);

        binding.buttonForeground.setOnClickListener(v -> checkForegroundThenStart());
        binding.buttonBackground.setOnClickListener(v -> checkBackgroundThenRequest());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            // Trước Android 10, KHÔNG tồn tại khái niệm quyền vị trí nền riêng —
            // có quyền foreground là mặc nhiên dùng được cả khi app chạy nền.
            binding.textBackgroundStatus.setText(R.string.background_not_needed_pre_q);
        }
    }

    private void checkForegroundThenStart() {
        boolean hasFine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        if (hasFine) {
            startLocationUpdates();
            binding.buttonBackground.setEnabled(true);
        } else {
            foregroundLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private void checkBackgroundThenRequest() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return; // đã có sẵn, không cần xin gì thêm — xem onCreate()
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            binding.textBackgroundStatus.setText(R.string.background_granted);
            return;
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            binding.textBackgroundStatus.setText(R.string.background_rationale);
        }
        // Từ Android 11 (API 30), hệ thống thường tự chuyển hướng người dùng
        // sang màn hình Settings để chọn "Allow all the time" thay vì hiện hộp
        // thoại Allow/Deny thông thường — hành vi UI cụ thể do hệ thống quyết
        // định, code gọi vẫn giống hệt cách xin quyền dangerous khác (Chương 26).
        backgroundLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
    }

    @SuppressLint("MissingPermission") // đã kiểm tra ở checkForegroundThenStart()
    private void startLocationUpdates() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 2000, 5, locationListener);
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 2000, 5, locationListener);
        } else {
            binding.textLocation.setText(R.string.location_no_provider);
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }
}
