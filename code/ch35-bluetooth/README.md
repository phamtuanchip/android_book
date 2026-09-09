# Code mẫu — Chương 35: Bluetooth Classic & BLE

**Cần THIẾT BỊ THẬT để chạy đầy đủ** — máy ảo Android không mô phỏng phần cứng Bluetooth thật,
nên không thể quét/kết nối/truyền dữ liệu bằng máy ảo. Cần ít nhất **hai điện thoại/máy tính
bảng Android thật** để test đầy đủ (một làm server, một làm client) cho phần Classic; phần quét
BLE chỉ cần một thiết bị thật (quét các thiết bị BLE xung quanh: tai nghe, vòng đeo tay, v.v.).

## Chuẩn bị (chỉ cho phần Bluetooth Classic — chat)

1. Trên **cả hai** thiết bị: vào Settings → Bluetooth, bật Bluetooth, ghép đôi (pair) hai thiết
   bị với nhau trước (quy trình ghép đôi chuẩn của hệ thống, ngoài phạm vi code mẫu này).
2. Cài app này lên **cả hai** thiết bị đã ghép đôi.

## Chạy thử — Bluetooth Classic (chat hai chiều)

1. Trên **thiết bị A**: mở app, cấp quyền khi được hỏi, bấm **Bắt đầu làm Server** — trạng thái
   chuyển thành "Đang chờ thiết bị khác kết nối tới".
2. Trên **thiết bị B**: mở app, cấp quyền, trong danh sách "Thiết bị đã ghép đôi" bấm vào thiết bị
   A — trạng thái cả hai bên chuyển thành "Đã kết nối".
3. Gõ tin nhắn ở bất kỳ bên nào, bấm **Gửi** — quan sát tin nhắn xuất hiện ở khung log của thiết
   bị còn lại gần như ngay lập tức.
4. Tắt Bluetooth trên một thiết bị — quan sát bên còn lại nhận thông báo "Đã ngắt kết nối".

## Chạy thử — Quét BLE

1. Từ màn hình chính, bấm **Mở màn hình quét BLE**.
2. Bấm **Bắt đầu quét BLE**, cấp quyền khi được hỏi — quan sát danh sách thiết bị BLE xung quanh
   xuất hiện dần (tai nghe Bluetooth, thiết bị đeo tay, TV thông minh...), kèm chỉ số RSSI (tín
   hiệu càng gần 0 càng mạnh/gần).
3. Quét tự dừng sau 12 giây, hoặc bấm **Dừng quét** thủ công.
