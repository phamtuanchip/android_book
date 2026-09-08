package vn.example.ch08intentnav;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import vn.example.ch08intentnav.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {

    private ActivitySecondBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String message = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);
        binding.textReceivedMessage.setText(
                (message == null || message.isEmpty())
                        ? getString(R.string.no_message_received)
                        : getString(R.string.received_format, message));

        binding.buttonReply.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra(MainActivity.EXTRA_REPLY, getString(R.string.default_reply));
            setResult(RESULT_OK, result);
            finish();
        });
    }
}
