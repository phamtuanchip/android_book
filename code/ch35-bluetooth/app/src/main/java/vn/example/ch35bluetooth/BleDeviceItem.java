package vn.example.ch35bluetooth;

/** Một dòng kết quả quét BLE — tách khỏi ScanResult của Android để dễ hiển thị/so sánh. */
public class BleDeviceItem {
    public final String name;
    public final String address;
    public final int rssi;

    public BleDeviceItem(String name, String address, int rssi) {
        this.name = name;
        this.address = address;
        this.rssi = rssi;
    }
}
