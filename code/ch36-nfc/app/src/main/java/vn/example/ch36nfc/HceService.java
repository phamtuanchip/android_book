package vn.example.ch36nfc;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

/**
 * Host Card Emulation (HCE): điện thoại "giả làm" một thẻ contactless — đây là
 * cơ chế đứng sau Google Wallet, thẻ giao thông NFC ảo... Khác mục 36.2-36.4
 * (điện thoại chủ động ĐỌC thẻ), ở đây điện thoại BỊ ĐỘNG chờ một đầu đọc NFC
 * bên ngoài (không phải điện thoại khác) gửi lệnh APDU tới.
 *
 * LƯU Ý: không có "máy ảo" hay dễ dàng tự test HCE bằng hai điện thoại thường —
 * cần một đầu đọc thẻ NFC (POS, đầu đọc kiểm soát ra vào...) đã cấu hình đúng
 * AID để gửi lệnh khớp. Code này ĐÚNG VÀ BIÊN DỊCH ĐƯỢC, nhưng mang tính minh
 * hoạ cơ chế hơn là một bài thực hành tự kiểm chứng được ngay tại nhà.
 */
public class HceService extends HostApduService {

    private static final String TAG = "HceService";

    // Lệnh SELECT AID chuẩn ISO 7816-4 mà đầu đọc gửi ĐẦU TIÊN để chọn đúng
    // "ứng dụng thẻ" — phải khớp AID khai báo trong res/xml/apduservice.xml.
    private static final byte[] SELECT_APDU_HEADER = {
            (byte) 0x00, (byte) 0xA4, (byte) 0x04, (byte) 0x00
    };

    // "9000" — mã trạng thái ISO 7816-4 nghĩa là "thành công", theo sau dữ liệu
    // phản hồi thật (ở đây trả một chuỗi cố định để minh hoạ).
    private static final byte[] SUCCESS_RESPONSE = {
            'H', 'e', 'l', 'l', 'o', (byte) 0x90, (byte) 0x00
    };
    private static final byte[] UNKNOWN_COMMAND = {(byte) 0x6F, (byte) 0x00};

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        // Được gọi MỖI KHI đầu đọc gửi một lệnh APDU tới — phải trả lời NGAY,
        // không có thời gian để làm việc nặng (tương tự tinh thần "callback
        // phải nhanh" đã nhắc ở onPause Chương 6).
        Log.d(TAG, "Nhận APDU: " + bytesToHex(commandApdu));

        if (startsWith(commandApdu, SELECT_APDU_HEADER)) {
            return SUCCESS_RESPONSE;
        }
        return UNKNOWN_COMMAND;
    }

    @Override
    public void onDeactivated(int reason) {
        // reason: DEACTIVATION_LINK_LOSS (mất kết nối, rời khỏi vùng NFC) hoặc
        // DEACTIVATION_DESELECTED (đầu đọc chủ động chọn "ứng dụng thẻ" khác).
        Log.d(TAG, "HCE ngừng hoạt động, lý do: " + reason);
    }

    private boolean startsWith(byte[] array, byte[] prefix) {
        if (array.length < prefix.length) return false;
        for (int i = 0; i < prefix.length; i++) {
            if (array[i] != prefix[i]) return false;
        }
        return true;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
