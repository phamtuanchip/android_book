# Code mẫu — Chương 25: UI test với Espresso

Cùng app kiểm tra độ mạnh mật khẩu từ Chương 24, giờ thêm `MainActivityEspressoTest` trong
`app/src/androidTest/java/` — test thao tác trực tiếp lên UI thật.

## Chạy test

1. Kết nối máy ảo/thiết bị thật (Chương 3) — Instrumented test BẮT BUỘC cần một trong hai.
2. Mở `MainActivityEspressoTest.java`, bấm nút mũi tên xanh cạnh tên class.
3. Quan sát: Android Studio tự cài app lên máy ảo, Espresso tự gõ chữ vào ô mật khẩu, tự kiểm
   tra nhãn hiển thị — toàn bộ không cần bạn chạm tay vào màn hình.
4. Thử sửa `PasswordStrengthChecker` (như bài tập Chương 24) để phá vỡ logic — chạy lại test này
   và xác nhận nó cũng bắt được lỗi tương tự Unit test, nhưng từ góc nhìn "người dùng thao tác
   trên UI thật" thay vì gọi thẳng hàm Java.
