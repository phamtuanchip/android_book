// AIDL chỉ hỗ trợ một tập kiểu dữ liệu giới hạn: kiểu nguyên thuỷ (int, boolean,
// String...), List/Map của chúng, và các class Parcelable tự khai báo — không
// truyền được object Java tuỳ ý như gọi hàm bình thường trong cùng tiến trình.
package vn.example.ch34aidlserver;

interface ICalculatorService {
    int add(int a, int b);
    int multiply(int a, int b);
    int getCallCount();
}
