# Code mẫu — Chương 18: WorkManager

Một `SyncWorker` giả lập tác vụ đồng bộ mất 3 giây, chạy qua `OneTimeWorkRequest` (xem kết quả
ngay) và minh hoạ cú pháp `PeriodicWorkRequest` (chu kỳ tối thiểu 15 phút, không xem được ngay).

## Chạy thử

1. Mở project bằng Android Studio, Run.
2. Bấm **Đồng bộ ngay** — quan sát trạng thái đổi ENQUEUED → RUNNING → SUCCEEDED (kèm giờ hoàn
   thành) trong khoảng 3 giây.
3. Bật chế độ máy bay trên máy ảo/thiết bị TRƯỚC khi bấm — quan sát trạng thái dừng ở ENQUEUED,
   chỉ chuyển sang RUNNING sau khi tắt chế độ máy bay (minh hoạ ràng buộc `NetworkType.CONNECTED`).
4. Bấm **Lên lịch định kỳ** — mở Logcat lọc tag `SyncWorker`, đợi khoảng 15 phút để thấy nó tự
   chạy (hoặc dùng `adb shell cmd jobscheduler` để ép chạy sớm hơn khi tìm hiểu thêm ngoài sách).
