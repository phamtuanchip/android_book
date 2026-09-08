# Code mẫu — Chương 26: Sandbox & runtime permission

Đếm số liên hệ trong danh bạ máy — minh hoạ đầy đủ vòng đời xin quyền runtime: cấp lần đầu, từ
chối rồi hỏi lại kèm giải thích, và "Không hỏi lại" phải hướng dẫn qua Settings.

## Chạy thử

1. Mở project bằng Android Studio, Run.
2. Bấm **Đọc số lượng danh bạ** — hộp thoại xin quyền hiện ra, chọn **Allow** — số liên hệ hiển thị.
3. Gỡ cài đặt app (để reset trạng thái quyền), cài lại. Lần này bấm nút, chọn **Deny** — quan sát
   dòng giải thích lý do cần quyền hiện ra.
4. Bấm nút lần nữa, lần này chọn **Deny & don't ask again** (hoặc tương đương tuỳ phiên bản
   Android) — quan sát nút **Mở Cài đặt ứng dụng** xuất hiện, vì từ giờ hệ thống không còn tự
   hiện hộp thoại xin quyền nữa.
