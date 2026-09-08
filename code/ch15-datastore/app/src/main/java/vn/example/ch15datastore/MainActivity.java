package vn.example.ch15datastore;

import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava3.RxDataStore;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

import vn.example.ch15datastore.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private RxDataStore<Preferences> dataStore;
    // Mọi subscribe() trả về một Disposable — CompositeDisposable gom lại để huỷ
    // TẤT CẢ cùng lúc trong onStop(), tránh callback chạy vào Activity đã chết
    // (tương đương lý do phải unregisterOnChangeListener ở Chương 12).
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dataStore = SettingsDataStore.getInstance(this);

        binding.buttonSave.setOnClickListener(v -> save());
    }

    @Override
    protected void onStart() {
        super.onStart();
        // dataStore.data() là một Flowable — tự phát giá trị hiện tại ngay khi subscribe,
        // rồi phát tiếp mỗi khi dữ liệu đổi (kể cả đổi từ nơi khác trong app).
        disposables.add(dataStore.data()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::applyToUi, throwable ->
                        binding.textStatus.setText(getString(R.string.read_error, throwable.getMessage()))));
    }

    private void applyToUi(Preferences prefs) {
        String name = prefs.get(SettingsKeys.DISPLAY_NAME);
        Boolean notifications = prefs.get(SettingsKeys.NOTIFICATIONS_ENABLED);
        binding.editDisplayName.setText(name != null ? name : "");
        binding.switchNotifications.setChecked(notifications != null ? notifications : true);
    }

    private void save() {
        String name = binding.editDisplayName.getText().toString().trim();
        boolean notificationsEnabled = binding.switchNotifications.isChecked();

        // updateDataAsync: đọc-sửa-ghi TRỌN VẸN TRONG MỘT GIAO DỊCH (transactional) —
        // an toàn hơn "đọc rồi ghi" 2 bước riêng lẻ như SharedPreferences.Editor khi có
        // nhiều nơi cùng ghi đồng thời. Trả về Single — PHẢI subscribe() thì mới thực
        // sự chạy, do bản chất "lazy" của RxJava.
        Single<Preferences> update = dataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePrefs = prefsIn.toMutablePreferences();
            mutablePrefs.set(SettingsKeys.DISPLAY_NAME, name);
            mutablePrefs.set(SettingsKeys.NOTIFICATIONS_ENABLED, notificationsEnabled);
            return Single.just(mutablePrefs);
        });

        disposables.add(update
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        prefs -> binding.textStatus.setText(getString(R.string.saved_at,
                                DateFormat.format("HH:mm:ss", System.currentTimeMillis()))),
                        throwable -> binding.textStatus.setText(getString(R.string.save_error, throwable.getMessage()))));
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposables.clear();
    }
}
