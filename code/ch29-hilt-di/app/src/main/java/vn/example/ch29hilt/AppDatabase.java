package vn.example.ch29hilt;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * So với Chương 13/23: không còn getInstance()/instance static tự viết tay ở
 * đây nữa — việc "chỉ tạo đúng một instance cho cả app" giờ do Hilt đảm nhiệm
 * (annotation @Singleton trên AppModule.provideDatabase()).
 */
@Database(entities = {Post.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PostDao postDao();
}
