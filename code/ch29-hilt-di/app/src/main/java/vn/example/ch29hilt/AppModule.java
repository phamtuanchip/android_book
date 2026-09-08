package vn.example.ch29hilt;

import android.content.Context;

import androidx.room.Room;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * "Công thức" cho những thứ Hilt KHÔNG THỂ tự suy ra cách tạo — Room/Retrofit
 * cần gọi qua Builder tĩnh (Room.databaseBuilder, Retrofit.Builder), không có
 * constructor để tự đánh dấu @Inject như PostRepository. Mỗi @Provides là một
 * "công thức" tương ứng cho một kiểu Hilt cần cấp phát ở đâu đó trong app.
 *
 * @InstallIn(SingletonComponent.class): mọi thứ khai báo ở đây sống theo đúng
 * vòng đời TOÀN APP (giống các singleton tự viết tay ở Chương 13/22 trước đây).
 */
@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public AppDatabase provideDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "posts_cache.db").build();
    }

    @Provides
    @Singleton
    public PostDao providePostDao(AppDatabase database) {
        return database.postDao();
    }

    @Provides
    @Singleton
    public JsonPlaceholderApi provideApi() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(8, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(JsonPlaceholderApi.class);
    }
}
