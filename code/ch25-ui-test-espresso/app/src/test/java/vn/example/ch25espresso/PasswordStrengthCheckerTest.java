package vn.example.ch25espresso;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Chạy bằng: nút mũi tên xanh cạnh tên class trong Android Studio, hoặc
 * `./gradlew testDebugUnitTest`. Không cần máy ảo/thiết bị — chạy thẳng trên
 * JVM của máy tính, nên rất nhanh (thường dưới 1 giây cho cả file này).
 */
public class PasswordStrengthCheckerTest {

    @Test
    public void matKhauNgan_luonYeu() {
        assertEquals(PasswordStrengthChecker.Strength.WEAK,
                PasswordStrengthChecker.check("ab1"));
    }

    @Test
    public void chiChuThuong_duDai_vanYeu() {
        assertEquals(PasswordStrengthChecker.Strength.WEAK,
                PasswordStrengthChecker.check("abcdefgh"));
    }

    @Test
    public void coChuHoaVaSo_laMedium() {
        assertEquals(PasswordStrengthChecker.Strength.MEDIUM,
                PasswordStrengthChecker.check("abcDEF123"));
    }

    @Test
    public void duDaiVaDuLoai_laStrong() {
        assertEquals(PasswordStrengthChecker.Strength.STRONG,
                PasswordStrengthChecker.check("Abcdefgh123"));
    }

    @Test
    public void bienChinhXacDoDaiToiThieuCuaStrong() {
        // Đủ 3 loại ký tự nhưng CHƯA đủ 10 ký tự -> chỉ MEDIUM, không phải STRONG.
        // Test kiểu "biên" (boundary) như thế này hay bắt ra lỗi off-by-one nhất.
        assertEquals(PasswordStrengthChecker.Strength.MEDIUM,
                PasswordStrengthChecker.check("Abc12345"));
    }

    @Test
    public void nullNemNgoaiLe() {
        assertThrows(IllegalArgumentException.class,
                () -> PasswordStrengthChecker.check(null));
    }
}
