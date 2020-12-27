package edu.iubat.vts.recyclerview.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;

import dev.alshakib.rvcompat.adapter.ListAdapterCompat;
import edu.iubat.vts.R;
import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.recyclerview.viewholder.BusListItemOverflowViewHolderCompat;

public class AdminBusListAdapterCompat extends ListAdapterCompat<Bus, BusListItemOverflowViewHolderCompat> {
    private static final String LOG_TAG = AdminBusListAdapterCompat.class.getSimpleName();

    public AdminBusListAdapterCompat() {
        super(new AsyncDifferConfig.Builder<>(new Bus.DiffUtilCallback()).build());
    }

    @NonNull
    @Override
    public BusListItemOverflowViewHolderCompat onCreateViewHolderCompat(@NonNull ViewGroup parent, int viewType) {
        return new BusListItemOverflowViewHolderCompat(inflateView(R.layout.bus_list_item_overflow, parent, false));
    }

    @Override
    public void onBindViewHolderCompat(@NonNull BusListItemOverflowViewHolderCompat holder, int position) {
        if (getItem(position) != null) {
            holder.getViewBinding().titleTextView.setText(getItem(position).getBusNumber());
            holder.getViewBinding().descriptionTextView.setText(getItem(position).getLicenseNumber());
        }
    }
}
