# Code mẫu — Chương 20: Notification

Thông báo nhắc nhở đầy đủ: channel, tap để mở app (kèm dữ liệu báo "mở từ thông báo"), và một
nút hành động ngay trên thông báo xử lý bằng `BroadcastReceiver` — không cần mở app.

## Chạy thử

1. Mở project bằng Android Studio, Run.
2. Bấm **Gửi thông báo nhắc nhở** — nếu Android 13+, cho phép quyền thông báo khi được hỏi.
3. Kéo thanh trạng thái xuống — thấy thông báo với 2 hành động: bấm vào nội dung, hoặc bấm nút
   **Đánh dấu đã đọc**.
4. Bấm nút **Đánh dấu đã đọc** ngay trên thông báo (không mở app) — thông báo tự đóng, Toast hiện ra.
5. Gửi lại một thông báo khác, lần này bấm vào NỘI DUNG thông báo — app mở lên, dòng trạng thái
   báo "Bạn vừa mở app bằng cách bấm vào thông báo!".
