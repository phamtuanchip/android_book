package vn.example.ch32provider;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.example.ch32provider.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppDatabase database;
    // Room CẤM gọi truy vấn trên main thread mặc định (ném IllegalStateException) vì
    // I/O đĩa có thể mất hàng chục/hàng trăm ms — đủ để làm UI giật (ANR nếu tệ hơn).
    // Executor riêng để chạy insert/delete ở background; Chương 19 sẽ nói kỹ hơn về
    // các lựa chọn xử lý bất đồng bộ trong Android.
    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = AppDatabase.getInstance(this);

        NoteAdapter adapter = new NoteAdapter(note -> {
            dbExecutor.execute(() -> database.noteDao().delete(note));
            Toast.makeText(this, getString(R.string.deleted_format, note.title), Toast.LENGTH_SHORT).show();
        });

        binding.recyclerNotes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerNotes.setAdapter(adapter);

        // getAll() trả LiveData — observe() tự cập nhật lại danh sách MỖI KHI bảng
        // "notes" thay đổi (do chính app này hoặc insert/delete ở bất kỳ đâu khác),
        // không cần tự gọi lại truy vấn hay tự quản lý callback thủ công.
        database.noteDao().getAll().observe(this, adapter::submitList);

        binding.buttonAdd.setOnClickListener(v -> {
            String title = binding.editTitle.getText().toString().trim();
            if (title.isEmpty()) {
                return;
            }
            Note note = new Note(title, System.currentTimeMillis());
            dbExecutor.execute(() -> database.noteDao().insert(note));
            binding.editTitle.setText("");
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbExecutor.shutdown();
    }
}
