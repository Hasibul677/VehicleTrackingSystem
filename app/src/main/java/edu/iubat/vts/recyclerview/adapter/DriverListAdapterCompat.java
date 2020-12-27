package edu.iubat.vts.recyclerview.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import dev.alshakib.rvcompat.adapter.ListAdapterCompat;
import edu.iubat.vts.R;
import edu.iubat.vts.data.model.User;
import edu.iubat.vts.extensions.ResourcesExt;
import edu.iubat.vts.recyclerview.viewholder.UserListItemViewHolderCompat;

public class DriverListAdapterCompat extends ListAdapterCompat<User, UserListItemViewHolderCompat> {

    public DriverListAdapterCompat() {
        super(new AsyncDifferConfig.Builder<>(new User.DiffUtilCallback()).build());
    }

    @NonNull
    @Override
    public UserListItemViewHolderCompat onCreateViewHolderCompat(@NonNull ViewGroup parent, int viewType) {
        return new UserListItemViewHolderCompat(inflateView(R.layout.user_list_item, parent, false));
    }

    @Override
    public void onBindViewHolderCompat(@NonNull UserListItemViewHolderCompat holder, int position) {
        if (getItem(position) != null) {
            Glide.with(holder.getViewBinding().imageView.getContext())
                    .load(getItem(position).getDisplayIconPath())
                    .placeholder(ResourcesExt.createThumbnailFromString(getItem(position).getName()))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.getViewBinding().imageView);
            holder.getViewBinding().titleTextView.setText(getItem(position).getName());
            holder.getViewBinding().descriptionTextView.setText(getItem(position).getEmail());
        }
    }
}
