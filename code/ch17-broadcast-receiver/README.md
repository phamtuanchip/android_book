# Code mẫu — Chương 17: BroadcastReceiver

Hai kiểu broadcast trong một màn hình: broadcast **hệ thống** (`ACTION_BATTERY_CHANGED`) và
broadcast **tự định nghĩa** trong phạm vi app của chính mình.

## Chạy thử

1. Mở project bằng Android Studio, Run.
2. Quan sát phần trăm pin hiển thị ngay khi mở app — không cần thao tác gì (sticky broadcast).
3. Bấm **Gửi tín hiệu** vài lần — quan sát bộ đếm tăng, số thứ tự tín hiệu gần nhất đúng.
4. (Tuỳ chọn) Rút/cắm sạc thiết bị thật — quan sát phần trăm pin tự cập nhật theo thời gian thực mà không cần bấm gì.
