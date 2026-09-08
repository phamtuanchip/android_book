package vn.example.ch13room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @Entity biến class này thành định nghĩa một bảng SQLite tên "notes" — Room sinh
 * ra SQL CREATE TABLE tương ứng lúc build, bạn không tự viết SQL DDL bằng tay.
 */
@Entity(tableName = "notes")
public class Note {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "created_at")
    public long createdAt;

    public Note(@NonNull String title, long createdAt) {
        this.title = title;
        this.createdAt = createdAt;
    }
}
