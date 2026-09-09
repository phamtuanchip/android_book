# Code mẫu — Chương 34: AIDL & Bound Service (IPC)

**Hai app riêng biệt**, giống cấu trúc Chương 32:

- `:server` (package `vn.example.ch34aidlserver`) — công khai `CalculatorService` qua AIDL.
- `:client` (package `vn.example.ch34aidlclient`) — bind sang service của app kia, gọi `add()`/
  `multiply()` như gọi hàm Java bình thường, dù thật ra đang đi qua ranh giới tiến trình (Binder).

## Chạy thử

1. Chạy cấu hình module **server** trước — chỉ cần cài lên máy ảo/thiết bị, không cần mở màn hình.
2. Chạy cấu hình module **client** — mở app, quan sát dòng trạng thái chuyển thành "Đã kết nối".
3. Nhập hai số, bấm **Cộng** hoặc **Nhân** — kết quả tính TRONG TIẾN TRÌNH CỦA APP SERVER được trả
   về và hiển thị ở app client, kèm số lượt gọi tích luỹ (chứng minh cùng một Service instance
   phục vụ nhiều lượt gọi).
4. Gỡ cài đặt app **server**, mở lại app **client** — quan sát thông báo "Không tìm thấy
   Calculator Server" thay vì crash.
