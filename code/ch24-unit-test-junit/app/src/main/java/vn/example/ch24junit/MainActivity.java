package vn.example.ch24junit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;

import vn.example.ch24junit.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Activity chỉ GỌI logic đã được test riêng ở PasswordStrengthCheckerTest,
                // không tự chứa nhánh rẽ nào cần test lại ở đây — đây chính là lý do tách
                // logic ra khỏi Activity giúp việc viết Unit test dễ dàng hơn nhiều.
                PasswordStrengthChecker.Strength strength =
                        PasswordStrengthChecker.check(s.toString());
                binding.textStrength.setText(labelFor(strength));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private String labelFor(PasswordStrengthChecker.Strength strength) {
        switch (strength) {
            case STRONG: return getString(R.string.strength_strong);
            case MEDIUM: return getString(R.string.strength_medium);
            default: return getString(R.string.strength_weak);
        }
    }
}
