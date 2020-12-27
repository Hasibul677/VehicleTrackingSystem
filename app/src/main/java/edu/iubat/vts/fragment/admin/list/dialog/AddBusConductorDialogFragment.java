package edu.iubat.vts.fragment.admin.list.dialog;

import android.os.Bundle;
import android.text.Editable;
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
import edu.iubat.vts.databinding.FragmentAddBusConductorDialogBinding;
import edu.iubat.vts.extensions.AppCompatExt;
import edu.iubat.vts.extensions.ResourcesExt;

public class AddBusConductorDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String LOG_TAG = AddBusConductorDialogFragment.class.getSimpleName();

    private FragmentAddBusConductorDialogBinding viewBinding;
    private AddBusConductorViewModel addBusConductorViewModel;

    private Bus bus;

    public AddBusConductorDialogFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bus = AddBusConductorDialogFragmentArgs.fromBundle(getArguments()).getBus();
        }
        addBusConductorViewModel = new ViewModelProvider(this).get(AddBusConductorViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentAddBusConductorDialogBinding.inflate(inflater, container, false);
        viewBinding.headerTextView.setText(requireContext().getString(R.string.header_add_bus_conductor, bus.getBusNumber()));
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewBinding.addConductorButton.setOnClickListener(this);
        addBusConductorViewModel.isLoadingLiveData().observe(getViewLifecycleOwner(),
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isLoading) {
                        if (isLoading) {
                            viewBinding.progressHorizontal.setVisibility(View.VISIBLE);
                            viewBinding.addConductorButton.setEnabled(false);
                        } else {
                            viewBinding.progressHorizontal.setVisibility(View.GONE);
                            viewBinding.addConductorButton.setEnabled(true);
                        }
                    }
                });

        addBusConductorViewModel.isSuccessLiveData().observe(getViewLifecycleOwner(),
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
    public void onClick(View v) {
        if (v.getId() == R.id.add_conductor_button) {
            onClickAddConductor();
        }
    }

    private void onClickAddConductor() {
        AppCompatExt.hideInputMethod(requireActivity());
        boolean isValidData = true;
        viewBinding.conductorNameInputLayout.setError(null);

        Editable conductorNameEditable = viewBinding.conductorNameEditText.getText();
        if (conductorNameEditable == null) {
            viewBinding.conductorNameInputLayout.setError(ResourcesExt.getString(requireContext(),
                    R.string.error_conductor_name_empty));
            isValidData = false;
        } else if (conductorNameEditable.toString().isEmpty()) {
            viewBinding.conductorNameInputLayout.setError(ResourcesExt.getString(requireContext(),
                    R.string.error_conductor_name_empty));
            isValidData = false;
        }

        if (isValidData) {
            bus.setConductorName(conductorNameEditable.toString());
            addBusConductorViewModel.updateBus(bus);
        }
    }
}