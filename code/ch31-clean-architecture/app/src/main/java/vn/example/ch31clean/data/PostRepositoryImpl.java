package vn.example.ch31clean.data;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.example.ch31clean.domain.Post;
import vn.example.ch31clean.domain.PostRepository;

/**
 * Cài đặt THẬT của PostRepository — biết Room, biết Retrofit, biết cách retry.
 * Tầng domain (GetPostsUseCase, RefreshPostsUseCase) chỉ cầm interface
 * PostRepository, hoàn toàn không biết class này tồn tại.
 */
@Singleton
public class PostRepositoryImpl implements PostRepository {

    private static final int MAX_ATTEMPTS = 2;
    private static final long RETRY_DELAY_MS = 1500;

    private final PostDao dao;
    private final JsonPlaceholderApi api;
    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Inject
    public PostRepositoryImpl(PostDao dao, JsonPlaceholderApi api) {
        this.dao = dao;
        this.api = api;
    }

    @Override
    public LiveData<List<Post>> getCachedPosts() {
        // Transformations.map: chuyển LiveData<List<PostEntity>> (tầng data) thành
        // LiveData<List<Post>> (tầng domain) — đây là RANH GIỚI nơi PostEntity
        // không bao giờ lộ ra ngoài PostRepositoryImpl.
        return Transformations.map(dao.getAll(), PostMapper::toDomainList);
    }

    @Override
    public void refresh(RefreshCallback callback) {
        fetchWithRetry(1, callback);
    }

    private void fetchWithRetry(int attempt, RefreshCallback callback) {
        api.getPosts().enqueue(new Callback<List<PostEntity>>() {
            @Override
            public void onResponse(Call<List<PostEntity>> call, Response<List<PostEntity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PostEntity> posts = response.body();
                    dbExecutor.execute(() -> {
                        dao.insertAll(posts);
                        mainHandler.post(callback::onSuccess);
                    });
                } else {
                    handleFailure(attempt, "HTTP " + response.code(), callback);
                }
            }

            @Override
            public void onFailure(Call<List<PostEntity>> call, Throwable t) {
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
