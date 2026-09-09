package vn.example.ch32provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * ContentProvider là "cửa hải quan" — cách DUY NHẤT một app khác được phép chạm
 * vào dữ liệu SQLite của app này (nhắc lại sandbox từ Chương 1/26: file .db nằm
 * trong getFilesDir() của app khác vốn KHÔNG đọc được trực tiếp). Mọi thao tác
 * đều đi qua các hàm chuẩn hoá dưới đây, không phải truy cập file trực tiếp.
 */
public class NoteProvider extends ContentProvider {

    private static final int NOTES = 1;
    private static final int NOTE_ID = 2;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        MATCHER.addURI(NoteContract.AUTHORITY, "notes", NOTES);
        MATCHER.addURI(NoteContract.AUTHORITY, "notes/#", NOTE_ID);
    }

    @Override
    public boolean onCreate() {
        // Chạy RẤT SỚM trong vòng đời tiến trình — thậm chí trước cả
        // Application.onCreate() (Chương 6/20) nếu Provider cần khởi tạo trước.
        // Không làm việc nặng ở đây; AppDatabase.getInstance() chỉ mở kết nối,
        // Room hoãn thực sự tạo bảng tới lần truy vấn đầu tiên.
        return true;
    }

    private SupportSQLiteDatabase writableDb() {
        return AppDatabase.getInstance(getContext()).getOpenHelper().getWritableDatabase();
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                         @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (MATCHER.match(uri)) {
            case NOTES:
                cursor = writableDb().query(new SimpleSQLiteQuery(
                        "SELECT id, title, created_at FROM notes ORDER BY id DESC"));
                break;
            case NOTE_ID:
                long id = ContentUris.parseId(uri);
                cursor = writableDb().query(new SimpleSQLiteQuery(
                        "SELECT id, title, created_at FROM notes WHERE id = ?", new Object[]{id}));
                break;
            default:
                throw new IllegalArgumentException("URI không được hỗ trợ: " + uri);
        }
        // Client gọi registerContentObserver(uri, ...) sẽ được báo mỗi khi
        // insert()/delete() bên dưới gọi notifyChange() với cùng uri gốc này.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (MATCHER.match(uri) != NOTES) {
            throw new IllegalArgumentException("Chỉ hỗ trợ insert vào " + NoteContract.CONTENT_URI);
        }
        long id = writableDb().insert("notes", SQLiteDatabase.CONFLICT_REPLACE, values);
        Uri result = ContentUris.withAppendedId(NoteContract.CONTENT_URI, id);
        getContext().getContentResolver().notifyChange(result, null);
        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = writableDb().delete("notes", selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                       @Nullable String[] selectionArgs) {
        // Bỏ qua update() để giữ code mẫu gọn — một Provider thật cần cài đặt
        // đầy đủ cả 4 thao tác CRUD.
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case NOTES:
                return "vnd.android.cursor.dir/vnd." + NoteContract.AUTHORITY + ".notes";
            case NOTE_ID:
                return "vnd.android.cursor.item/vnd." + NoteContract.AUTHORITY + ".notes";
            default:
                throw new IllegalArgumentException("URI không được hỗ trợ: " + uri);
        }
    }
}
