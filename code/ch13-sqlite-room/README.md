# Code mẫu — Chương 13: SQLite & Room

Danh sách ghi chú lưu trong SQLite thật, thao tác qua Room (`Entity` + `Dao` + `RoomDatabase`),
hiển thị bằng `RecyclerView` tự cập nhật qua `LiveData`.

## Chạy thử

1. Mở project bằng Android Studio (Room sinh code lúc build — cần Gradle sync thành công).
2. Nhập tiêu đề, bấm **Thêm** — ghi chú mới xuất hiện đầu danh sách.
3. Bấm vào một ghi chú để xoá.
4. Đóng hẳn app, mở lại — dữ liệu vẫn còn (nằm trong file SQLite thật trên thiết bị, không phải bộ nhớ tạm).

## Xem trực tiếp file database (nâng cao, không bắt buộc)

Trong Android Studio: **View → Tool Windows → App Inspection → Database Inspector** trong lúc
app đang chạy — cho phép xem trực tiếp bảng `notes` và tự chạy câu SQL để kiểm tra, rất hữu ích
khi debug logic Room.
