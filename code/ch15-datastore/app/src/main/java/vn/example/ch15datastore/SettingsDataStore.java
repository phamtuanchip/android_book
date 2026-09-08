package vn.example.ch15datastore;

import android.content.Context;

import androidx.datastore.rxjava3.RxDataStore;
import androidx.datastore.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.preferences.core.Preferences;

public final class SettingsDataStore {

    private static volatile RxDataStore<Preferences> instance;

    public static RxDataStore<Preferences> getInstance(Context context) {
        if (instance == null) {
            synchronized (SettingsDataStore.class) {
                if (instance == null) {
                    // Tên file thật sự nằm ở "<files-dir>/datastore/settings.preferences_pb" —
                    // định dạng nhị phân (protobuf), không phải XML dễ đọc như SharedPreferences.
                    instance = new RxPreferenceDataStoreBuilder(context.getApplicationContext(), "settings").build();
                }
            }
        }
        return instance;
    }

    private SettingsDataStore() {
    }
}
