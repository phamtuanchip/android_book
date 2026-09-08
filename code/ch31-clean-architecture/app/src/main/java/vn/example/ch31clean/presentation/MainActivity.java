package vn.example.ch31clean.presentation;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import dagger.hilt.android.AndroidEntryPoint;
import vn.example.ch31clean.databinding.ActivityMainBinding;

/**
 * Không đổi gì nhiều so với Chương 29/28 ở tầng này — điểm khác biệt của
 * Chương 31 nằm ở CÁCH TỔ CHỨC các lớp bên dưới (domain/data/di), không phải
 * ở cách Activity dùng ViewModel.
 */
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PostViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(PostViewModel.class);

        PostAdapter adapter = new PostAdapter();
        binding.recyclerPosts.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerPosts.setAdapter(adapter);

        viewModel.getPosts().observe(this, adapter::submitList);

        viewModel.getLoading().observe(this, isLoading ->
                binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        viewModel.getErrorMessage().observe(this, message -> {
            if (message == null) {
                binding.textError.setVisibility(View.GONE);
            } else {
                binding.textError.setVisibility(View.VISIBLE);
                binding.textError.setText(getString(R.string.error_refresh_kept_cache, message));
            }
        });

        binding.buttonLoad.setOnClickListener(v -> viewModel.refresh());
    }
}
