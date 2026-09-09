package vn.example.ch35bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Cài đặt mẫu "Bluetooth chat" kinh điển của Android: một thread chờ kết nối
 * đến (server), một thread chủ động kết nối đi (client), một thread đọc/ghi dữ
 * liệu sau khi đã kết nối — cả ba đều là I/O CHẶN (blocking), nên BẮT BUỘC chạy
 * trên thread riêng, không bao giờ trên main thread (cùng nguyên tắc từ Chương 19).
 */
public class BluetoothChatManager {

    // UUID chuẩn cho Serial Port Profile (SPP) — hầu hết thiết bị Bluetooth
    // Classic hỗ trợ truyền dữ liệu nối tiếp đều dùng chung UUID này.
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String SOCKET_NAME = "ch35bluetooth-chat";

    public interface Listener {
        void onConnected(String deviceName);
        void onMessageReceived(String message);
        void onError(String message);
        void onDisconnected();
    }

    private final BluetoothAdapter adapter;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private Listener listener;

    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;

    public BluetoothChatManager(BluetoothAdapter adapter) {
        this.adapter = adapter;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @SuppressLint("MissingPermission") // Đã kiểm tra quyền ở Activity trước khi gọi (mục 35.3)
    public void startServer() {
        stopAll();
        acceptThread = new AcceptThread();
        acceptThread.start();
    }

    @SuppressLint("MissingPermission")
    public void connectTo(BluetoothDevice device) {
        stopAll();
        connectThread = new ConnectThread(device);
        connectThread.start();
    }

    public void sendMessage(String message) {
        if (connectedThread != null) {
            connectedThread.write(message);
        }
    }

    public void stopAll() {
        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
    }

    private void manageConnectedSocket(BluetoothSocket socket, String remoteName) {
        connectedThread = new ConnectedThread(socket);
        connectedThread.start();
        mainHandler.post(() -> {
            if (listener != null) listener.onConnected(remoteName);
        });
    }

    /** Chạy trên thiết bị ĐÓNG VAI SERVER — chờ thiết bị khác chủ động kết nối tới. */
    private class AcceptThread extends Thread {
        private final BluetoothServerSocket serverSocket;

        @SuppressLint("MissingPermission")
        AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = adapter.listenUsingRfcommWithServiceRecord(SOCKET_NAME, SPP_UUID);
            } catch (IOException e) {
                mainHandler.post(() -> {
                    if (listener != null) listener.onError("Không mở được server socket: " + e.getMessage());
                });
            }
            serverSocket = tmp;
        }

        @Override
        @SuppressLint("MissingPermission")
        public void run() {
            if (serverSocket == null) return;
            try {
                // accept() CHẶN THREAD NÀY tới khi có thiết bị khác kết nối tới —
                // đây chính là lý do bắt buộc chạy trên thread riêng.
                BluetoothSocket socket = serverSocket.accept();
                serverSocket.close();
                manageConnectedSocket(socket, socket.getRemoteDevice().getName());
            } catch (IOException e) {
                mainHandler.post(() -> {
                    if (listener != null) listener.onError("Server dừng: " + e.getMessage());
                });
            }
        }

        void cancel() {
            try {
                if (serverSocket != null) serverSocket.close();
            } catch (IOException ignored) {
            }
        }
    }

    /** Chạy trên thiết bị ĐÓNG VAI CLIENT — chủ động kết nối tới một thiết bị đã ghép đôi. */
    private class ConnectThread extends Thread {
        private final BluetoothSocket socket;
        private final BluetoothDevice device;

        @SuppressLint("MissingPermission")
        ConnectThread(BluetoothDevice device) {
            this.device = device;
            BluetoothSocket tmp = null;
            try {
                tmp = device.createRfcommSocketToServiceRecord(SPP_UUID);
            } catch (IOException e) {
                mainHandler.post(() -> {
                    if (listener != null) listener.onError("Không tạo được socket: " + e.getMessage());
                });
            }
            socket = tmp;
        }

        @Override
        @SuppressLint("MissingPermission")
        public void run() {
            if (socket == null) return;
            adapter.cancelDiscovery(); // đang quét (nếu có) sẽ làm chậm/đứt kết nối
            try {
                // connect() cũng CHẶN THREAD tới khi thành công hoặc thất bại hẳn.
                socket.connect();
                manageConnectedSocket(socket, device.getName());
            } catch (IOException e) {
                mainHandler.post(() -> {
                    if (listener != null) listener.onError("Kết nối thất bại: " + e.getMessage());
                });
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }

        void cancel() {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    /** Chạy sau khi ĐÃ kết nối (dù là server hay client) — đọc/ghi dữ liệu qua lại. */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket socket;
        private final InputStream input;
        private final OutputStream output;
        private volatile boolean running = true;

        ConnectedThread(BluetoothSocket socket) {
            this.socket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException ignored) {
            }
            input = tmpIn;
            output = tmpOut;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            while (running) {
                try {
                    // read() CHẶN THREAD tới khi có dữ liệu mới, hoặc kết nối bị đóng.
                    int bytes = input.read(buffer);
                    String message = new String(buffer, 0, bytes, StandardCharsets.UTF_8);
                    mainHandler.post(() -> {
                        if (listener != null) listener.onMessageReceived(message);
                    });
                } catch (IOException e) {
                    running = false;
                    mainHandler.post(() -> {
                        if (listener != null) listener.onDisconnected();
                    });
                }
            }
        }

        void write(String message) {
            try {
                output.write(message.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                mainHandler.post(() -> {
                    if (listener != null) listener.onError("Gửi thất bại: " + e.getMessage());
                });
            }
        }

        void cancel() {
            running = false;
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }
}
