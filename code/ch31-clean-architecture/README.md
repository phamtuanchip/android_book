# Code mẫu — Chương 31: Clean Architecture cơ bản

Cùng chức năng với Chương 28/29, tổ chức lại thành 3 tầng theo package: `domain/` (thuần Java,
không phụ thuộc Android), `data/` (Room + Retrofit thật), `presentation/` (Activity + ViewModel
+ Adapter). `di/` chứa cấu hình Hilt nối các tầng lại với nhau.

```
vn.example.ch31clean/
├── domain/           Post, PostRepository (interface), GetPostsUseCase, RefreshPostsUseCase
├── data/             PostEntity, PostDao, AppDatabase, JsonPlaceholderApi, PostRepositoryImpl, PostMapper
├── presentation/      MainActivity, PostViewModel, PostAdapter
└── di/               AppModule, RepositoryModule
```

## Chạy thử

1. Mở project bằng Android Studio, Run — hành vi giống hệt Chương 28/29.
2. Mở `domain/PostRepository.java` — xác nhận đây chỉ là interface, không import bất kỳ thứ gì
   từ `androidx.room` hay `retrofit2`.
3. Mở `data/PostRepositoryImpl.java` — đây mới là nơi thật sự "biết" Room/Retrofit tồn tại.
4. Thử (chỉ để quan sát, không cần sửa thật) tưởng tượng thay `data/PostRepositoryImpl` bằng một
   cài đặt hoàn toàn khác (ví dụ đọc từ file tĩnh có sẵn trong app để demo, không cần mạng) — vì
   `domain/` và `presentation/` chỉ biết `PostRepository` (interface), lẽ ra không cần sửa gì ở
   hai tầng đó.
