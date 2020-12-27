package edu.iubat.vts.fragment.admin.list.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import edu.iubat.vts.R;
import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.data.model.User;
import edu.iubat.vts.databinding.FragmentBusDetailsDialogBinding;

public class BusDetailsDialogFragment extends BottomSheetDialogFragment {
    private static final String LOG_TAG = BusDetailsDialogFragment.class.getSimpleName();

    private FragmentBusDetailsDialogBinding viewBinding;
    private BusDetailsViewModel busDetailsViewModel;

    private Bus bus;

    public BusDetailsDialogFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bus = BusDetailsDialogFragmentArgs.fromBundle(getArguments()).getBus();
        }
        busDetailsViewModel = new ViewModelProvider(this).get(BusDetailsViewModel.class);
        if (bus != null) {
            busDetailsViewModel.loadUser(bus);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentBusDetailsDialogBinding.inflate(inflater, container, false);
        if (bus.getConductorName() != null && !bus.getConductorName().isEmpty()) {
            viewBinding.conductorNameTextView.setText(requireContext().getString(R.string.text_conductor_name, bus.getConductorName()));
            viewBinding.conductorNameTextView.setVisibility(View.VISIBLE);
        }
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        busDetailsViewModel.isLoadingLiveData().observe(getViewLifecycleOwner(),
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isLoading) {
                        if (isLoading) {
                            viewBinding.progressHorizontal.setVisibility(View.VISIBLE);
                        } else {
                            viewBinding.progressHorizontal.setVisibility(View.GONE);
                        }
                    }
                });

        busDetailsViewModel.getUserLiveData().observe(getViewLifecycleOwner(),
                new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        if (user != null) {
                            viewBinding.driverNameTextView.setText(requireContext().getString(R.string.text_driver_name, user.getName()));
                            viewBinding.driverNameTextView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}