package vn.example.ch27security;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Chỉ khai báo INTERFACE — không viết thân hàm nào cả. Retrofit đọc annotation
 * lúc runtime, tự sinh code thật gửi request + parse response, giống tinh thần
 * @Dao của Room ở Chương 13.
 */
public interface JsonPlaceholderApi {

    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("posts/{id}")
    Call<Post> getPost(@Path("id") int id);
}
