package vn.example.ch31clean.di;

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
import vn.example.ch31clean.data.AppDatabase;
import vn.example.ch31clean.data.JsonPlaceholderApi;
import vn.example.ch31clean.data.PostDao;

/** Công thức cho mọi thứ TẦNG DATA cần nhưng không tự @Inject constructor được
 * (Room/Retrofit dựng qua Builder tĩnh) — giống hệt lý do đã có ở Chương 29. */
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
