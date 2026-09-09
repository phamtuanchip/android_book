# Code mẫu — Chương 40: Theo dõi sau phát hành (Crashlytics/Analytics tự xây)

Cài đặt tối giản đúng CƠ CHẾ mà Firebase Crashlytics và các SDK analytics thật tự động hoá —
không cần tài khoản Firebase, chạy được ngay, để bạn hiểu rõ "hộp đen" bên dưới các dịch vụ đó
làm gì trước khi dùng bản thật.

## Chạy thử — Crash reporting

1. Mở project bằng Android Studio, Run.
2. Quan sát dòng "Không có crash log nào từ lần chạy trước".
3. Bấm **Gây crash thử** — app đóng lại như một crash bình thường (đây là hành vi ĐÚNG, không
   phải lỗi — `CrashReporter` chỉ GHI LẠI rồi để hệ thống xử lý tiếp, không "cứu" app).
4. Mở lại app — lần này thấy "Tìm thấy 1 crash log từ (các) lần chạy trước", kèm đầy đủ stack
   trace, tên thiết bị, phiên bản app đã ghi lại từ lần crash trước.
5. Bấm **Xoá crash log đã lưu**, mở lại app — xác nhận log đã biến mất.

## Chạy thử — Analytics batching

1. Bấm **Ghi một sự kiện** vài lần liên tiếp trong vòng dưới 10 giây.
2. Quan sát Logcat (tag `AnalyticsLogger`): mỗi lần bấm chỉ log "Đã xếp hàng", KHÔNG log "Gửi đợt"
   ngay lập tức.
3. Đợi đủ 10 giây (không thao tác gì) — quan sát dòng log "Gửi đợt gồm N sự kiện" xuất hiện một
   lần duy nhất, gộp tất cả sự kiện đã bấm — minh hoạ đúng cơ chế batching thật.

## Bước tiếp theo nếu muốn dùng Firebase Crashlytics/Analytics thật

Thay `CrashReporter`/`AnalyticsLogger` cục bộ bằng:

1. Tạo project trên [Firebase Console](https://console.firebase.google.com), tải file
   `google-services.json` về, đặt vào thư mục `app/`.
   thêm plugin `com.google.gms.google-services` và `com.google.firebase.crashlytics` vào
   `build.gradle`, cùng dependency `com.google.firebase:firebase-crashlytics` và
   `com.google.firebase:firebase-analytics`.
2. Gọi `FirebaseCrashlytics.getInstance().log(...)`/`recordException(...)` thay vì
   `CrashReporter`, và `FirebaseAnalytics.getInstance(context).logEvent(...)` thay vì
   `AnalyticsLogger` — cùng ý tưởng, nhưng có dashboard trực quan, tự động dedup lỗi trùng, và tự
   tải lên server thật.
