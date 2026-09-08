# Code mẫu — Chương 6: Vòng đời Activity & Application

Ghi log tại mọi callback vòng đời của `MainActivity` và `DemoApplication` để bạn quan sát
trực tiếp thứ tự gọi trên Logcat (lọc theo tag `Lifecycle`).

## Chạy thử

1. Mở project bằng Android Studio, Run lên máy ảo/thiết bị thật.
2. Mở tab **Logcat**, lọc theo tag `Lifecycle`.
3. Thử lần lượt và đối chiếu log với Chương 6:
   - Bấm nút **Mở màn hình 2** → quan sát `MainActivity` `onPause`/`onStop` trước khi `SecondActivity` `onResume`.
   - Bấm **Back** từ màn hình 2 → quan sát `SecondActivity` `onDestroy` và `MainActivity` `onRestart`/`onResume`.
   - Bấm nút Home → quan sát `onPause`/`onStop` nhưng KHÔNG `onDestroy` (activity vẫn còn trong bộ nhớ).
   - Xoay màn hình (nếu auto-rotate bật) → quan sát `onSaveInstanceState` rồi `onDestroy`/`onCreate` lại — đây là hệ quả của thay đổi cấu hình (configuration change) nói ở Chương 6.
