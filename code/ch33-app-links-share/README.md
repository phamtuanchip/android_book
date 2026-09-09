# Code mẫu — Chương 33: App Links, Deep Link, Share sheet

Một app minh hoạ ba cơ chế giao tiếp liên ứng dụng: chia sẻ văn bản qua Share sheet (cả gửi lẫn
nhận), và mở màn hình cụ thể qua Deep Link (`ch33share://note/42`).

## Chạy thử

1. Mở project bằng Android Studio, Run.
2. Nhập một tin nhắn, bấm **Chia sẻ qua app khác** — hộp thoại "Chia sẻ qua" của hệ thống hiện
   ra, liệt kê các app khác (và cả app này) có thể nhận `text/plain`.
3. Từ một app khác trên máy ảo có hỗ trợ chia sẻ văn bản (ví dụ Chrome: chọn văn bản → Share),
   chọn app **App Links & Share Demo** trong danh sách — quan sát dòng "Đã nhận chia sẻ..." hiện
   ra khi app mở lên.
4. Bấm **Mở deep link ch33share://note/42** — màn hình chi tiết ghi chú mở ra, hiển thị đúng ID
   `42` lấy từ URI.
5. (Tuỳ chọn) Chạy lệnh dưới đây từ PowerShell để xác nhận deep link cũng mở được từ BÊN NGOÀI
   app (mô phỏng một trang web/thông báo gọi vào):

   ```powershell
   adb shell am start -a android.intent.action.VIEW -d "ch33share://note/99"
   ```

## Vì sao phần App Link (https://example.com/note/...) không hoạt động thật?

Khai báo `autoVerify="true"` với domain `https` trong `AndroidManifest.xml` chỉ mang tính **minh
hoạ cú pháp** — Chương 33 giải thích rõ nó cần bạn thật sự sở hữu domain và host file
`.well-known/assetlinks.json` tương ứng thì Android mới xác minh và tự động mở app thay vì trình
duyệt. Phần thật sự chạy được ngay trên máy bạn là **custom scheme** (`ch33share://`).
