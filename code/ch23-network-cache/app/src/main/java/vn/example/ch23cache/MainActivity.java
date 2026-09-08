package vn.example.ch23cache;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import vn.example.ch23cache.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PostRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new PostRepository(this);

        PostAdapter adapter = new PostAdapter();
        binding.recyclerPosts.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerPosts.setAdapter(adapter);

        // Hiển thị NGAY dữ liệu cache cục bộ — kể cả trước khi biết mạng có hoạt
        // động hay không. Đây chính là tinh thần "offline-first": màn hình không
        // bao giờ trống trơn chỉ vì đang chờ mạng hoặc mạng vừa mất.
        repository.getCachedPosts().observe(this, adapter::submitList);

        binding.buttonLoad.setOnClickListener(v -> refresh());

        // Tự làm mới một lần khi mở app — nếu thất bại, dữ liệu cache (nếu có từ
        // lần chạy trước) vẫn hiển thị bình thường nhờ dòng observe() ở trên.
        refresh();
    }

    private void refresh() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.textError.setVisibility(View.GONE);

        repository.refresh(new PostRepository.RefreshCallback() {
            @Override
            public void onSuccess() {
                binding.progressBar.setVisibility(View.GONE);
                // Không cần tự cập nhật adapter ở đây — dao.insertAll() đã kích hoạt
                // LiveData ở trên tự phát dữ liệu mới, giống hệt cơ chế Chương 13.
            }

            @Override
            public void onError(String message) {
                binding.progressBar.setVisibility(View.GONE);
                binding.textError.setVisibility(View.VISIBLE);
                // CHỦ Ý không xoá danh sách đang hiển thị khi có lỗi — người dùng vẫn
                // xem được dữ liệu cũ, chỉ được báo rằng nó có thể không mới nhất.
                binding.textError.setText(getString(R.string.error_refresh_kept_cache, message));
            }
        });
    }
}
