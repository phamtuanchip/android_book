# Code mẫu — Chương 38: Chuẩn bị release build, ký ứng dụng, App Bundle

Cùng app Retrofit+Gson đã dùng ở Chương 22/27, giờ cấu hình đầy đủ `signingConfig` đọc từ file
`keystore.properties` riêng (không commit), `versionCode`/`versionName` theo semantic versioning,
và sẵn sàng build ra cả APK lẫn Android App Bundle (AAB).

## Build APK/AAB chưa ký (không cần tạo keystore)

Vì project CHƯA có file `keystore.properties` thật, `assembleRelease`/`bundleRelease` vẫn chạy
được, chỉ là kết quả **chưa được ký**:

```powershell
.\gradlew.bat assembleRelease   # ra app/build/outputs/apk/release/app-release-unsigned.apk
.\gradlew.bat bundleRelease     # ra app/build/outputs/bundle/release/app-release.aab
```

## Build bản đã ký thật (tự tạo keystore trước)

1. Tạo keystore (chỉ làm MỘT LẦN, giữ file này thật an toàn — xem cảnh báo ở Chương 27):

   ```powershell
   keytool -genkeypair -v -keystore release-key.jks -alias my-app-key -keyalg RSA -keysize 2048 -validity 10000
   ```

   Đặt file `release-key.jks` ngay trong thư mục project này (đã có trong `.gitignore`, không
   bao giờ bị commit).

2. Sao chép `keystore.properties.example` thành `keystore.properties`, điền đúng mật khẩu bạn vừa
   đặt ở bước 1.

3. Build lại — lần này `assembleRelease`/`bundleRelease` tự động ký:

   ```powershell
   .\gradlew.bat assembleRelease
   ```

   Xác nhận file kết quả đã ký bằng:

   ```powershell
   apksigner verify --print-certs app\build\outputs\apk\release\app-release.apk
   ```

## So sánh APK và AAB

- `assembleRelease` → một file `.apk` duy nhất, cài trực tiếp lên thiết bị bằng `adb install`.
- `bundleRelease` → một file `.aab` — **không cài trực tiếp được**, dùng để tải lên Google Play
  (Chương 39); Play Store tự sinh ra các APK tối ưu riêng cho từng cấu hình thiết bị người dùng
  từ file `.aab` này.
