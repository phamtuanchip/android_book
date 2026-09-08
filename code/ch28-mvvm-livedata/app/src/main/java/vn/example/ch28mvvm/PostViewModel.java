package vn.example.ch28mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

/**
 * ViewModel KHÔNG giữ tham chiếu Activity/View nào cả (khác hẳn Chương 6 —
 * nguyên nhân memory leak khi giữ Context sai cách) — chỉ giữ dữ liệu và logic
 * điều phối. AndroidViewModel là biến thể ViewModel có sẵn Application Context,
 * dùng khi (như ở đây) cần Context để khởi tạo Repository/Room.
 */
public class PostViewModel extends AndroidViewModel {

    private final PostRepository repository;
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public PostViewModel(@NonNull Application application) {
        super(application);
        repository = new PostRepository(application);
        refresh(); // tải lần đầu ngay khi ViewModel được tạo
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
