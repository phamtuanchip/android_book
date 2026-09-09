package vn.example.ch38release;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** Singleton — cùng lý do với AppDatabase (Chương 13) và OkHttpClient (Chương 21):
 * chỉ nên có một instance Retrofit cho cả app. */
public final class ApiClient {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    private static volatile JsonPlaceholderApi api;

    public static JsonPlaceholderApi getApi() {
        if (api == null) {
            synchronized (ApiClient.class) {
                if (api == null) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    api = retrofit.create(JsonPlaceholderApi.class);
                }
            }
        }
        return api;
    }

    private ApiClient() {
    }
}
