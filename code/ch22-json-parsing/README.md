# Code mẫu — Chương 22: Parse JSON với Retrofit + Gson

Cùng API (`jsonplaceholder.typicode.com/posts`) và cùng giao diện với Chương 21, nhưng viết lại
bằng Retrofit + Gson — so sánh trực tiếp lượng code giảm đi bao nhiêu.

## Chạy thử

1. Mở project bằng Android Studio, Run.
2. Bấm **Tải danh sách bài viết** — kết quả giống hệt Chương 21, nhưng đọc `MainActivity.java`
   để thấy không còn `OkHttpClient`/`JSONArray` thủ công nào nữa.
3. So sánh field `Post.authorId` (`@SerializedName("userId")`) với JSON gốc trả về `"userId"` —
   xác nhận Gson tự map đúng dù tên field Java khác tên key JSON.
