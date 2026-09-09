# Phụ lục A: Bảng tổng hợp lỗi thường gặp & cách xử lý

Bảng tra cứu nhanh — mỗi dòng tóm tắt một lỗi đã giải thích chi tiết trong chương tương ứng.
Dùng Ctrl+F tìm theo thông báo lỗi hoặc từ khoá khi gặp sự cố, rồi quay lại đúng chương để đọc
phần giải thích đầy đủ.

## Cài đặt & build

| Triệu chứng | Nguyên nhân thường gặp | Xem lại |
|---|---|---|
| `'adb' is not recognized` | `Path` chưa trỏ tới `platform-tools`, hoặc terminal mở trước khi cập nhật biến môi trường | Chương 2 |
| Máy ảo chạy cực chậm/treo lúc khởi động | Chưa bật ảo hoá phần cứng (VT-x/AMD-V), hoặc xung đột Hyper-V/WSL2 | Chương 3 |
| `INSTALL_FAILED_UPDATE_INCOMPATIBLE` | App cũ trên máy ký bằng key khác bản đang cài — gỡ cài đặt cũ trước | Chương 5 |
| Thiếu `android:exported` trên Activity/Service/Receiver có intent-filter | Bắt buộc khai báo tường minh từ Android 12 (API 31) | Chương 4, 8, 32, 34 |
| Build release crash/`null` dữ liệu dù debug chạy tốt | R8 đổi tên field mà Gson/reflection cần đúng tên gốc — thiếu rule `-keep` | Chương 27 |

## Vòng đời & UI

| Triệu chứng | Nguyên nhân thường gặp | Xem lại |
|---|---|---|
| Dữ liệu mất khi xoay màn hình | Không lưu trong `onSaveInstanceState`, hoặc tưởng nhầm nó là lưu trữ lâu dài | Chương 6 |
| Giữ `Activity`/`Context` trong biến `static` hoặc trong ViewModel | Rò rỉ bộ nhớ — Activity không bao giờ được giải phóng | Chương 6, 28 |
| View "biến mất"/lệch vị trí dù XML trông đúng | Thiếu ràng buộc `ConstraintLayout` theo một chiều (ngang hoặc dọc) | Chương 7 |
| `NoMatchingViewException` khi test Espresso | Sai ID, hoặc bàn phím ảo che View cần thao tác (quên `closeSoftKeyboard()`) | Chương 25 |

## Dữ liệu & mạng

| Triệu chứng | Nguyên nhân thường gặp | Xem lại |
|---|---|---|
| `IllegalStateException: Cannot access database on the main thread` | Gọi Room trực tiếp trong `onClickListener`, quên bọc qua `Executor` | Chương 13 |
| `NetworkOnMainThreadException` | Gọi `OkHttpClient.execute()` (đồng bộ) trên main thread thay vì `enqueue()` | Chương 21 |
| Field JSON map ra `null` dù tên đúng chính tả trông giống | Gõ sai `@SerializedName`, hoặc lệch kiểu dữ liệu | Chương 22 |
| Mất mạng khiến màn hình trống trơn | Thiết kế "network-first" thay vì offline-first (luôn đọc cache trước) | Chương 23 |

## Chạy nền & thông báo

| Triệu chứng | Nguyên nhân thường gặp | Xem lại |
|---|---|---|
| `ForegroundServiceDidNotStartInTimeException` | Gọi `startForeground()` quá trễ sau `startForegroundService()` | Chương 16 |
| Broadcast tự định nghĩa không tới nơi | Thiếu `setPackage()`, hoặc kỳ vọng receiver khai báo tĩnh vẫn nhận được (không còn đúng từ Android 8+) | Chương 17 |
| `PeriodicWorkRequest` "chạy chậm hơn khai báo" | Chu kỳ tối thiểu 15 phút — WorkManager tự nâng lên, không phải lỗi | Chương 18 |
| Bấm Huỷ tác vụ nhưng vẫn chạy tới hết | `Future.cancel(true)` chỉ đặt cờ interrupted — code phải tự kiểm tra `isInterrupted()` | Chương 19 |
| Notification không hiện ra, không báo lỗi | Thiếu `NotificationChannel` (Android 8+), hoặc thiếu quyền `POST_NOTIFICATIONS` (Android 13+) | Chương 20 |

## Quyền & bảo mật

| Triệu chứng | Nguyên nhân thường gặp | Xem lại |
|---|---|---|
| `SecurityException` dù đã khai báo `<uses-permission>` | Quyền thuộc nhóm "dangerous" — phải xin thêm ở runtime, khai báo manifest chỉ là điều kiện cần | Chương 26 |
| Xin lại quyền không có tác dụng, không hiện hộp thoại | Người dùng đã chọn "Không hỏi lại" — chỉ còn cách hướng dẫn vào Settings | Chương 26 |
| Quét Bluetooth thất bại âm thầm trên Android 12+ | Xin `ACCESS_FINE_LOCATION` thay vì `BLUETOOTH_SCAN` — hai quyền phục vụ hai nhóm phiên bản khác nhau | Chương 35, 37 |
| Xin quyền vị trí nền bị từ chối ngay lập tức | Cố xin `ACCESS_BACKGROUND_LOCATION` cùng lúc với quyền foreground — phải tách hai bước | Chương 37 |

## Liên ứng dụng & phần cứng

| Triệu chứng | Nguyên nhân thường gặp | Xem lại |
|---|---|---|
| App khác không đọc được `ContentProvider`/`Service` của bạn | Thiếu `android:exported="true"` | Chương 32, 34 |
| `bindService()` sang app khác không tìm thấy gì (API 30+) | Thiếu khai báo `<queries>` (package visibility) trong manifest | Chương 34 |
| Deep link không mở được từ trình duyệt/nguồn ngoài | Thiếu `category.BROWSABLE` trong intent-filter | Chương 33 |
| Kết nối Bluetooth/đọc NFC làm đứng UI | Gọi `connect()`/`accept()`/`read()` (đều CHẶN thread) trực tiếp trên main thread | Chương 35, 36 |

## Xuất bản

| Triệu chứng | Nguyên nhân thường gặp | Xem lại |
|---|---|---|
| Google Play từ chối bản tải lên | `versionCode` không tăng so với bản trước đó | Chương 38, 39 |
| Không đọc được crash log từ bản release đã phát hành | Thiếu `mapping.txt` đúng phiên bản để dịch ngược tên đã bị R8 làm rối | Chương 27, 40 |
| Mất khả năng phát hành bản cập nhật cho app cũ | Làm mất file keystore release và không dùng Google Play App Signing | Chương 27, 38 |
