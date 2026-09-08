# Code mẫu — Chương 15: Jetpack DataStore

Cùng bài toán với Chương 12 (tên hiển thị + bật/tắt thông báo) nhưng lưu qua **Preferences
DataStore** thay vì `SharedPreferences`, dùng cầu nối RxJava3 chính thức vì project viết bằng Java.

## Chạy thử

1. Mở project bằng Android Studio, để Gradle sync xong, Run.
2. Nhập tên, bật/tắt switch, bấm **Lưu** — quan sát dòng trạng thái cập nhật.
3. Đóng hẳn app, mở lại — dữ liệu vẫn còn, đọc lại qua `dataStore.data()` ngay khi màn hình mở.
4. So sánh với `code/ch12-shared-preferences/` — giao diện gần như giống hệt, khác nhau ở tầng lưu trữ bên dưới.
