package vn.example.ch25espresso;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test — chạy TRÊN máy ảo/thiết bị thật (khác Unit test cục bộ ở
 * Chương 24), cài app thật lên, thao tác lên UI thật y như người dùng. Nằm ở
 * app/src/androidTest/java/, KHÔNG PHẢI app/src/test/java/.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityEspressoTest {

    // Tự động mở MainActivity trước MỖI @Test, tự đóng lại sau khi test xong —
    // không cần gọi startActivity() thủ công.
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void goMatKhauYeu_hienThiNhanYeu() {
        onView(withId(R.id.editPassword))
                .perform(typeText("abc"), closeSoftKeyboard());

        onView(withId(R.id.textStrength))
                .check(matches(withText("Yếu")));
    }

    @Test
    public void goMatKhauManh_hienThiNhanManh() {
        onView(withId(R.id.editPassword))
                .perform(typeText("Abcdefgh123"), closeSoftKeyboard());

        onView(withId(R.id.textStrength))
                .check(matches(withText("Mạnh")));
    }

    @Test
    public void xoaHetChu_quayVeYeu() {
        onView(withId(R.id.editPassword))
                .perform(typeText("Abcdefgh123"), closeSoftKeyboard());
        onView(withId(R.id.textStrength)).check(matches(withText("Mạnh")));

        onView(withId(R.id.editPassword))
                .perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.textStrength)).check(matches(withText("Yếu")));
    }
}
