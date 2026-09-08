package vn.example.ch26permission;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.example.ch26permission.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    readContactsCount();
                } else {
                    handleDenied();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonReadContacts.setOnClickListener(v -> checkAndReadContacts());
        binding.buttonOpenSettings.setOnClickListener(v -> openAppSettings());
    }

    private void checkAndReadContacts() {
        binding.buttonOpenSettings.setVisibility(View.GONE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            readContactsCount();
            return;
        }

        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            // Người dùng đã từ chối ÍT NHẤT MỘT LẦN trước đó, nhưng chưa chọn "Không
            // hỏi lại" — đây là lúc NÊN giải thích rõ vì sao cần quyền này TRƯỚC khi
            // xin lại, thay vì xin thẳng lần nữa (dễ bị từ chối tiếp vì người dùng
            // không hiểu lý do).
            binding.textResult.setText(R.string.rationale_contacts);
        }

        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS);
    }

    private void readContactsCount() {
        binding.textResult.setText(R.string.loading);
        executor.execute(() -> {
            int count = 0;
            try (Cursor cursor = getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI,
                    new String[]{ContactsContract.Contacts._ID},
                    null, null, null)) {
                if (cursor != null) {
                    count = cursor.getCount();
                }
            }
            int finalCount = count;
            runOnUiThread(() -> binding.textResult.setText(
                    getString(R.string.contacts_count_format, finalCount)));
        });
    }

    private void handleDenied() {
        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            // Từ chối NHƯNG shouldShowRequestPermissionRationale() giờ trả về false
            // -> dấu hiệu người dùng đã chọn "Không hỏi lại" (hoặc đây là lần từ chối
            // đầu tiên trên một số phiên bản OEM) — gọi lại requestPermissionLauncher
            // từ giờ sẽ KHÔNG hiện hộp thoại nào nữa, chỉ tự động trả về false ngay.
            // Cách duy nhất còn lại là hướng dẫn người dùng tự bật trong Settings.
            binding.textResult.setText(R.string.permission_permanently_denied);
            binding.buttonOpenSettings.setVisibility(View.VISIBLE);
        } else {
            binding.textResult.setText(R.string.permission_denied_once);
        }
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
