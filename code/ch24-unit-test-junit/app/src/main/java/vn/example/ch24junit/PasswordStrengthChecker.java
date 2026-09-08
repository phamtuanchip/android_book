package vn.example.ch24junit;

/**
 * CỐ Ý không import bất kỳ thứ gì từ android.* — đây là điều kiện để class này
 * chạy được trong Unit Test cục bộ (JVM thường, không cần emulator/thiết bị,
 * xem mục 24.1). Business logic tách khỏi Android framework càng nhiều, càng
 * dễ test.
 */
public final class PasswordStrengthChecker {

    public enum Strength { WEAK, MEDIUM, STRONG }

    public static Strength check(String password) {
        if (password == null) {
            throw new IllegalArgumentException("password không được null");
        }
        if (password.length() < 6) {
            return Strength.WEAK;
        }

        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        int variety = (hasDigit ? 1 : 0) + (hasUpper ? 1 : 0) + (hasLower ? 1 : 0);

        if (password.length() >= 10 && variety == 3) {
            return Strength.STRONG;
        }
        if (variety >= 2) {
            return Strength.MEDIUM;
        }
        return Strength.WEAK;
    }

    private PasswordStrengthChecker() {
    }
}
