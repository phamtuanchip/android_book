# Code mẫu — Chương 24: Unit test với JUnit

`PasswordStrengthChecker` là logic Java thuần (không đụng gì tới Android) được test đầy đủ
bằng JUnit trong `app/src/test/java/`, rồi dùng lại trong `MainActivity` để hiển thị theo
thời gian thực khi gõ mật khẩu.

## Chạy test

1. Mở project bằng Android Studio.
2. Mở `PasswordStrengthCheckerTest.java`, bấm nút mũi tên xanh cạnh tên class (hoặc cạnh từng
   `@Test` để chạy riêng lẻ).
3. Xem kết quả trong tab **Run** — mỗi test pass/fail kèm thời gian chạy (thường vài chục ms).
4. Thử sửa `PasswordStrengthChecker.check()` cố tình sai một điều kiện, chạy lại test — quan sát
   đúng test nào báo đỏ, giúp khoanh vùng lỗi ngay lập tức.

## Chạy app

Run bình thường, gõ vào ô mật khẩu, quan sát nhãn độ mạnh cập nhật theo thời gian thực — dùng
đúng hàm `PasswordStrengthChecker.check()` đã được test ở trên.
