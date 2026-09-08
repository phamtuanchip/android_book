# Code mẫu — Chương 9: Fragment & Navigation Component

Mô hình "single-activity": một `MainActivity` chứa `NavHostFragment`, điều hướng giữa
`FirstFragment` và `SecondFragment` qua `nav_graph.xml`, truyền dữ liệu bằng Safe Args.

## Chạy thử

1. Mở project bằng Android Studio, để Gradle sync (plugin Safe Args sẽ sinh code lúc build).
2. Run. Nhập tin nhắn ở màn hình đầu, bấm **Tiếp theo**.
3. Màn hình 2 hiển thị đúng tin nhắn đã nhập — dữ liệu được Safe Args kiểm tra kiểu lúc build.
4. Bấm **Quay lại** hoặc nút Back hệ thống — quay về màn hình đầu, `NavHostFragment` tự quản lý back stack.

## Xem nav graph trực quan

Mở `app/src/main/res/navigation/nav_graph.xml` bằng Android Studio (không phải trình soạn thảo
text) để thấy giao diện kéo-thả trực quan của Navigation Editor.
