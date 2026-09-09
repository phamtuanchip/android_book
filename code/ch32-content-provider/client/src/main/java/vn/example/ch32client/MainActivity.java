package vn.example.ch32client;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.example.ch32client.databinding.ActivityMainBinding;

/**
 * App HOÀN TOÀN riêng biệt với app :app (package vn.example.ch32provider) —
 * không share code, không share process. Toàn bộ tương tác với dữ liệu ghi chú
 * của app kia CHỈ đi qua content:// URI dưới đây, đúng như một app thứ ba bất kỳ
 * trên máy người dùng cũng có thể làm nếu biết URI này (xem thảo luận bảo mật
 * trong Chương 32).
 */
public class MainActivity extends AppCompatActivity {

    // Phải khớp CHÍNH XÁC với NoteContract.AUTHORITY khai báo bên app :app —
    // đây là "địa chỉ" duy nhất kết nối hai app, không có cách nào IDE tự kiểm
    // tra giúp bạn gõ đúng vì hai project không biết gì về nhau.
    private static final Uri NOTES_URI = Uri.parse("content://vn.example.ch32provider.provider/notes");

    private ActivityMainBinding binding;
    private RemoteNoteAdapter adapter;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final ContentObserver observer = new ContentObserver(mainHandler) {
        @Override
        public void onChange(boolean selfChange) {
            // Được gọi tự động mỗi khi NoteProvider.insert()/delete() bên app kia
            // gọi notifyChange() — không cần tự bấm "Tải lại" thủ công.
            queryNotes();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new RemoteNoteAdapter();
        binding.recyclerNotes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerNotes.setAdapter(adapter);

        binding.buttonQuery.setOnClickListener(v -> queryNotes());
        binding.buttonInsert.setOnClickListener(v -> insertNote());
    }

    @Override
    protected void onStart() {
        super.onStart();
        getContentResolver().registerContentObserver(NOTES_URI, true, observer);
        queryNotes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getContentResolver().unregisterContentObserver(observer);
    }

    private void queryNotes() {
        executor.execute(() -> {
            List<RemoteNote> notes = new ArrayList<>();
            String error = null;
            try (Cursor cursor = getContentResolver().query(
                    NOTES_URI, null, null, null, null)) {
                if (cursor != null) {
                    int idxId = cursor.getColumnIndexOrThrow("id");
                    int idxTitle = cursor.getColumnIndexOrThrow("title");
                    int idxCreatedAt = cursor.getColumnIndexOrThrow("created_at");
                    while (cursor.moveToNext()) {
                        notes.add(new RemoteNote(
                                cursor.getLong(idxId),
                                cursor.getString(idxTitle),
                                cursor.getLong(idxCreatedAt)));
                    }
                }
            } catch (Exception e) {
                // SecurityException nếu Provider có readPermission và app này chưa
                // xin/được cấp; IllegalArgumentException nếu app :app CHƯA ĐƯỢC CÀI
                // (Provider không tồn tại trên máy) — cả hai đều rơi vào đây.
                error = e.getMessage();
            }
            List<RemoteNote> finalNotes = notes;
            String finalError = error;
            mainHandler.post(() -> {
                if (finalError != null) {
                    binding.textNote.setText(getString(R.string.error_format, finalError));
                } else {
                    binding.textNote.setText(getString(R.string.note_count_format, finalNotes.size()));
                    adapter.submitList(finalNotes);
                }
            });
        });
    }

    private void insertNote() {
        String time = DateFormat.format("HH:mm:ss", System.currentTimeMillis()).toString();
        ContentValues values = new ContentValues();
        values.put("title", getString(R.string.note_from_client_format, time));
        values.put("created_at", System.currentTimeMillis());

        executor.execute(() -> {
            try {
                getContentResolver().insert(NOTES_URI, values);
                mainHandler.post(() ->
                        Toast.makeText(this, R.string.inserted_toast, Toast.LENGTH_SHORT).show());
                // Không cần tự gọi queryNotes() ở đây — ContentObserver phía trên
                // sẽ tự kích hoạt vì NoteProvider.insert() đã gọi notifyChange().
            } catch (Exception e) {
                mainHandler.post(() ->
                        binding.textNote.setText(getString(R.string.error_format, e.getMessage())));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
