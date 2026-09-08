package vn.example.ch15datastore;

import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.core.Preferences;

/** Gom key lại một chỗ — cùng lý do với PrefsManager ở Chương 12. */
public final class SettingsKeys {
    public static final Preferences.Key<String> DISPLAY_NAME = PreferencesKeys.stringKey("display_name");
    public static final Preferences.Key<Boolean> NOTIFICATIONS_ENABLED = PreferencesKeys.booleanKey("notifications_enabled");

    private SettingsKeys() {
    }
}
