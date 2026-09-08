# Code mẫu — Chương 8: Sự kiện, Intent, điều hướng

Hai màn hình minh hoạ 3 kiểu Intent: explicit intent kèm dữ liệu, nhận kết quả trả về bằng
Activity Result API, và implicit intent mở trình duyệt ngoài.

## Chạy thử

1. Mở project bằng Android Studio, Run.
2. Nhập một tin nhắn, bấm **Gửi sang màn hình 2** — màn hình 2 hiển thị đúng tin nhắn đã gửi.
3. Bấm **Trả lời & quay lại** trên màn hình 2 — quay về màn hình 1, thấy phản hồi hiển thị (minh hoạ nhận kết quả qua `registerForActivityResult`).
4. Bấm **Mở trình duyệt** — hệ thống mở app trình duyệt mặc định của máy (implicit intent, không cần biết trước app nào xử lý).
