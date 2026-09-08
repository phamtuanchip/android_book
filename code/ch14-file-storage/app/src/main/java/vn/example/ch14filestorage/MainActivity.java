package vn.example.ch14filestorage;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.example.ch14filestorage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "note.txt";

    private ActivityMainBinding binding;
    // Đọc/ghi file cũng là I/O đĩa — cùng lý do với Room ở Chương 13, không nên
    // chạy trên main thread dù API File ở đây không tự ném lỗi ép buộc như Room.
    private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonSaveInternal.setOnClickListener(v -> saveTo(internalFile(), "bộ nhớ TRONG"));
        binding.buttonSaveExternal.setOnClickListener(v -> saveTo(externalFile(), "bộ nhớ NGOÀI (riêng của app)"));
        binding.buttonReadBoth.setOnClickListener(v -> readBoth());
    }

    /** getFilesDir(): thư mục riêng của app trong bộ nhớ trong — luôn tồn tại, luôn
     * riêng tư (không app nào khác đọc được), tự động xoá khi gỡ cài đặt app. */
    private File internalFile() {
        return new File(getFilesDir(), FILE_NAME);
    }

    /** getExternalFilesDir(null): thư mục riêng của app trên bộ nhớ ngoài — KHÔNG
     * cần xin quyền (khác thư mục dùng chung như Downloads/Pictures), nhưng người
     * dùng CÓ THỂ xem được qua trình quản lý file, và cũng bị xoá khi gỡ app. */
    private File externalFile() {
        File dir = getExternalFilesDir(null);
        return dir != null ? new File(dir, FILE_NAME) : internalFile();
    }

    private void saveTo(File file, String label) {
        String content = binding.editContent.getText().toString();
        ioExecutor.execute(() -> {
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(content.getBytes(StandardCharsets.UTF_8));
                runOnUiThread(() -> Toast.makeText(this,
                        getString(R.string.saved_format, label, file.getAbsolutePath()),
                        Toast.LENGTH_LONG).show());
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(this, getString(R.string.save_failed), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void readBoth() {
        ioExecutor.execute(() -> {
            String internal = readFile(internalFile());
            String external = readFile(externalFile());
            String result = getString(R.string.read_result_format, internal, external);
            runOnUiThread(() -> binding.textResult.setText(result));
        });
    }

    private String readFile(File file) {
        if (!file.exists()) {
            return getString(R.string.file_not_found);
        }
        byte[] buffer = new byte[(int) file.length()];
        try (FileInputStream in = new FileInputStream(file)) {
            int read = in.read(buffer);
            return new String(buffer, 0, Math.max(read, 0), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return getString(R.string.read_failed);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ioExecutor.shutdown();
    }
}
