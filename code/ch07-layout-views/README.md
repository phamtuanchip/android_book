# Code mẫu — Chương 7: Layout & Views

Một màn hình duy nhất gộp nhiều loại View phổ biến (`EditText`, `Button`, `TextView`,
`CheckBox`, `SeekBar`) trong `ConstraintLayout`, dùng **ViewBinding** thay vì `findViewById`.

## Chạy thử

1. Mở project bằng Android Studio, Run.
2. Gõ tên vào ô nhập, bấm **Chào** — quan sát `TextView` cập nhật.
3. Bấm nút Chào khi ô nhập trống — quan sát thông báo khác.
4. Kéo thanh trượt độ tuổi — quan sát giá trị cập nhật theo thời gian thực.

## Vì sao dùng ViewBinding?

`buildFeatures { viewBinding true }` trong `app/build.gradle` bật tính năng này — Gradle tự sinh
class `ActivityMainBinding` từ `activity_main.xml` mỗi lần build, với một field cho mỗi view có
`android:id`. So với `findViewById`, cách này bắt lỗi sai id/sai kiểu ngay lúc biên dịch thay vì
crash lúc chạy.
