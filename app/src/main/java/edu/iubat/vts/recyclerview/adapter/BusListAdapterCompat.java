package edu.iubat.vts.recyclerview.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;

import dev.alshakib.rvcompat.adapter.ListAdapterCompat;
import edu.iubat.vts.R;
import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.recyclerview.viewholder.BusListItemViewHolderCompat;

public class BusListAdapterCompat extends ListAdapterCompat<Bus, BusListItemViewHolderCompat> {
    private static final String LOG_TAG = BusListAdapterCompat.class.getSimpleName();

    public BusListAdapterCompat() {
        super(new AsyncDifferConfig.Builder<>(new Bus.DiffUtilCallback()).build());
    }

    @NonNull
    @Override
    public BusListItemViewHolderCompat onCreateViewHolderCompat(@NonNull ViewGroup parent, int viewType) {
        return new BusListItemViewHolderCompat(inflateView(R.layout.bus_list_item, parent, false));
    }

    @Override
    public void onBindViewHolderCompat(@NonNull BusListItemViewHolderCompat holder, int position) {
        if (getItem(position) != null) {
            holder.getViewBinding().titleTextView.setText(getItem(position).getBusNumber());
            holder.getViewBinding().descriptionTextView.setText(getItem(position).getLicenseNumber());
        }
    }
}
