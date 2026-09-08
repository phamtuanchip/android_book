# Android Book — Kế hoạch biên soạn sách

Sách lập trình Android bằng tiếng Việt, dành cho người đã biết Java OOP cơ bản, đi từ
cài đặt môi trường đến kiến trúc nâng cao, kèm code mẫu chạy được cho từng chương.

## 1. Mục tiêu

- Dạy lập trình Android từ cơ bản đến nâng cao/chuyên sâu.
- Mỗi bài/chương có ví dụ code mẫu, và code đó được lưu trên repo (chạy được, không phải snippet rời rạc).
- Xuất bản dạng "release" — chất lượng đủ để công bố công khai kèm code kèm theo.
- Định dạng phát hành: **HTML** và **PDF** trước, **EPUB** ở giai đoạn sau.

## 2. Đối tượng độc giả

- Đã biết lập trình Java cơ bản và OOP (class, interface, kế thừa, đa hình...).
- Chưa biết về Android và những khái niệm cơ bản về ứng dụng thiết bị dị động — cần hướng dẫn từ bước cài đặt môi trường.
- Giả định người đọc không biết Kotlin, Gradle, hay các khái niệm riêng của Android.
- Cần có cả phần nâng cao, giao tiếp giữa các app, điều khiển tầng sâu như bluetooth, nfc.

## 3. Cấu trúc thư mục dự kiến

```
android_book/
├── book/                     # Nội dung sách (Markdown), 1 file/chương
│   ├── part0-setup/
│   ├── part1-fundamentals/
│   ├── part2-storage/
│   ├── part3-background/
│   ├── part4-networking/
│   ├── part5-testing-security/
│   ├── part6-architecture/
│   └── part7-publishing/
├── code/                     # Code mẫu, 1 project Android/module cho mỗi chương
│   └── ch01-hello-world/ ...
├── build/                    # Script/cấu hình build HTML, PDF, EPUB
└── README.md                 # File kế hoạch này
```

Quy ước: mỗi chương gồm 1 file nội dung trong `book/` + 1 project/module tương ứng
trong `code/` (đặt tên khớp số chương, vd `ch07-recyclerview`), có README riêng
hướng dẫn chạy.

## 4. Quy ước cho mỗi chương

Mỗi chương cần có:
1. Mục tiêu học (đọc xong làm được gì).
2. Lý thuyết/khái niệm nền, giải thích ngắn gọn, có sơ đồ nếu cần.
3. Ví dụ code từng bước, giải thích tại sao (không chỉ liệt kê code).
4. Project mẫu hoàn chỉnh trong `code/`, build & chạy được trên máy ảo lẫn thiết bị thật.
5. Bài tập/gợi ý mở rộng cuối chương.
6. Lỗi thường gặp + cách khắc phục (đúc kết từ thực tế, không lý thuyết suông).

## 5. Mục lục sách (dự kiến)

### Phần 0 — Chuẩn bị môi trường
1. Giới thiệu Android: kiến trúc hệ điều hành, vòng đời phiên bản, hệ sinh thái
2. Cài đặt JDK, Android Studio, SDK/SDK Manager
3. Tạo và cấu hình máy ảo (AVD), kết nối thiết bị thật (USB debugging, ADB)
4. Tạo project đầu tiên: cấu trúc project, Gradle, `AndroidManifest.xml`
5. Build & deploy ứng dụng "Hello World" đầu tiên lên emulator/thiết bị thật

### Phần 1 — Nền tảng
6. Vòng đời Activity & Application
7. Layout & Views (XML layout, ConstraintLayout, ViewGroup)
8. Sự kiện, Intent (explicit/implicit), điều hướng giữa các màn hình
9. Fragment & Navigation Component
10. RecyclerView & Adapter (danh sách, hiệu năng)
11. Resource: string/dimens/style, đa ngôn ngữ, đa kích thước màn hình

### Phần 2 — Lưu trữ dữ liệu
12. SharedPreferences (cấu hình, trạng thái đơn giản)
13. SQLite & Room (cơ sở dữ liệu quan hệ)
14. File storage: internal vs external, scoped storage
15. Jetpack DataStore (thay thế SharedPreferences hiện đại)

### Phần 3 — Chạy ngầm & bất đồng bộ
16. Service & foreground service
17. BroadcastReceiver, hệ thống sự kiện của Android
18. WorkManager (tác vụ nền có lịch/điều kiện)
19. Xử lý bất đồng bộ: Thread/Executor (Java) hoặc Coroutine (nếu mở rộng sang Kotlin)
20. Notification

### Phần 4 — Mạng & API
21. Gọi HTTP với OkHttp/Retrofit
22. Parse JSON (Gson/Moshi), mapping dữ liệu
23. Xử lý lỗi mạng, retry, cache offline-first cơ bản

### Phần 5 — Kiểm thử, Sandbox & Bảo mật
24. Unit test với JUnit
25. UI test với Espresso
26. Sandbox của Android: mô hình quyền (permission), runtime permission
27. Bảo mật cơ bản: ProGuard/R8, keystore, ký ứng dụng

### Phần 6 — Kiến trúc nâng cao
28. MVVM, ViewModel & LiveData
29. Dependency Injection với Hilt
30. Giới thiệu Jetpack Compose
31. Clean Architecture cơ bản cho ứng dụng Android

### Phần 7 — Giao tiếp liên ứng dụng & phần cứng nâng cao
32. Content Provider: chia sẻ dữ liệu có kiểm soát giữa các ứng dụng
33. Giao tiếp liên ứng dụng nâng cao: App Links/Deep Link, Share sheet, PendingIntent
34. AIDL & Bound Service: giao tiếp liên tiến trình (IPC) giữa các app
35. Bluetooth: Classic & BLE — quét thiết bị, kết nối, truyền/nhận dữ liệu
36. NFC: đọc/ghi tag, Host Card Emulation (HCE), giao tiếp giữa 2 thiết bị
37. Quyền & sandbox cho phần cứng: location cho BLE/NFC, background location, xin quyền runtime theo API level

### Phần 8 — Xuất bản ứng dụng
38. Chuẩn bị release build, ký ứng dụng, tối ưu kích thước (App Bundle)
39. Đưa ứng dụng lên Google Play Console
40. Theo dõi sau phát hành: Crashlytics, Analytics cơ bản

### Phụ lục
- Bảng tổng hợp lỗi thường gặp & cách xử lý
- Tài liệu tham khảo & nguồn học thêm

## 6. Pipeline xuất bản (HTML → PDF → EPUB)

- Viết nội dung bằng Markdown trong `book/`, mỗi chương 1 file, có front-matter
  (số chương, tiêu đề, mục tiêu học).
- Dùng công cụ build tài liệu kỹ thuật (ví dụ: mdBook, Honkit, hoặc Pandoc) để:
  - Sinh **HTML** dạng sách (có mục lục điều hướng, syntax highlight cho code).
  - Sinh **PDF** từ cùng nguồn Markdown (qua Pandoc + LaTeX, hoặc in từ HTML).
  - Giai đoạn sau: sinh **EPUB** từ cùng nguồn, không viết lại nội dung.
- Cần chọn cụ thể 1 công cụ trước khi bắt đầu để tránh phải chuyển định dạng giữa chừng.

## 7. Lộ trình biên soạn (milestones)

1. Chốt công cụ build (mdBook/Honkit/Pandoc) + dựng khung thư mục `book/`, `code/`.
2. Viết & code mẫu xong Phần 0 (cài đặt môi trường) — cột mốc "chạy được Hello World".
3. Viết & code mẫu xong Phần 1–2 (nền tảng + lưu trữ).
4. Viết & code mẫu xong Phần 3–4 (chạy ngầm + mạng).
5. Viết & code mẫu xong Phần 5–6 (kiểm thử, bảo mật, kiến trúc nâng cao).
6. Viết & code mẫu xong Phần 7 (giao tiếp liên ứng dụng, Bluetooth, NFC) — cần thiết bị thật để test, emulator không mô phỏng đủ.
7. Viết Phần 8 (xuất bản) + phụ lục.
8. Build bản HTML + PDF hoàn chỉnh, rà soát lại toàn bộ code mẫu chạy được.
9. Xuất bản EPUB.

## 8. Việc cần quyết định tiếp theo

- Ngôn ngữ code mẫu: Java thuần theo toàn bộ sách, hay chuyển dần sang Kotlin ở phần nâng cao?
- Công cụ build tài liệu cụ thể (ảnh hưởng cấu trúc file Markdown ngay từ đầu).
- Phiên bản Android SDK tối thiểu/mục tiêu để đảm bảo code mẫu nhất quán.
