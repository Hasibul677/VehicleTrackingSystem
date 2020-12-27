package edu.iubat.vts.recyclerview.viewholder;

import android.view.View;

import androidx.annotation.NonNull;

import dev.alshakib.rvcompat.viewholder.ViewHolderCompat;
import edu.iubat.vts.databinding.BusListItemBinding;

public class BusListItemViewHolderCompat extends ViewHolderCompat {

    private final BusListItemBinding viewBinding;

    public BusListItemViewHolderCompat(@NonNull View itemView) {
        super(itemView);
        viewBinding = BusListItemBinding.bind(itemView);
        viewBinding.getRoot().setOnClickListener(this);
    }

    public BusListItemBinding getViewBinding() {
        return viewBinding;
    }
}
