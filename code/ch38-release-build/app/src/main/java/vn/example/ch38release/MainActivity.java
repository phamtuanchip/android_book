package vn.example.ch38release;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.example.ch38release.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new PostAdapter();
        binding.recyclerPosts.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerPosts.setAdapter(adapter);

        binding.buttonLoad.setOnClickListener(v -> loadPosts());
    }

    private void loadPosts() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.textError.setVisibility(View.GONE);

        // So sánh với Chương 21: không còn OkHttpClient/Request/JSONArray thủ công
        // nào ở đây cả — chỉ còn đúng MỘT lời gọi hàm, giống gọi API Java bình thường.
        ApiClient.getApi().getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                // Callback của Retrofit trên Android LUÔN chạy trên MAIN THREAD —
                // khác hẳn OkHttp thuần ở Chương 21, KHÔNG cần runOnUiThread() nữa.
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.submitList(response.body());
                } else {
                    showError(getString(R.string.error_http, response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                showError(getString(R.string.error_network, t.getMessage()));
            }
        });
    }

    private void showError(String message) {
        binding.textError.setVisibility(View.VISIBLE);
        binding.textError.setText(message);
    }
}
