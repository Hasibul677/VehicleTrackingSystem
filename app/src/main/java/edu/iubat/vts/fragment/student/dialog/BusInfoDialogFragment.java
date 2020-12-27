package edu.iubat.vts.fragment.student.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import edu.iubat.vts.R;
import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.databinding.FragmentBusInfoDialogBinding;

public class BusInfoDialogFragment extends BottomSheetDialogFragment {
    private static final String LOG_TAG = BusInfoDialogFragment.class.getSimpleName();

    private FragmentBusInfoDialogBinding viewBinding;

    private Bus bus;

    public BusInfoDialogFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bus = BusInfoDialogFragmentArgs.fromBundle(getArguments()).getBus();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentBusInfoDialogBinding.inflate(inflater, container, false);
        viewBinding.conductorNameTextView.setText(requireContext().getString(R.string.text_conductor_name, bus.getConductorName()));
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}