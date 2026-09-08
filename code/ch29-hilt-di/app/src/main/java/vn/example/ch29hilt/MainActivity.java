package vn.example.ch29hilt;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import dagger.hilt.android.AndroidEntryPoint;
import vn.example.ch29hilt.databinding.ActivityMainBinding;

/**
 * @AndroidEntryPoint: đánh dấu để Hilt "chèn" một ViewModelProvider.Factory
 * biết cách tự lắp ráp PostViewModel (và mọi @Inject khác) vào Activity này.
 * Thiếu annotation này, `new ViewModelProvider(this).get(PostViewModel.class)`
 * bên dưới sẽ ném lỗi vì không tìm được factory phù hợp cho constructor có
 * tham số của PostViewModel.
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

        // ViewModelProvider trả về CÙNG MỘT instance PostViewModel nếu Activity bị
        // huỷ/tạo lại do configuration change (xoay màn hình, Chương 6) — request
        // mạng đang chạy dở KHÔNG bị huỷ theo, không bị gọi lại từ đầu một cách
        // lãng phí như khi tự quản lý trong Activity ở Chương 23.
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
