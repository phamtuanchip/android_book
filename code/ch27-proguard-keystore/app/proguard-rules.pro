# Xem Chương 27 để hiểu đầy đủ lý do từng dòng dưới đây tồn tại.

# --- Model dùng bởi Gson (Post.java) ---
# Gson đọc/ghi field bằng REFLECTION lúc chạy (nó không "gọi getter/setter" như
# code bình thường) — R8 mặc định không biết field nào đang bị dùng theo cách
# này, và sẽ đổi tên chúng thành "a", "b", "c"... Nếu tên field bị đổi mà JSON
# key gốc ("title", "body"...) thì không đổi theo -> Gson map ra toàn field null.
-keepclassmembers class vn.example.ch27security.Post {
    <fields>;
}

# --- Retrofit + Gson cần giữ nguyên thông tin generic type lúc runtime ---
# (vd Call<List<Post>>) để biết chính xác cần parse JSON thành kiểu gì.
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes Exceptions

# --- Các quy tắc khuyến nghị chính thức từ OkHttp/Retrofit cho R8 ---
# (phần lớn thư viện hiện đại tự đóng gói sẵn "consumer rules" riêng trong AAR,
# nên trong thực tế bạn thường KHÔNG cần tự chép tay các dòng dưới — liệt kê ở
# đây để thấy rõ chúng tồn tại và vì sao).
-dontwarn okhttp3.**
-dontwarn retrofit2.**
