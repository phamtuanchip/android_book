package vn.example.ch31clean.domain;

import androidx.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

/**
 * Use case: một "hành động nghiệp vụ" cụ thể, đặt tên bằng ĐỘNG TỪ mô tả đúng
 * ý định (GetPosts, RefreshPosts) — không phải chỉ là lớp bọc rỗng quanh
 * Repository. Với logic đơn giản như ở đây, use case gần như chỉ chuyển tiếp
 * lời gọi; giá trị thật sự lộ rõ khi nghiệp vụ phức tạp hơn (ví dụ: lọc bài
 * viết theo quyền người dùng, ghép dữ liệu từ hai repository khác nhau...).
 */
public class GetPostsUseCase {

    private final PostRepository repository;

    @Inject
    public GetPostsUseCase(PostRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Post>> execute() {
        return repository.getCachedPosts();
    }
}
