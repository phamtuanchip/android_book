# Code mẫu — Chương 4 & 5: Hello World

Project Android tối thiểu dùng xuyên suốt Chương 4 (giải thích cấu trúc project) và Chương 5
(build & deploy). Ngôn ngữ: Java. `compileSdk`/`targetSdk` 34, `minSdk` 24.

## Mở project

1. Mở Android Studio → **Open** → chọn thư mục `ch04-05-hello-world` này.
2. Chờ Gradle sync lần đầu (tải dependencies từ Google/Maven Central).
3. Chọn máy ảo hoặc thiết bị thật đã kết nối (Chương 3) ở thanh công cụ.
4. Bấm **Run ▶**.

## Build & cài bằng dòng lệnh (nội dung Chương 5)

```powershell
# Từ thư mục project này
.\gradlew.bat assembleDebug
adb install app\build\outputs\apk\debug\app-debug.apk
```

> Thư mục này chưa kèm sẵn `gradlew.bat`/Gradle wrapper binary (không phù hợp check vào sách dạng
> text) — khi mở project bằng Android Studio, IDE sẽ tự tạo wrapper cho bạn. Nếu muốn chạy
> `gradlew` mà chưa mở qua Android Studio, chạy `gradle wrapper` (yêu cầu đã cài Gradle) để sinh
> file wrapper trước.

## Icon ứng dụng

Manifest dùng tạm icon hệ thống có sẵn (`@android:drawable/sym_def_app_icon`) để giữ project gọn
nhẹ dạng text. Khi tự tạo project thật trong Android Studio, dùng **File → New → Image Asset** để
sinh bộ icon riêng — chi tiết không thuộc phạm vi 2 chương này.
