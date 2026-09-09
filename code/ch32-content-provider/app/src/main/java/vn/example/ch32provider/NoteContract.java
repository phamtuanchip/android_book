package vn.example.ch32provider;

import android.net.Uri;

/**
 * "Hợp đồng" công khai của ContentProvider — authority, URI, tên cột. Đây là
 * phần DUY NHẤT app khác (như module :client trong cùng project này, đóng vai
 * một app HOÀN TOÀN riêng biệt) cần biết để đọc/ghi dữ liệu qua Provider, mà
 * không cần và không được phép biết Room/SQLite chạy bên dưới.
 */
public final class NoteContract {

    public static final String AUTHORITY = "vn.example.ch32provider.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/notes");

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CREATED_AT = "created_at";

    private NoteContract() {
    }
}
