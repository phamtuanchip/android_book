# Code mẫu — Chương 39: Đưa ứng dụng lên Google Play Console

Chương này không có logic Android chạy trên thiết bị — thay vào đó là một cấu trúc thư mục quản
lý nội dung cửa hàng (kiểu Fastlane) kèm script kiểm tra độ dài ký tự **đúng giới hạn thật của
Google Play Console**, để tránh mất công dán rồi bị form báo lỗi.

```
ch39-google-play-console/
├── store-listing/vi-VN/
│   ├── title.txt                (tối đa 30 ký tự)
│   ├── short_description.txt    (tối đa 80 ký tự)
│   └── full_description.txt     (tối đa 4000 ký tự)
├── release-notes/vi-VN/
│   └── default.txt              (tối đa 500 ký tự — "Thông tin mới")
└── tools/validate-store-listing.js
```

## Chạy thử

```bash
node tools/validate-store-listing.js
```

Kết quả in ra từng file kèm số ký tự hiện tại/giới hạn cho phép, và mã thoát khác 0 nếu có mục
không hợp lệ — dùng được trực tiếp trong CI/CD để chặn commit nếu ai đó lỡ viết mô tả quá dài.

## Chạy thử — tự phá vỡ giới hạn để xem script bắt lỗi

```bash
node -e "require('fs').writeFileSync('store-listing/vi-VN/title.txt', 'Một tiêu đề cực kỳ dài vượt quá ba mươi ký tự cho phép')"
node tools/validate-store-listing.js
```

Xác nhận script báo "QUÁ DÀI" đúng file vừa sửa, sau đó khôi phục lại nội dung gốc bằng git
(`git checkout -- store-listing/vi-VN/title.txt`) nếu bạn đã thử.
