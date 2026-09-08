package vn.example.ch29hilt;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * So với Chương 23/28: constructor giờ nhận thẳng PostDao và JsonPlaceholderApi
 * ĐÃ ĐƯỢC KHỞI TẠO SẴN — không còn tự gọi AppDatabase.getInstance(context) hay
 * ApiClient.getApi() bên trong nữa. @Inject đánh dấu "Hilt ơi, tự tạo giúp tôi
 * một PostRepository, biết lấy PostDao/JsonPlaceholderApi ở đâu từ AppModule".
 */
@Singleton
public class PostRepository {

    public interface RefreshCallback {
        void onSuccess();
        void onError(String message);
    }

    private static final int MAX_ATTEMPTS = 2;
    private static final long RETRY_DELAY_MS = 1500;

    private final PostDao dao;
    private final JsonPlaceholderApi api;
    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Inject
    public PostRepository(PostDao dao, JsonPlaceholderApi api) {
        this.dao = dao;
        this.api = api;
    }

    public LiveData<List<Post>> getCachedPosts() {
        return dao.getAll();
    }

    public void refresh(RefreshCallback callback) {
        fetchWithRetry(1, callback);
    }

    private void fetchWithRetry(int attempt, RefreshCallback callback) {
        api.getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Post> posts = response.body();
                    dbExecutor.execute(() -> {
                        dao.insertAll(posts);
                        mainHandler.post(callback::onSuccess);
                    });
                } else {
                    handleFailure(attempt, "HTTP " + response.code(), callback);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                handleFailure(attempt, t.getMessage(), callback);
            }
        });
    }

    private void handleFailure(int attempt, String message, RefreshCallback callback) {
        if (attempt < MAX_ATTEMPTS) {
            mainHandler.postDelayed(() -> fetchWithRetry(attempt + 1, callback), RETRY_DELAY_MS);
        } else {
            callback.onError(message);
        }
    }
}
