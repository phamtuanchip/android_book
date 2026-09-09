# Android Book — Kế hoạch biên soạn sách

Sách lập trình Android bằng tiếng Việt, dành cho người đã biết Java OOP cơ bản, đi từ
cài đặt môi trường đến kiến trúc nâng cao, kèm code mẫu chạy được cho từng chương.

> **Trạng thái: đã viết xong toàn bộ 40 chương + 2 phụ lục.** Xem `dist/index.html` (chạy
> `npm run build` trước) để đọc bản HTML đầy đủ. Phần còn lại: xuất bản PDF (mục 6) và rà soát
> mở rộng thêm nếu cần.

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
├── book/                     # Nội dung sách (Markdown), 1 file/chương + manifest.json (mục lục)
│   ├── part0-setup/          # Chương 1-5
│   ├── part1-fundamentals/   # Chương 6-11
│   ├── part2-storage/        # Chương 12-15
│   ├── part3-background/     # Chương 16-20
│   ├── part4-networking/     # Chương 21-23
│   ├── part5-testing-security/  # Chương 24-27
│   ├── part6-architecture/   # Chương 28-31
│   ├── part7-interop-hardware/  # Chương 32-37
│   ├── part8-publishing/     # Chương 38-40
│   └── part9-appendix/       # Phụ lục A-B
├── code/                     # Code mẫu — mỗi chương có project Android riêng, chạy độc lập
│   └── ch04-05-hello-world/, ch06-lifecycle-demo/, ... ch40-crash-analytics/
├── tools/                    # Script build HTML (tools/build.js) + CSS (tools/style.css)
├── dist/                     # HTML đã build (không commit — chạy `npm run build` để sinh ra)
└── README.md                 # File kế hoạch này
```

Quy ước: mỗi chương gồm 1 file nội dung trong `book/<part>/chXX-slug.md` + 1 project Android
riêng trong `code/chXX-slug/` (đặt tên khớp số chương), có README riêng hướng dẫn chạy. Một vài
chương liên quan tới giao tiếp liên ứng dụng thật (Chương 32, 34) dùng HAI module Gradle
(`:app`+`:client` hoặc `:server`+`:client`) trong cùng một project, để minh hoạ đúng hai app độc
lập giao tiếp với nhau — không phải mô phỏng giả trong một app.

## 4. Quy ước cho mỗi chương

Mỗi chương cần có:
1. Mục tiêu học (đọc xong làm được gì).
2. Lý thuyết/khái niệm nền, giải thích ngắn gọn, có sơ đồ nếu cần.
3. Ví dụ code từng bước, giải thích tại sao (không chỉ liệt kê code).
4. Project mẫu hoàn chỉnh trong `code/`, build & chạy được trên máy ảo lẫn thiết bị thật.
5. Bài tập/gợi ý mở rộng cuối chương.
6. Lỗi thường gặp + cách khắc phục (đúc kết từ thực tế, không lý thuyết suông).

## 5. Mục lục sách (đã viết xong toàn bộ)

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
- Phụ lục A — Bảng tổng hợp lỗi thường gặp & cách xử lý (tra cứu nhanh theo triệu chứng)
- Phụ lục B — Tài liệu tham khảo & nguồn học thêm

## 6. Pipeline xuất bản (HTML → PDF → EPUB)

**Đã chốt và hoạt động cho HTML**: script Node.js tự viết (`tools/build.js`, dùng `markdown-it`)
thay vì mdBook/Honkit/Pandoc — lý do chọn tự viết thay vì công cụ có sẵn: cần tuỳ biến sâu phần
hiển thị sơ đồ Mermaid có pan/zoom (`svg-pan-zoom`) ngay trong trình duyệt, việc các công cụ có
sẵn không hỗ trợ sẵn ra khỏi hộp.

- `npm run build` (hoặc `node tools/build.js`) đọc `book/manifest.json` + từng file
  `book/<part>/chXX-*.md`, sinh HTML đầy đủ vào `dist/` (sidebar điều hướng, prev/next, syntax
  highlight bằng highlight.js, sơ đồ Mermaid có zoom/pan).
- **PDF**: chưa làm — bước tiếp theo dự kiến là in trực tiếp từ bản HTML đã build bằng Chromium
  headless (Puppeteer, đã có sẵn trong `devDependencies`), giữ nguyên định dạng thay vì đi qua
  Pandoc/LaTeX (tránh phải viết lại phần hiển thị sơ đồ Mermoid/code cho một pipeline riêng).
- **EPUB**: dự kiến ở giai đoạn sau PDF, dùng lại đúng nguồn Markdown, không viết lại nội dung.

## 7. Lộ trình biên soạn (milestones)

1. ✅ Chốt công cụ build (script Node.js tự viết) + dựng khung thư mục `book/`, `code/`.
2. ✅ Viết & code mẫu xong Phần 0 (cài đặt môi trường) — cột mốc "chạy được Hello World".
3. ✅ Viết & code mẫu xong Phần 1–2 (nền tảng + lưu trữ).
4. ✅ Viết & code mẫu xong Phần 3–4 (chạy ngầm + mạng).
5. ✅ Viết & code mẫu xong Phần 5–6 (kiểm thử, bảo mật, kiến trúc nâng cao).
6. ✅ Viết & code mẫu xong Phần 7 (giao tiếp liên ứng dụng, Bluetooth, NFC) — các chương phần
   cứng (35-36) cần thiết bị thật để tự kiểm chứng, đã ghi rõ trong README từng code mẫu.
7. ✅ Viết Phần 8 (xuất bản) + Phụ lục A-B.
8. ⬜ Build bản PDF hoàn chỉnh (Puppeteer in từ HTML), rà soát lần cuối toàn bộ code mẫu.
9. ⬜ Xuất bản EPUB.

## 8. Các quyết định đã chốt trong quá trình viết

- **Ngôn ngữ code mẫu**: Java thuần xuyên suốt toàn bộ 40 chương, **trừ duy nhất Chương 30**
  (Jetpack Compose) — bắt buộc dùng Kotlin vì Compose không có API cho Java. Chương 30 nêu rõ đây
  là ngoại lệ ngay từ đầu, không dạy Kotlin đầy đủ, chỉ giới thiệu khái niệm và so sánh với bản
  XML tương đương ở Chương 7.
- **Phiên bản Android SDK**: `compileSdk`/`targetSdk` 34, `minSdk` 24 — áp dụng nhất quán cho mọi
  code mẫu trong `code/`.
- **Công cụ build tài liệu**: script Node.js tự viết (xem mục 6) thay vì mdBook/Honkit/Pandoc.
