# Code mẫu — Chương 32: Content Provider

**Hai app HOÀN TOÀN riêng biệt** trong cùng project này (hai module Gradle, build ra hai APK
khác nhau) để minh hoạ đúng bản chất "chia sẻ dữ liệu giữa các app":

- `:app` (package `vn.example.ch32provider`) — app "chủ", quản lý ghi chú bằng Room (như
  Chương 13), công khai dữ liệu qua `NoteProvider` (`ContentProvider`).
- `:client` (package `vn.example.ch32client`) — app "khách", **không phụ thuộc code của `:app`**,
  chỉ biết URI `content://vn.example.ch32provider.provider/notes` để đọc/ghi.

## Chạy thử

1. Trong Android Studio, chọn cấu hình chạy module **app** trước, Run — cài và mở app Note
   Provider, thêm vài ghi chú qua giao diện của nó.
2. Đổi cấu hình chạy sang module **client**, Run — cài và mở app Note Client (cả hai app cùng
   tồn tại trên máy ảo/thiết bị, độc lập nhau).
3. Trong app Client, bấm **Đọc ghi chú từ app khác** — xác nhận thấy đúng các ghi chú đã thêm ở
   bước 1, đọc được từ một tiến trình/app hoàn toàn khác.
4. Bấm **Thêm ghi chú vào app khác** trong app Client — quay lại app Note Provider (không cần
   khởi động lại), kéo danh sách xem — ghi chú mới từ Client đã xuất hiện.
5. (Thử nghiệm) Gỡ cài đặt app **app** (Note Provider), mở lại app Client, bấm đọc ghi chú —
   quan sát thông báo lỗi rõ ràng thay vì crash, vì `ContentProvider` không còn tồn tại.
