package vn.example.ch32provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {

    // LiveData<List<Note>>: Room TỰ ĐỘNG phát giá trị mới mỗi khi bảng "notes" thay
    // đổi (insert/delete/update) — không cần tự gọi lại truy vấn thủ công.
    @Query("SELECT * FROM notes ORDER BY id DESC")
    LiveData<List<Note>> getAll();

    @Insert
    void insert(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT COUNT(*) FROM notes")
    int count();
}
