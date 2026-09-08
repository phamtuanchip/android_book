# Code mẫu — Chương 10: RecyclerView & Adapter

Danh sách ghi chú hiển thị qua `RecyclerView` + `ListAdapter`/`DiffUtil`, có nút thêm ghi chú
mới để quan sát animation cập nhật danh sách.

## Chạy thử

1. Mở project bằng Android Studio, Run.
2. Bấm vào một ghi chú — quan sát Toast hiển thị đúng tiêu đề.
3. Bấm **Thêm ghi chú ngẫu nhiên** nhiều lần — quan sát item mới trượt vào đầu danh sách có
   animation, không phải "nhảy" (vẽ lại toàn bộ) như khi dùng `notifyDataSetChanged()`.
