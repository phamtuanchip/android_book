# Code mẫu — Chương 19: Thread & Executor

Đếm số nguyên tố tới một giới hạn (mặc định 2.000.000) chạy trên `ExecutorService`, có thanh
tiến trình cập nhật trực tiếp và có thể **huỷ giữa chừng** đúng cách bằng `Future.cancel(true)`.

## Chạy thử

1. Mở project bằng Android Studio, Run.
2. Bấm **Bắt đầu** — quan sát thanh tiến trình chạy trong khi UI (kéo/chạm màn hình) vẫn mượt,
   không hề bị đứng dù đang tính toán nặng.
3. Bấm **Huỷ** giữa chừng — quan sát dừng gần như ngay lập tức, hiển thị đúng số đã đếm được
   tới thời điểm huỷ (không phải 0, không phải phải đợi tính hết).
4. Thử để chạy tới khi xong hẳn (không huỷ) — so sánh thông báo kết quả khác với khi huỷ.
