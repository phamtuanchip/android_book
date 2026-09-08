# Code mẫu — Chương 11: Resource, đa ngôn ngữ, đa kích thước màn hình

Không có logic Java đặc biệt — toàn bộ nội dung minh hoạ cách Android tự chọn đúng resource
theo **qualifier**, hoàn toàn khai báo qua tên thư mục `res/`.

```
res/
├── values/              # mặc định (tiếng Việt trong demo này)
├── values-en/            # ghi đè khi ngôn ngữ hệ thống là English
├── values-night/         # ghi đè màu khi bật chế độ tối (Dark theme)
└── values-sw600dp/       # ghi đè kích thước khi màn hình rộng ≥ 600dp (tablet)
```

## Chạy thử

1. Mở project bằng Android Studio, Run — mặc định thấy giao diện tiếng Việt, nền sáng.
2. Đổi ngôn ngữ hệ thống của máy ảo/thiết bị sang **English** (Settings → System → Languages) →
   mở lại app → thấy chữ chuyển sang tiếng Anh, không cần build lại APK.
3. Bật **Dark theme** của hệ thống → thấy nền và màu chữ đổi theo `values-night/colors.xml`.
4. Nếu có máy ảo màn hình lớn (Pixel Tablet, hoặc AVD tự tạo với màn hình ≥ 600dp) → thấy
   padding và cỡ chữ lớn hơn hẳn, theo `values-sw600dp/dimens.xml`.
