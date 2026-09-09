package vn.example.ch33share;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import vn.example.ch33share.databinding.ActivityNoteDetailBinding;

/**
 * Mở được qua bất kỳ cách nào tạo ra đúng Intent ACTION_VIEW với URI khớp
 * <intent-filter> khai báo trong manifest — không nhất thiết từ trong app này:
 * một trang web, một tin nhắn, hay một app hoàn toàn khác cũng gọi được, miễn
 * đúng scheme/host.
 */
public class NoteDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNoteDetailBinding binding = ActivityNoteDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Uri uri = getIntent().getData();
        if (uri != null) {
            // ch33share://note/42 -> lastPathSegment = "42"
            String noteId = uri.getLastPathSegment();
            binding.textNoteId.setText(getString(R.string.note_id_format, noteId));
            binding.textSourceUri.setText(getString(R.string.source_uri_format, uri.toString()));
        } else {
            binding.textNoteId.setText(R.string.no_uri_data);
        }
    }
}
