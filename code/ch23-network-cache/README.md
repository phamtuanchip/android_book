# Code mẫu — Chương 23: Xử lý lỗi mạng, retry, cache offline-first

Kết hợp Room (Chương 13) + Retrofit (Chương 22) qua một `PostRepository`: danh sách luôn hiển
thị từ cache cục bộ trước, đồng bộ mạng chạy nền và tự cập nhật lại UI khi xong — kèm retry đơn
giản khi gặp lỗi tạm thời.

## Chạy thử

1. Mở project bằng Android Studio, Run lần đầu (có mạng) — danh sách tải về và được lưu cache.
2. Đóng hẳn app, **bật chế độ máy bay**, mở lại app — danh sách vẫn hiển thị đầy đủ (đọc từ Room,
   không cần mạng), kèm thông báo lỗi đồng bộ nhỏ phía trên chứ không xoá mất danh sách.
3. Tắt chế độ máy bay, bấm **Làm mới** — đồng bộ lại thành công, thông báo lỗi biến mất.
4. (Tuỳ chọn) Đặt breakpoint hoặc log trong `PostRepository.handleFailure()` để quan sát cơ chế
   thử lại một lần trước khi báo lỗi hẳn.
