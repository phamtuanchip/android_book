package vn.example.ch36nfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.format.DateFormat;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import vn.example.ch36nfc.databinding.ActivityMainBinding;

/**
 * "Foreground Dispatch" — cách CHUẨN để một Activity đang mở giành quyền xử lý
 * sự kiện chạm thẻ NFC TRƯỚC bất kỳ app nào khác (kể cả trước intent-filter
 * tĩnh khai báo trong manifest của chính app này) — xem mục 36.2.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private boolean writeModeArmed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            binding.textNfcStatus.setText(R.string.status_no_nfc_hardware);
            binding.buttonArmWrite.setEnabled(false);
        } else if (!nfcAdapter.isEnabled()) {
            binding.textNfcStatus.setText(R.string.status_nfc_disabled);
        } else {
            binding.textNfcStatus.setText(R.string.status_ready);
        }

        setupForegroundDispatch();

        binding.buttonArmWrite.setOnClickListener(v -> {
            writeModeArmed = true;
            binding.textMode.setText(R.string.mode_write_armed);
            appendLog(getString(R.string.log_write_armed));
        });

        handleIntent(getIntent());
    }

    private void setupForegroundDispatch() {
        int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                ? PendingIntent.FLAG_MUTABLE : 0;
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, flags);

        // Không lọc quá chi tiết — nhận MỌI loại thẻ NFC tới gần (NDEF, thẻ
        // trống, hay công nghệ khác), rồi tự phân loại trong handleIntent().
        intentFilters = new IntentFilter[]{
                new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),
                new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED),
                new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            // Chỉ có tác dụng khi Activity đang ở FOREGROUND — enableForegroundDispatch
            // "giành" mọi sự kiện chạm thẻ về đây, KHÔNG cho hệ thống tự mở app khác
            // (hoặc hộp thoại "Open with") trong lúc màn hình này đang hiển thị.
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            // BẮT BUỘC hủy khi rời màn hình — quên dòng này khiến Activity tiếp
            // tục "giành" sự kiện NFC dù không còn hiển thị, chặn app/Activity
            // khác nhận được sự kiện chạm thẻ của chính chúng.
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (!NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                && !NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                && !NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            return; // Activity mở bình thường từ icon, không phải do chạm thẻ
        }

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) {
            return;
        }

        if (writeModeArmed) {
            writeTextToTag(tag, binding.editWriteText.getText().toString());
            writeModeArmed = false;
            binding.textMode.setText(R.string.mode_read);
        } else {
            readTag(intent, tag);
        }
    }

    private void readTag(Intent intent, Tag tag) {
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMessages == null || rawMessages.length == 0) {
            appendLog(getString(R.string.log_tag_no_ndef_format, tagIdHex(tag)));
            return;
        }
        NdefMessage message = (NdefMessage) rawMessages[0];
        for (NdefRecord record : message.getRecords()) {
            String text = decodeTextRecord(record);
            appendLog(getString(R.string.log_read_format, tagIdHex(tag), text));
        }
    }

    /** Ghi một NdefRecord kiểu Text (chuẩn RTD_TEXT) vào thẻ — hoạt động cả với
     * thẻ đã có định dạng NDEF (Ndef) lẫn thẻ trống mới (NdefFormatable). */
    private void writeTextToTag(Tag tag, String text) {
        NdefMessage message = new NdefMessage(new NdefRecord[]{createTextRecord(text)});
        Ndef ndef = Ndef.get(tag);
        try {
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    appendLog(getString(R.string.log_write_readonly));
                    return;
                }
                ndef.writeNdefMessage(message);
                ndef.close();
                appendLog(getString(R.string.log_write_success_format, text));
            } else {
                NdefFormatable formatable = NdefFormatable.get(tag);
                if (formatable == null) {
                    appendLog(getString(R.string.log_write_unsupported));
                    return;
                }
                formatable.connect();
                formatable.format(message);
                formatable.close();
                appendLog(getString(R.string.log_write_success_format, text));
            }
        } catch (IOException | FormatException e) {
            appendLog(getString(R.string.log_write_error_format, e.getMessage()));
        }
    }

    private NdefRecord createTextRecord(String text) {
        byte[] langBytes = "vi".getBytes(StandardCharsets.US_ASCII);
        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        byte[] payload = new byte[1 + langBytes.length + textBytes.length];
        payload[0] = (byte) langBytes.length; // byte đầu: độ dài mã ngôn ngữ
        System.arraycopy(langBytes, 0, payload, 1, langBytes.length);
        System.arraycopy(textBytes, 0, payload, 1 + langBytes.length, textBytes.length);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);
    }

    private String decodeTextRecord(NdefRecord record) {
        byte[] payload = record.getPayload();
        int langLength = payload[0] & 0x3F;
        return new String(payload, 1 + langLength, payload.length - 1 - langLength, StandardCharsets.UTF_8);
    }

    private String tagIdHex(Tag tag) {
        StringBuilder sb = new StringBuilder();
        for (byte b : tag.getId()) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    private void appendLog(String line) {
        String time = DateFormat.format("HH:mm:ss", System.currentTimeMillis()).toString();
        binding.textLog.append("[" + time + "] " + line + "\n");
    }
}
