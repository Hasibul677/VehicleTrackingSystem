package edu.iubat.vts.recyclerview.viewholder;

import android.view.View;

import androidx.annotation.NonNull;

import dev.alshakib.rvcompat.viewholder.ViewHolderCompat;
import edu.iubat.vts.databinding.BusListItemOverflowBinding;

public class BusListItemOverflowViewHolderCompat extends ViewHolderCompat {

    private final BusListItemOverflowBinding viewBinding;

    public BusListItemOverflowViewHolderCompat(@NonNull View itemView) {
        super(itemView);
        viewBinding = BusListItemOverflowBinding.bind(itemView);
        viewBinding.getRoot().setOnClickListener(this);
        viewBinding.overflowButton.setOnClickListener(this);
    }

    public BusListItemOverflowBinding getViewBinding() {
        return viewBinding;
    }
}
