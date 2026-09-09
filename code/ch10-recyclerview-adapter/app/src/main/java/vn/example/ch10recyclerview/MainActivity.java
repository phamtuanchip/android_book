package vn.example.ch10recyclerview;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import vn.example.ch10recyclerview.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NoteAdapter adapter;
    private final List<Note> notes = new ArrayList<>();
    private long nextId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new NoteAdapter(note ->
                Toast.makeText(this, getString(R.string.clicked_format, note.getTitle()), Toast.LENGTH_SHORT).show());

        binding.recyclerNotes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerNotes.setAdapter(adapter);

        seedInitialNotes();
        adapter.submitList(new ArrayList<>(notes));

        binding.buttonAddNote.setOnClickListener(v -> {
            long id = nextId++;
            notes.add(0, new Note(id, getString(R.string.note_title_format, id),
                    DateFormat.format("HH:mm:ss", System.currentTimeMillis()).toString()));
            // submitList() nhận danh sách MỚI, ListAdapter tự so sánh với danh sách cũ
            // bằng DIFF_CALLBACK ở NoteAdapter — không tự tay gọi notifyItemInserted().
            adapter.submitList(new ArrayList<>(notes));
        });
    }

    private void seedInitialNotes() {
        notes.add(new Note(nextId++, "Mua sách Android", "08:00:00"));
        notes.add(new Note(nextId++, "Ôn lại Chương 9", "09:15:00"));
        notes.add(new Note(nextId++, "Viết code mẫu Chương 10", "10:30:00"));
    }
}
