package vn.example.ch38release;

import com.google.gson.annotations.SerializedName;

/**
 * Gson map TỰ ĐỘNG field JSON -> field Java cùng tên. Khi tên khác nhau (ở đây
 * JSON dùng "userId" nhưng ta muốn field Java rõ nghĩa hơn là "authorId"),
 * @SerializedName chỉ định rõ ràng thay vì đổi tên field Java để "khớp" JSON.
 */
public class Post {
    public int id;

    @SerializedName("userId")
    public int authorId;

    public String title;
    public String body;
}
