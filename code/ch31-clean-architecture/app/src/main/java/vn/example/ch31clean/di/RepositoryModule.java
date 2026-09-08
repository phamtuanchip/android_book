package vn.example.ch31clean.di;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import vn.example.ch31clean.data.PostRepositoryImpl;
import vn.example.ch31clean.domain.PostRepository;

/**
 * @Binds — cú pháp NGẮN GỌN dành riêng cho trường hợp "chỉ cần khai báo interface
 * X được cài đặt bằng class Y", khác @Provides (Chương 29) vốn dùng khi cần
 * VIẾT CODE thật để tạo ra đối tượng. Đây chính là chỗ Hilt được cấu hình để nối
 * đúng PostRepository (domain, interface) với PostRepositoryImpl (data, class thật).
 */
@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {

    @Binds
    public abstract PostRepository bindPostRepository(PostRepositoryImpl impl);
}
