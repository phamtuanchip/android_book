package vn.example.ch31clean.domain;

/**
 * Model của TẦNG DOMAIN — hoàn toàn không có annotation @Entity (Room) hay
 * @SerializedName (Gson). Tầng domain không biết, và không cần biết, dữ liệu
 * đang được lưu bằng SQLite hay lấy về bằng JSON — đó là chi tiết của tầng data.
 */
public class Post {
    public final int id;
    public final int authorId;
    public final String title;
    public final String body;

    public Post(int id, int authorId, String title, String body) {
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.body = body;
    }
}
