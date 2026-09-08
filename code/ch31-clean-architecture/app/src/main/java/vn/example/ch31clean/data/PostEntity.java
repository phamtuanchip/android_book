package vn.example.ch31clean.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Model của TẦNG DATA — mang annotation của cả Room (@Entity) lẫn Gson
 * (@SerializedName) vì nó đóng vai trò vừa là bảng cache vừa là DTO nhận JSON
 * (đơn giản hoá như Chương 23/29). Khác biệt quan trọng với Chương 29: giờ
 * đây KHÔNG PHẢI model duy nhất trong app — domain.Post (thuần, không
 * annotation) mới là model tầng trên nhìn thấy, PostMapper nối hai bên.
 */
@Entity(tableName = "posts")
public class PostEntity {

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
