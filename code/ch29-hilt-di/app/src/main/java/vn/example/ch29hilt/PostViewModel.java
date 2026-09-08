package vn.example.ch29hilt;

import dagger.hilt.android.lifecycle.HiltViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * So với Chương 28: không còn extends AndroidViewModel/không cần Application
 * nữa — PostRepository giờ được Hilt TỰ ĐỘNG lắp ráp đầy đủ (PostDao,
 * JsonPlaceholderApi bên trong nó cũng do Hilt cấp) và truyền thẳng vào đây qua
 * constructor. PostViewModel không cần biết Room hay Retrofit tồn tại.
 *
 * @HiltViewModel + @Inject constructor là cú pháp HIỆN ĐẠI — annotation cũ hơn
 * @ViewModelInject (từng phổ biến trong tài liệu Hilt đời đầu) đã bị loại bỏ.
 */
@HiltViewModel
public class PostViewModel extends ViewModel {

    private final PostRepository repository;
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public PostViewModel(PostRepository repository) {
        this.repository = repository;
        refresh();
    }

    public LiveData<List<Post>> getPosts() {
        return repository.getCachedPosts();
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void refresh() {
        loading.setValue(true);
        repository.refresh(new PostRepository.RefreshCallback() {
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
