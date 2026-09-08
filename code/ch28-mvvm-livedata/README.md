# Code mẫu — Chương 28: MVVM, ViewModel & LiveData

Refactor lại app offline-first ở Chương 23: chuyển toàn bộ trạng thái (danh sách, loading, lỗi)
và logic điều phối vào `PostViewModel`, `MainActivity` chỉ còn hiển thị.

## Chạy thử

1. Mở project bằng Android Studio, Run.
2. Ngay khi mở app, `PostViewModel` tự gọi `refresh()` một lần — quan sát ProgressBar chạy.
3. **Xoay màn hình** trong lúc ProgressBar đang chạy (nếu auto-rotate bật) — quan sát:
   - ProgressBar KHÔNG bị reset về ẩn rồi lại hiện — trạng thái loading vẫn đúng liên tục.
   - Không có lệnh gọi mạng THỨ HAI nào được bắn ra chỉ vì Activity bị tạo lại (kiểm tra qua
     Logcat/Profiler Network nếu muốn chắc chắn).
4. So sánh `MainActivity.java` ở đây với bản Chương 23 — quan sát Activity ngắn gọn hơn hẳn vì
   không còn tự cầm `PostRepository`/callback nào cả.
