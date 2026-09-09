# Code mẫu — Chương 37: Quyền vị trí — foreground và background

Minh hoạ đúng trình tự bắt buộc: xin quyền vị trí **lúc dùng app** (foreground) trước, chỉ xin
thêm quyền chạy **nền** (background) khi thực sự cần, và luôn xin RIÊNG hai bước — không gộp
chung như một quyền dangerous bình thường (Chương 26).

## Chạy thử

1. Mở project bằng Android Studio, Run trên máy ảo hoặc thiết bị thật (máy ảo mô phỏng vị trí
   được qua Extended Controls → Location, xem lại Chương 3).
2. Bấm **Bật vị trí (khi đang dùng app)** — cấp quyền khi được hỏi — quan sát toạ độ cập nhật.
3. Bấm **Xin thêm quyền chạy nền** — trên Android 11+, hệ thống có thể đưa bạn tới màn hình
   Settings để tự chọn "Allow all the time" thay vì hộp thoại thông thường — đây là hành vi hệ
   thống chủ động thay đổi từ phiên bản này, không phải lỗi code.
4. Từ chối quyền nền — xác nhận app vẫn hoạt động bình thường ở bước 2 (vị trí khi đang mở app
   không phụ thuộc vào quyền nền).
