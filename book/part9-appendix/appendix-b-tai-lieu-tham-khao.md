# Phụ lục B: Tài liệu tham khảo & nguồn học thêm

Sách này ưu tiên giải thích khái niệm và cơ chế bằng tiếng Việt kèm code chạy được thật —
tài liệu chính thức bên dưới luôn là nguồn cập nhật nhất khi Android ra phiên bản mới, hoặc khi
cần tra cứu chi tiết API vượt ngoài phạm vi một chương giới thiệu.

## Tài liệu chính thức Android

- **developer.android.com/guide** — tài liệu hướng dẫn chính thức, theo từng chủ đề (Activity, Fragment, Intent, Service...) — nguồn tham khảo hàng đầu cho Phần 0, 1, 3.
- **developer.android.com/reference** — tài liệu tham khảo API đầy đủ (Javadoc), tra cứu chính xác tham số/hành vi từng phương thức.
- **developer.android.com/training/permissions** — tài liệu chi tiết về mô hình quyền runtime, cập nhật theo từng phiên bản Android mới (bổ sung cho Chương 26, 37).
- **developer.android.com/topic/architecture** — hướng dẫn kiến trúc chính thức của Google (MVVM, Repository, UDF) — mở rộng cho Chương 28, 31.
- **developer.android.com/jetpack** — tổng quan các thư viện Jetpack (Room, Lifecycle, Navigation, Hilt, Compose...) đã dùng xuyên suốt sách.
- **developer.android.com/jetpack/compose** — tài liệu đầy đủ về Jetpack Compose, mở rộng cho Chương 30.
- **developer.android.com/guide/topics/connectivity/bluetooth** và **.../nfc** — tài liệu chi tiết Bluetooth/NFC, mở rộng cho Chương 35, 36.
- **developer.android.com/studio/publish** — hướng dẫn chính thức về ký ứng dụng, App Bundle, phát hành — mở rộng cho Chương 38, 39.
- **source.android.com** — tài liệu về nội bộ hệ điều hành Android (AOSP), cho ai muốn tìm hiểu sâu hơn tầng dưới Chương 1.

## Thư viện chính đã dùng trong sách

- **square.github.io/okhttp** và **square.github.io/retrofit** — tài liệu chính thức của OkHttp/Retrofit (Chương 21-22).
- **developer.android.com/jetpack/androidx/releases/room** — ghi chú phát hành và tài liệu Room (Chương 13, 23).
- **dagger.dev/hilt** — tài liệu chính thức Dagger Hilt (Chương 29, 31).
- **github.com/google/gson** — tài liệu Gson (Chương 22-23).

## Kotlin (cho ai muốn mở rộng sau Chương 30)

- **kotlinlang.org/docs** — tài liệu ngôn ngữ Kotlin chính thức.
- **developer.android.com/kotlin** — tài liệu Android dành riêng cho Kotlin, bao gồm Coroutines (nhắc tới ở Chương 19) và Compose đầy đủ (Chương 30).

## Dịch vụ theo dõi sau phát hành (mở rộng Chương 40)

- **firebase.google.com/docs/crashlytics** — tài liệu Firebase Crashlytics.
- **firebase.google.com/docs/analytics** — tài liệu Firebase Analytics.

## Công cụ build tài liệu (dùng để tạo sách này)

- **mermaid.js.org** — cú pháp vẽ sơ đồ Mermaid, dùng trong toàn bộ hình minh hoạ của sách.
- **markdown-it** (thư viện npm) — bộ chuyển đổi Markdown sang HTML dùng trong `tools/build.js` của repo này.

---

Đã đọc hết 40 chương và hai phụ lục — cảm ơn bạn đã theo sách tới đây. Toàn bộ mã nguồn ví dụ
nằm trong thư mục `code/` của repo, độc lập với nội dung sách, có thể mở trực tiếp bằng Android
Studio để tiếp tục thử nghiệm.
