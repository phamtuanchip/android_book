package vn.example.ch07layoutviews;

import android.os.Bundle;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import vn.example.ch07layoutviews.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ViewBinding sinh ra class ActivityMainBinding từ activity_main.xml lúc build —
        // truy cập view qua binding.<id> có kiểm tra kiểu tại compile-time, không cần
        // findViewById() + ép kiểu thủ công, và không bao giờ trả về null nếu id tồn tại.
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonGreet.setOnClickListener(v -> {
            String name = binding.editName.getText().toString().trim();
            if (name.isEmpty()) {
                binding.textResult.setText(R.string.result_empty_name);
            } else {
                binding.textResult.setText(getString(R.string.result_greeting, name));
            }
        });

        binding.seekAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.textAgeValue.setText(getString(R.string.age_value, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}
