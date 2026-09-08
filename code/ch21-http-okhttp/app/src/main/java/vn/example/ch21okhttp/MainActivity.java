package vn.example.ch21okhttp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import vn.example.ch21okhttp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String POSTS_URL = "https://jsonplaceholder.typicode.com/posts";

    private ActivityMainBinding binding;
    private PostAdapter adapter;

    // Một OkHttpClient nên được TÁI SỬ DỤNG cho cả app (giữ connection pool, cache
    // nội bộ...) — không tạo mới cho mỗi request. Ở quy mô lớn hơn, đặt nó vào một
    // singleton dùng chung, tương tự AppDatabase ở Chương 13.
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

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

        Request request = new Request.Builder()
                .url(POSTS_URL)
                .get()
                .build();

        // enqueue(): gửi request BẤT ĐỒNG BỘ — OkHttp tự chạy nó trên thread nền
        // riêng (dispatcher pool), callback onResponse/onFailure cũng chạy trên
        // THREAD NỀN đó, KHÔNG PHẢI main thread — bắt buộc phải runOnUiThread()
        // trước khi đụng tới bất kỳ View nào, khác Retrofit ở Chương 22.
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> showError(getString(R.string.error_network, e.getMessage())));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> showError(getString(R.string.error_http, response.code())));
                    return;
                }
                String json = response.body() != null ? response.body().string() : "[]";
                try {
                    List<Post> posts = parsePosts(json);
                    runOnUiThread(() -> {
                        binding.progressBar.setVisibility(View.GONE);
                        adapter.submitList(posts);
                    });
                } catch (JSONException e) {
                    runOnUiThread(() -> showError(getString(R.string.error_parse, e.getMessage())));
                }
            }
        });
    }

    /** Map tay từng field JSON -> Post — xem Chương 22 để thấy Gson tự động hoá việc này. */
    private List<Post> parsePosts(String json) throws JSONException {
        List<Post> posts = new ArrayList<>();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            posts.add(new Post(
                    obj.getInt("id"),
                    obj.getInt("userId"),
                    obj.getString("title"),
                    obj.getString("body")));
        }
        return posts;
    }

    private void showError(String message) {
        binding.progressBar.setVisibility(View.GONE);
        binding.textError.setVisibility(View.VISIBLE);
        binding.textError.setText(message);
    }
}
