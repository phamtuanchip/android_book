package vn.example.ch31clean.presentation;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import vn.example.ch31clean.databinding.ItemPostBinding;
import vn.example.ch31clean.domain.Post;

/** Adapter (tầng presentation) chỉ biết domain.Post — không import gì từ
 * package data cả, đúng nguyên tắc "phụ thuộc chỉ hướng vào trong" của Chương 31. */
public class PostAdapter extends ListAdapter<Post, PostAdapter.PostViewHolder> {

    public PostAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Post> DIFF_CALLBACK = new DiffUtil.ItemCallback<Post>() {
        @Override
        public boolean areItemsTheSame(@NonNull Post oldItem, @NonNull Post newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Post oldItem, @NonNull Post newItem) {
            return oldItem.title.equals(newItem.title) && oldItem.body.equals(newItem.body);
        }
    };

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding binding = ItemPostBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PostViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        private final ItemPostBinding binding;

        PostViewHolder(ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Post post) {
            binding.textTitle.setText(post.title);
            binding.textBody.setText(post.body);
            binding.textUserId.setText("authorId: " + post.authorId);
        }
    }
}
