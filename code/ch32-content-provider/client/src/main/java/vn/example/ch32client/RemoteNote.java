package vn.example.ch32client;

/** Model RIÊNG của app client — không import gì từ app :app cả (không thể, vì
 * đây là hai APK độc lập). Chỉ cần biết tên cột theo đúng "hợp đồng" mà app kia
 * công bố (xem README) để tự map Cursor -> object của chính mình. */
public class RemoteNote {
    public final long id;
    public final String title;
    public final long createdAt;

    public RemoteNote(long id, String title, long createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }
}
