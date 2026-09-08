# Code mẫu — Chương 21: Gọi HTTP với OkHttp

Gọi API công khai [jsonplaceholder.typicode.com](https://jsonplaceholder.typicode.com/posts)
bằng OkHttp thuần, tự parse JSON bằng `org.json` (không dùng thư viện map tự động — đó là nội
dung Chương 22).

## Chạy thử

1. Mở project bằng Android Studio, Run (cần máy ảo/thiết bị có kết nối mạng).
2. Bấm **Tải danh sách bài viết** — quan sát ProgressBar rồi danh sách hiện ra.
3. Bật chế độ máy bay, bấm lại — quan sát thông báo lỗi mạng thay vì app treo/crash.
4. Thử sửa `POSTS_URL` thành một domain không tồn tại — quan sát `onFailure` bắt đúng lỗi.
