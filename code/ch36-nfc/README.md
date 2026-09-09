# Code mẫu — Chương 36: NFC

**Cần thiết bị Android thật có NFC** để chạy phần đọc/ghi thẻ. Phần Host Card Emulation (HCE)
biên dịch và cài đặt đúng, nhưng cần đầu đọc NFC chuyên dụng để tự kiểm chứng — xem giải thích
trong `HceService.java`.

## Chuẩn bị

- Một điện thoại Android thật có NFC, đã bật NFC trong Settings.
- Một vài thẻ NFC trống (NFC tag/sticker) — loại phổ biến, giá rẻ, mua được ở cửa hàng linh kiện
  điện tử hoặc sàn thương mại điện tử. Thẻ **có thể ghi lại nhiều lần** (không phải loại chỉ ghi
  một lần).

## Chạy thử — Đọc và ghi NDEF

1. Mở project bằng Android Studio, Run lên thiết bị thật.
2. Chạm một thẻ NFC trống vào mặt sau máy — quan sát log hiển thị "không chứa dữ liệu NDEF" (thẻ
   trống, hợp lý).
3. Gõ một đoạn văn bản vào ô nhập, bấm **Chờ chạm thẻ để GHI** — chạm CÙNG thẻ đó lại — quan sát
   log báo ghi thành công.
4. Chạm lại thẻ đó lần nữa (không bấm nút ghi trước) — quan sát log đọc đúng lại nội dung vừa ghi.
5. Thử ghi đè nội dung khác lên cùng thẻ — xác nhận đọc lại thấy nội dung MỚI, không phải nội
   dung cũ.

## Về phần Host Card Emulation (`HceService.java`)

Đây là phần **khó tự kiểm chứng tại nhà** — cần một đầu đọc thẻ NFC thật (máy POS, đầu đọc kiểm
soát ra vào) đã cấu hình nhận diện đúng AID `F0010203040506` khai báo trong
`res/xml/apduservice.xml`. Mục tiêu của phần này trong sách là để bạn đọc hiểu **cơ chế**, không
phải để tự chạy thử ngay — nếu có điều kiện tiếp cận thiết bị đầu đọc NFC lập trình được (nhiều
board phát triển NFC hỗ trợ gửi lệnh APDU tuỳ ý), có thể tự kiểm chứng thêm ngoài phạm vi sách.
