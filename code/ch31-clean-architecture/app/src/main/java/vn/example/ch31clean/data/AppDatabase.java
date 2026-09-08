package vn.example.ch31clean.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PostEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PostDao postDao();
}
