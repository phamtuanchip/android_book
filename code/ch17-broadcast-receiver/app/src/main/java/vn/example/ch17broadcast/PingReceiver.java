package vn.example.ch17broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PingReceiver extends BroadcastReceiver {

    public static final String ACTION_PING = "vn.example.ch17broadcast.ACTION_PING";
    public static final String EXTRA_SEQUENCE = "extra_sequence";

    public interface Listener {
        void onPingReceived(int sequence);
    }

    private final Listener listener;

    public PingReceiver(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int sequence = intent.getIntExtra(EXTRA_SEQUENCE, -1);
        listener.onPingReceived(sequence);
    }
}
