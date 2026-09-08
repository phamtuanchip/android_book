package vn.example.ch23cache;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PostDao {

    @Query("SELECT * FROM posts ORDER BY id")
    LiveData<List<Post>> getAll();

    // REPLACE theo primary key (id): bài viết đã có sẽ được cập nhật đè, bài viết
    // mới sẽ được thêm — một kiểu "upsert" đơn giản phù hợp cho cache làm mới toàn bộ.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Post> posts);
}
