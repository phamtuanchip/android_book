package vn.example.ch12prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Gom mọi key/logic đọc-ghi SharedPreferences vào một chỗ duy nhất — tránh rải
 * chuỗi key ("display_name", "font_size"...) khắp nơi trong code, dễ gõ sai và
 * khó đổi tên về sau. Toàn bộ Activity/Fragment khác chỉ gọi qua class này.
 */
public class PrefsManager {

    private static final String PREFS_NAME = "app_settings";

    private static final String KEY_DISPLAY_NAME = "display_name";
    private static final String KEY_NOTIFICATIONS_ENABLED = "notifications_enabled";
    private static final String KEY_FONT_SIZE = "font_size";

    private final SharedPreferences prefs;

    public PrefsManager(Context context) {
        // MODE_PRIVATE: chỉ app của mình đọc/ghi được file này — luôn dùng chế độ này,
        // các MODE khác (MODE_WORLD_READABLE...) đã bị loại bỏ khỏi Android từ lâu vì lý do bảo mật.
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getDisplayName(String defaultValue) {
        return prefs.getString(KEY_DISPLAY_NAME, defaultValue);
    }

    public boolean isNotificationsEnabled() {
        return prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true);
    }

    public int getFontSize(int defaultValue) {
        return prefs.getInt(KEY_FONT_SIZE, defaultValue);
    }

    public void save(String displayName, boolean notificationsEnabled, int fontSize) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_DISPLAY_NAME, displayName);
        editor.putBoolean(KEY_NOTIFICATIONS_ENABLED, notificationsEnabled);
        editor.putInt(KEY_FONT_SIZE, fontSize);
        // apply(): ghi xuống đĩa BẤT ĐỒNG BỘ, trả về ngay lập tức — dùng cho hầu hết
        // trường hợp. commit(): ghi ĐỒNG BỘ, trả về boolean thành công/thất bại, chặn
        // thread gọi nó — chỉ dùng khi cần biết chắc chắn đã ghi xong trước khi làm tiếp.
        editor.apply();
    }

    public void registerOnChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
