package edu.iubat.vts.recyclerview.viewholder;

import android.view.View;

import androidx.annotation.NonNull;

import dev.alshakib.rvcompat.viewholder.ViewHolderCompat;
import edu.iubat.vts.databinding.UserListItemBinding;

public class UserListItemViewHolderCompat extends ViewHolderCompat {

    private final UserListItemBinding viewBinding;

    public UserListItemViewHolderCompat(@NonNull View itemView) {
        super(itemView);
        viewBinding = UserListItemBinding.bind(itemView);
        viewBinding.getRoot().setOnClickListener(this);
    }

    public UserListItemBinding getViewBinding() {
        return viewBinding;
    }
}
