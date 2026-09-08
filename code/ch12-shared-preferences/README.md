# Code mẫu — Chương 12: SharedPreferences

Màn hình cài đặt lưu 3 loại dữ liệu đơn giản (chuỗi, boolean, số nguyên) bằng
`SharedPreferences`, gói gọn trong `PrefsManager`.

## Chạy thử

1. Mở project bằng Android Studio, Run.
2. Nhập tên, bật/tắt switch, kéo thanh cỡ chữ, bấm **Lưu**.
3. Vuốt app khỏi danh sách app gần đây (đóng hẳn, không chỉ bấm Home), mở lại app —
   xác nhận cả 3 giá trị vẫn còn nguyên (khác `onSaveInstanceState` ở Chương 6).
4. Mở Logcat lọc tag `PrefsDemo` — mỗi lần bấm Lưu sẽ thấy log tên các key vừa đổi,
   minh hoạ `OnSharedPreferenceChangeListener`.
