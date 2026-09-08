# Code mẫu — Chương 14: File storage (internal vs external)

Ghi/đọc cùng một nội dung vào hai nơi khác nhau: bộ nhớ trong (`getFilesDir()`) và thư mục
riêng của app trên bộ nhớ ngoài (`getExternalFilesDir(null)`) — không cần xin quyền nào cả.

## Chạy thử

1. Mở project bằng Android Studio, Run.
2. Nhập nội dung, bấm **Lưu vào bộ nhớ TRONG** — xem Toast hiển thị đường dẫn file thật.
3. Bấm **Lưu vào bộ nhớ NGOÀI** — so sánh đường dẫn khác với bước 2.
4. Bấm **Đọc lại cả hai nơi** — xác nhận cả hai đều đọc lại đúng nội dung đã lưu.
5. (Tuỳ chọn) Dùng `adb shell run-as vn.example.ch14filestorage ls files` để tận mắt thấy file
   `note.txt` nằm trong thư mục riêng của app trên thiết bị.
