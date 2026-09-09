package vn.example.ch35bluetooth;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import vn.example.ch35bluetooth.databinding.ItemDeviceBinding;

public class BleDeviceAdapter extends ListAdapter<BleDeviceItem, BleDeviceAdapter.ViewHolder> {

    public BleDeviceAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<BleDeviceItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<BleDeviceItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull BleDeviceItem oldItem, @NonNull BleDeviceItem newItem) {
                    return oldItem.address.equals(newItem.address);
                }

                @Override
                public boolean areContentsTheSame(@NonNull BleDeviceItem oldItem, @NonNull BleDeviceItem newItem) {
                    return oldItem.rssi == newItem.rssi && oldItem.name.equals(newItem.name);
                }
            };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDeviceBinding binding = ItemDeviceBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BleDeviceItem item = getItem(position);
        holder.binding.textName.setText(item.name);
        holder.binding.textAddress.setText(
                holder.itemView.getContext().getString(R.string.ble_item_format, item.address, item.rssi));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemDeviceBinding binding;

        ViewHolder(ItemDeviceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
