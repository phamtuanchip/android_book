package vn.example.ch31clean.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import vn.example.ch31clean.domain.GetPostsUseCase;
import vn.example.ch31clean.domain.Post;
import vn.example.ch31clean.domain.PostRepository;
import vn.example.ch31clean.domain.RefreshPostsUseCase;

/**
 * So với Chương 29: PostViewModel giờ phụ thuộc vào HAI USE CASE, không còn
 * biết PostRepository (interface) hay PostRepositoryImpl (class thật) tồn tại
 * dưới tên gì — nó chỉ biết "có một hành động GetPosts, một hành động
 * RefreshPosts". Đây chính là ranh giới presentation -> domain của Chương 31.
 */
@HiltViewModel
public class PostViewModel extends ViewModel {

    private final RefreshPostsUseCase refreshPostsUseCase;
    private final LiveData<List<Post>> posts;
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public PostViewModel(GetPostsUseCase getPostsUseCase, RefreshPostsUseCase refreshPostsUseCase) {
        this.refreshPostsUseCase = refreshPostsUseCase;
        this.posts = getPostsUseCase.execute();
        refresh();
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void refresh() {
        loading.setValue(true);
        refreshPostsUseCase.execute(new PostRepository.RefreshCallback() {
            @Override
            public void onSuccess() {
                loading.setValue(false);
                errorMessage.setValue(null);
            }

            @Override
            public void onError(String message) {
                loading.setValue(false);
                errorMessage.setValue(message);
            }
        });
    }
}
