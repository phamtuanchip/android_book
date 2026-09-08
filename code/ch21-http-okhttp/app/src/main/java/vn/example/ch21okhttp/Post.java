package vn.example.ch21okhttp;

/** Model thuần Java — tự map tay từ JSON ở chương này (Chương 22 sẽ tự động hoá bằng Gson). */
public class Post {
    public final int id;
    public final int userId;
    public final String title;
    public final String body;

    public Post(int id, int userId, String title, String body) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.body = body;
    }
}
