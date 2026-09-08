package vn.example.ch19async;

/** Việc tính toán thuần tuý, không biết gì về Android/UI — dễ test độc lập. */
public final class PrimeCounter {

    public interface ProgressCallback {
        void onProgress(int percent);
    }

    /**
     * Đếm số nguyên tố từ 2 tới limit. Kiểm tra Thread.currentThread().isInterrupted()
     * mỗi vòng lặp ngoài — đây là cách CHỦ ĐỘNG hợp tác để có thể bị huỷ giữa chừng.
     * Executor.Future.cancel(true) chỉ ĐẶT cờ interrupted, không tự dừng vòng lặp
     * đang chạy nếu code không tự kiểm tra cờ này.
     */
    public static int countPrimes(int limit, ProgressCallback callback) {
        int count = 0;
        for (int n = 2; n <= limit; n++) {
            if (Thread.currentThread().isInterrupted()) {
                return count;
            }
            if (isPrime(n)) {
                count++;
            }
            if (n % Math.max(limit / 100, 1) == 0) {
                callback.onProgress((int) (n * 100L / limit));
            }
        }
        return count;
    }

    private static boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }
        for (int i = 2; (long) i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    private PrimeCounter() {
    }
}
