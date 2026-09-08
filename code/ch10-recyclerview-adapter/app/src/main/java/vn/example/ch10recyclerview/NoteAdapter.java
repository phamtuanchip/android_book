package vn.example.ch10recyclerview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import vn.example.ch10recyclerview.databinding.ItemNoteBinding;

/**
 * ListAdapter + DiffUtil tự tính toán item nào thêm/xoá/đổi vị trí giữa 2 danh
 * sách, chỉ báo RecyclerView cập nhật đúng phần đó (có animation mượt) — thay vì
 * notifyDataSetChanged() vẽ lại toàn bộ danh sách mỗi lần có thay đổi nhỏ.
 */
public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteViewHolder> {

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    private final OnNoteClickListener onNoteClickListener;

    protected NoteAdapter(OnNoteClickListener onNoteClickListener) {
        super(DIFF_CALLBACK);
        this.onNoteClickListener = onNoteClickListener;
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle())
                    && oldItem.getCreatedAt().equals(newItem.getCreatedAt());
        }
    };

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNoteBinding binding = ItemNoteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new NoteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(getItem(position), onNoteClickListener);
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        private final ItemNoteBinding binding;

        NoteViewHolder(ItemNoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Note note, OnNoteClickListener listener) {
            binding.textTitle.setText(note.getTitle());
            binding.textTimestamp.setText(note.getCreatedAt());
            binding.getRoot().setOnClickListener(v -> listener.onNoteClick(note));
        }
    }
}
