package edu.iubat.vts.fragment.admin.list.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import dev.alshakib.rvcompat.viewholder.ViewHolderCompat;
import edu.iubat.vts.R;
import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.data.model.User;
import edu.iubat.vts.databinding.FragmentAddBusDriverDialogBinding;

public class AddBusDriverDialogFragment extends BottomSheetDialogFragment implements ViewHolderCompat.OnItemClickListener {
    private static final String LOG_TAG = AddBusDriverDialogFragment.class.getSimpleName();

    private FragmentAddBusDriverDialogBinding viewBinding;
    private AddBusDriverViewModel addBusDriverViewModel;

    private Bus bus;

    public AddBusDriverDialogFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBusDriverViewModel = new ViewModelProvider(this).get(AddBusDriverViewModel.class);
        if (getArguments() != null) {
            bus = AddBusDriverDialogFragmentArgs.fromBundle(getArguments())
                    .getBus();
        }
        addBusDriverViewModel.getDriverListAdapterCompat().setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentAddBusDriverDialogBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewBinding.recyclerView.setHasFixedSize(true);
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        viewBinding.recyclerView.setAdapter(addBusDriverViewModel.getDriverListAdapterCompat());

        addBusDriverViewModel.isLoadingLiveData().observe(getViewLifecycleOwner(),
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isLoading) {
                        if (isLoading) {
                            viewBinding.progressHorizontal.setVisibility(View.VISIBLE);
                            viewBinding.recyclerView.setEnabled(false);
                        } else {
                            viewBinding.progressHorizontal.setVisibility(View.GONE);
                            viewBinding.recyclerView.setEnabled(true);
                        }
                    }
                });
        addBusDriverViewModel.isSuccessLiveData().observe(getViewLifecycleOwner(),
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isSuccess) {
                        if (isSuccess) {
                            dismiss();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(@NonNull View v, int viewType, int position) {
        if (v.getId() == R.id.container) {
            User user = addBusDriverViewModel.getDriverListAdapterCompat().getCurrentList().get(position);
            if (user != null) {
                onClickUpdateBus(user);
            }
        }
    }

    private void onClickUpdateBus(@NonNull User user) {
        bus.setDriverDocumentId(user.getDocumentId());
        addBusDriverViewModel.updateBus(bus);
    }
}