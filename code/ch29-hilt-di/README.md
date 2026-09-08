# Code mẫu — Chương 29: Dependency Injection với Hilt

Cùng chức năng với Chương 28, nhưng thay mọi singleton tự viết tay (`AppDatabase.getInstance()`,
`ApiClient.getApi()`) bằng Hilt: `AppModule` khai báo cách tạo, `@Inject` đánh dấu nơi cần dùng.

## Chạy thử

1. Mở project bằng Android Studio, để Gradle sync xong (annotation processor của Hilt/Room chạy
   lúc build — lần build đầu có thể chậm hơn các chương trước).
2. Run — hành vi giống hệt Chương 28 (không có gì mới về UI/UX, chỉ khác cách các đối tượng
   được khởi tạo/nối dây bên dưới).
3. So sánh trực tiếp `PostRepository.java`, `PostViewModel.java`, `MainActivity.java` với bản
   Chương 28 — chú ý cách `AppDatabase`/`JsonPlaceholderApi` không còn được gọi trực tiếp ở bất
   kỳ đâu ngoài `AppModule.java`.
