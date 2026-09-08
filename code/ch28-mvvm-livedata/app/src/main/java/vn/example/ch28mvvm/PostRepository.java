package vn.example.ch28mvvm;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository: một tầng trung gian gộp "nguồn dữ liệu cục bộ" (Room) và "nguồn dữ
 * liệu mạng" (Retrofit) thành một API duy nhất cho UI dùng — UI không cần biết
 * dữ liệu đang tới từ đâu. Đây là bước đệm nhẹ trước khi học kiến trúc MVVM đầy
 * đủ ở Chương 28.
 */
public class PostRepository {

    public interface RefreshCallback {
        void onSuccess();
        void onError(String message);
    }

    private static final int MAX_ATTEMPTS = 2;
    private static final long RETRY_DELAY_MS = 1500;

    private final PostDao dao;
    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public PostRepository(Context context) {
        dao = AppDatabase.getInstance(context).postDao();
    }

    /** Nguồn SỰ THẬT hiển thị lên UI luôn là Room — kể cả khi mất mạng hoàn toàn,
     * đây vẫn trả về dữ liệu lần đồng bộ gần nhất (offline-first). */
    public LiveData<List<Post>> getCachedPosts() {
        return dao.getAll();
    }

    /** Kích hoạt đồng bộ với server; kết quả (nếu thành công) tự động chảy tới UI
     * qua LiveData ở trên — refresh() không cần trực tiếp trả dữ liệu ra ngoài. */
    public void refresh(RefreshCallback callback) {
        fetchWithRetry(1, callback);
    }

    private void fetchWithRetry(int attempt, RefreshCallback callback) {
        ApiClient.getApi().getPosts().enqueue(new Callback<List<Post>>() {
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
            // Retry đơn giản: chờ một khoảng cố định rồi thử lại. Chỉ hợp lý cho lỗi
            // TẠM THỜI (mất mạng thoáng qua, server timeout) — lỗi 4xx (dữ liệu/quyền
            // sai) thử lại cũng không ích gì, một hệ thống retry hoàn chỉnh hơn sẽ
            // phân biệt hai trường hợp này thay vì luôn thử lại như code mẫu này.
            mainHandler.postDelayed(() -> fetchWithRetry(attempt + 1, callback), RETRY_DELAY_MS);
        } else {
            callback.onError(message);
        }
    }
}
