# Code mẫu — Chương 16: Service & foreground service

`CounterService` vừa là **started service** (chạy độc lập, đếm số giây, hiển thị notification
liên tục — kể cả khi rời khỏi app) vừa là **bound service** (Activity bind vào để đọc số đếm
trực tiếp qua Binder).

## Chạy thử

1. Mở project bằng Android Studio, Run.
2. Bấm **Bắt đầu** — nếu máy chạy Android 13+, hộp thoại xin quyền thông báo hiện ra, chọn Allow.
3. Quan sát số đếm tăng dần trên màn hình VÀ trên thanh thông báo (kéo thanh trạng thái xuống).
4. Bấm Home rời khỏi app — thông báo vẫn còn đó, số đếm vẫn tiếp tục tăng (xem lại notification
   sẽ thấy số mới hơn) — đây chính là ý nghĩa "foreground service tiếp tục chạy dù không có UI".
5. Mở lại app, bấm **Dừng** — notification biến mất, service dừng hẳn.
