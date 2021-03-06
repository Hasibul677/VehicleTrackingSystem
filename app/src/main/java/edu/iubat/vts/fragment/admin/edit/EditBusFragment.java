package edu.iubat.vts.fragment.admin.edit;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import edu.iubat.vts.R;
import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.databinding.FragmentEditBusBinding;
import edu.iubat.vts.extensions.AppCompatExt;
import edu.iubat.vts.extensions.NavigationExt;
import edu.iubat.vts.extensions.ResourcesExt;

public class EditBusFragment extends Fragment implements View.OnClickListener {
    private static final String LOG_TAG = EditBusFragment.class.getSimpleName();

    private FragmentEditBusBinding viewBinding;
    private NavController navController;
    private EditBusViewModel editBusViewModel;

    private Bus bus;

    public EditBusFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bus = EditBusFragmentArgs.fromBundle(getArguments()).getBus();
        }
        editBusViewModel = new ViewModelProvider(this).get(EditBusViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentEditBusBinding.inflate(inflater, container, false);
        if (bus != null) {
            viewBinding.busNumberEditText.setText(bus.getBusNumber());
            viewBinding.busLicenseNumberEditText.setText(bus.getLicenseNumber());
        }
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        viewBinding.updateButton.setOnClickListener(this);

        editBusViewModel.isLoadingLiveData().observe(getViewLifecycleOwner(),
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isLoading) {
                        if (isLoading) {
                            viewBinding.progressHorizontal.setVisibility(View.VISIBLE);
                            viewBinding.updateButton.setEnabled(false);
                        } else {
                            viewBinding.progressHorizontal.setVisibility(View.GONE);
                            viewBinding.updateButton.setEnabled(true);
                        }
                    }
                });

        editBusViewModel.isSuccessLiveData().observe(getViewLifecycleOwner(),
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isSuccess) {
                        if (isSuccess) {
                            NavigationExt.safeNavigateUp(navController, R.id.nav_edit_bus);
                        }
                    }
                });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.update_button) {
            onClickUpdateBus();
        }
    }

    private void onClickUpdateBus() {
        AppCompatExt.hideInputMethod(requireActivity());

        boolean isValidData = true;
        viewBinding.busNumberInputLayout.setError(null);
        viewBinding.busLicenseNumberInputLayout.setError(null);

        Editable busNumberEditable = viewBinding.busNumberEditText.getText();
        Editable busLicenseNumberEditable = viewBinding.busLicenseNumberEditText.getText();
        if (busNumberEditable == null) {
            viewBinding.busLicenseNumberInputLayout.setError(ResourcesExt.getString(requireContext(),
                    R.string.error_bus_number_empty));
            isValidData = false;
        } else if (busNumberEditable.toString().isEmpty()) {
            viewBinding.busNumberInputLayout.setError(ResourcesExt.getString(requireContext(),
                    R.string.error_bus_number_empty));
            isValidData = false;
        }
        if (busLicenseNumberEditable == null) {
            viewBinding.busLicenseNumberInputLayout.setError(ResourcesExt.getString(requireContext(),
                    R.string.error_bus_license_number_empty));
            isValidData = false;
        } else if (busLicenseNumberEditable.toString().isEmpty()) {
            viewBinding.busLicenseNumberInputLayout.setError(ResourcesExt.getString(requireContext(),
                    R.string.error_bus_license_number_empty));
            isValidData = false;
        }

        if (isValidData && bus != null) {
            bus.setBusNumber(busNumberEditable.toString());
            bus.setLicenseNumber(busLicenseNumberEditable.toString());
            editBusViewModel.updateBus(bus);
        }
    }
}