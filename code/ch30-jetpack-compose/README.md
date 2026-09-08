# Code mẫu — Chương 30: Giới thiệu Jetpack Compose

**Project DUY NHẤT trong sách viết bằng Kotlin** — Jetpack Compose không có API cho Java. Cùng
màn hình "nhập tên, bấm Chào, hiện lời chào" như `code/ch07-layout-views/`, viết lại hoàn toàn
bằng Compose để so sánh trực tiếp hai cách tiếp cận.

## Chạy thử

1. Mở project bằng Android Studio, để Gradle sync xong (Compose compiler chạy lúc build).
2. Run — nhập tên, bấm **Chào**, quan sát dòng chữ bên dưới cập nhật.
3. Mở `MainActivity.kt` cạnh `code/ch07-layout-views/.../MainActivity.java` — so sánh trực tiếp
   cách hai bên xử lý cùng một tính năng.

## Vì sao dự án này khác mọi dự án khác trong sách?

Toàn bộ sách dùng Java để phù hợp với người đọc "đã biết Java OOP cơ bản" (xem README gốc của
repo). Jetpack Compose là ngoại lệ bắt buộc — thư viện này được thiết kế và chỉ hoạt động với
Kotlin. Chương 30 giới thiệu Compose ở mức khái niệm cho bạn biết nó tồn tại và khác gì cách làm
UI bằng XML đã dùng xuyên suốt sách; không có ý định dạy Kotlin đầy đủ trong phạm vi sách này.
