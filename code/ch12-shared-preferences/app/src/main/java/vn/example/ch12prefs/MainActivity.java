package vn.example.ch12prefs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import vn.example.ch12prefs.databinding.ActivityMainBinding;

/**
 * Đóng app hẳn (vuốt khỏi danh sách app gần đây) rồi mở lại — giá trị vẫn còn
 * nguyên, KHÁC với onSaveInstanceState ở Chương 6 (chỉ sống qua configuration
 * change, mất khi app bị đóng hẳn). SharedPreferences ghi xuống đĩa, tồn tại
 * qua mọi lần khởi động lại tiến trình.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "PrefsDemo";
    private static final int FONT_SIZE_OFFSET = 10; // SeekBar chạy 0..20, hiển thị 10..30sp

    private ActivityMainBinding binding;
    private PrefsManager prefsManager;

    private final SharedPreferences.OnSharedPreferenceChangeListener changeListener =
            (sharedPreferences, key) -> Log.d(TAG, "Preference đã đổi: " + key);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefsManager = new PrefsManager(this);
        loadCurrentValues();

        binding.seekFontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.labelFontSize.setText(
                        getString(R.string.label_font_size_value, progress + FONT_SIZE_OFFSET));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        binding.buttonSave.setOnClickListener(v -> {
            String name = binding.editDisplayName.getText().toString().trim();
            boolean notificationsEnabled = binding.switchNotifications.isChecked();
            int fontSize = binding.seekFontSize.getProgress() + FONT_SIZE_OFFSET;

            prefsManager.save(name, notificationsEnabled, fontSize);

            String time = DateFormat.format("HH:mm:ss", System.currentTimeMillis()).toString();
            binding.textStatus.setText(getString(R.string.status_saved_at, time));
        });
    }

    private void loadCurrentValues() {
        binding.editDisplayName.setText(prefsManager.getDisplayName(""));
        binding.switchNotifications.setChecked(prefsManager.isNotificationsEnabled());
        int fontSize = prefsManager.getFontSize(16);
        binding.seekFontSize.setProgress(fontSize - FONT_SIZE_OFFSET);
        binding.labelFontSize.setText(getString(R.string.label_font_size_value, fontSize));
    }

    @Override
    protected void onStart() {
        super.onStart();
        prefsManager.registerOnChangeListener(changeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Luôn hủy đăng ký listener khi Activity không còn hiển thị — đăng ký mà quên
        // hủy là một nguồn rò rỉ bộ nhớ phổ biến (Activity không được giải phóng vì
        // SharedPreferences vẫn giữ tham chiếu tới listener của nó).
        prefsManager.unregisterOnChangeListener(changeListener);
    }
}
