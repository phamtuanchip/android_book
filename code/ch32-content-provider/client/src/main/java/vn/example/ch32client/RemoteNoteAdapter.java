package vn.example.ch32client;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.example.ch32client.databinding.ItemNoteBinding;

public class RemoteNoteAdapter extends RecyclerView.Adapter<RemoteNoteAdapter.ViewHolder> {

    private final List<RemoteNote> notes = new ArrayList<>();

    public void submitList(List<RemoteNote> newNotes) {
        notes.clear();
        notes.addAll(newNotes);
        notifyDataSetChanged(); // danh sách nhỏ, đơn giản hoá — Chương 10 đã bàn ListAdapter/DiffUtil
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNoteBinding binding = ItemNoteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemNoteBinding binding;

        ViewHolder(ItemNoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(RemoteNote note) {
            binding.textTitle.setText(note.title);
            binding.textCreatedAt.setText(
                    DateFormat.format("HH:mm:ss dd/MM/yyyy", note.createdAt));
        }
    }
}
