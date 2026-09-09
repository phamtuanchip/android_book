package vn.example.ch32provider;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract NoteDao noteDao();

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        // Double-checked locking: đảm bảo cả app chỉ mở đúng MỘT kết nối database,
        // dù nhiều Activity/thread cùng gọi getInstance() gần như đồng thời.
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "notes.db")
                            .build();
                }
            }
        }
        return instance;
    }
}
