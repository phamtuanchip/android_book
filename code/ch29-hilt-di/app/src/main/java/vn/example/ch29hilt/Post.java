package vn.example.ch29hilt;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Cùng một class vừa là model JSON (Gson, Chương 22) vừa là bảng Room (Chương 13)
 * — cách đơn giản hoá có chủ đích cho bài học này. Trong project thực tế lớn hơn,
 * thường tách riêng "PostDto" (khớp JSON) và "PostEntity" (khớp bảng) cùng một
 * hàm mapper ở giữa, để hai tầng không phụ thuộc cứng vào nhau (liên quan tới tư
 * duy Clean Architecture ở Chương 31) — bỏ qua ở đây để tập trung vào chủ đề chính.
 */
@Entity(tableName = "posts")
public class Post {

    @PrimaryKey
    public int id;

    @SerializedName("userId")
    @ColumnInfo(name = "author_id")
    public int authorId;

    @NonNull
    public String title = "";

    @NonNull
    public String body = "";
}
