package vn.example.ch31clean.domain;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * INTERFACE, không phải class — đây là điểm khác biệt cốt lõi so với Chương 29.
 * Tầng domain CHỈ khai báo "cần một nguồn dữ liệu Post có khả năng làm 2 việc
 * này", không quan tâm việc triển khai thật (Room? Retrofit? file? tất cả?)
 * nằm ở đâu — đó là việc của PostRepositoryImpl trong tầng data.
 */
public interface PostRepository {

    interface RefreshCallback {
        void onSuccess();
        void onError(String message);
    }

    LiveData<List<Post>> getCachedPosts();

    void refresh(RefreshCallback callback);
}
