# Code mẫu — Chương 27: ProGuard/R8, keystore, ký ứng dụng

Cùng app Retrofit + Gson ở Chương 22, bật `minifyEnabled true` cho bản release và cấu hình
`proguard-rules.pro` đúng cách để không phá vỡ Gson/Retrofit — xem giải thích chi tiết trong
Chương 27.

## Build thử bản release (đã thu gọn + làm rối code)

```powershell
.\gradlew.bat assembleRelease
```

Sau khi build xong, hai file đáng chú ý:

- `app/build/outputs/apk/release/app-release-unsigned.apk` — APK đã thu gọn (chưa ký, xem mục
  keystore bên dưới để build phiên bản ký được).
- `app/build/outputs/mapping/release/mapping.txt` — **bảng ánh xạ** tên gốc ↔ tên đã bị làm rối
  (ví dụ `Post` ↔ `a`). Lưu file này lại cho MỖI bản release — không có nó, bạn không thể đọc
  hiểu stack trace crash từ người dùng thật (liên quan Chương 40).

## Tạo keystore cho bản release (một lần, dùng lại mãi mãi)

**Không** commit file keystore hay mật khẩu vào git. Lệnh dưới đây chỉ minh hoạ — chạy trên máy
của bạn và cất giữ file `.jks` sinh ra ở nơi an toàn (không phải trong repo):

```powershell
keytool -genkeypair -v -keystore release-key.jks -alias my-app-key -keyalg RSA -keysize 2048 -validity 10000
```

Xem Chương 27 để hiểu vì sao **làm mất file này trước khi đăng ký Google Play App Signing đồng
nghĩa với việc không bao giờ phát hành được bản cập nhật cho app đã public**. Chương 38 sẽ hướng
dẫn nối tiếp: cấu hình `signingConfig` trong Gradle để build thẳng ra APK/AAB đã ký sẵn.
