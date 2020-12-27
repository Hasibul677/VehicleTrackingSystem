package edu.iubat.vts.recyclerview.viewholder;

import android.view.View;

import androidx.annotation.NonNull;

import dev.alshakib.rvcompat.viewholder.ViewHolderCompat;
import edu.iubat.vts.databinding.UserListItemOverflowBinding;

public class UserListItemOverflowViewHolderCompat extends ViewHolderCompat {

    private final UserListItemOverflowBinding viewBinding;

    public UserListItemOverflowViewHolderCompat(@NonNull View itemView) {
        super(itemView);
        viewBinding = UserListItemOverflowBinding.bind(itemView);
        viewBinding.getRoot().setOnClickListener(this);
        viewBinding.overflowButton.setOnClickListener(this);
    }

    public UserListItemOverflowBinding getViewBinding() {
        return viewBinding;
    }
}
