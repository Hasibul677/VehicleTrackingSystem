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
import edu.iubat.vts.data.model.User;
import edu.iubat.vts.databinding.FragmentEditStudentBinding;
import edu.iubat.vts.extensions.AppCompatExt;
import edu.iubat.vts.extensions.NavigationExt;
import edu.iubat.vts.extensions.ResourcesExt;

public class EditStudentFragment extends Fragment implements View.OnClickListener {
    private static final String LOG_TAG = EditStudentFragment.class.getSimpleName();

    private FragmentEditStudentBinding viewBinding;
    private NavController navController;

    private EditUserViewModel editUserViewModel;

    private User user;

    public EditStudentFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = EditStudentFragmentArgs.fromBundle(getArguments()).getUser();
        }
        editUserViewModel = new ViewModelProvider(this).get(EditUserViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentEditStudentBinding.inflate(inflater, container, false);
        if (user != null) {
            viewBinding.firstNameEditText.setText(user.getFirstName());
            viewBinding.lastNameEditText.setText(user.getLastName());
            viewBinding.studentIdEditText.setText(user.getStudentId());
        }
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        viewBinding.updateButton.setOnClickListener(this);
        editUserViewModel.isLoadingLiveData().observe(getViewLifecycleOwner(),
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
        editUserViewModel.isSuccessLiveData().observe(getViewLifecycleOwner(),
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isSuccess) {
                        if (isSuccess) {
                            NavigationExt.safeNavigateUp(navController, R.id.nav_edit_student);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.update_button) {
            onClickUpdateAccount();
        }
    }

    private void onClickUpdateAccount() {
        AppCompatExt.hideInputMethod(requireActivity());
        boolean isValidData = true;
        viewBinding.firstNameInputLayout.setError(null);
        viewBinding.lastNameInputLayout.setError(null);
        viewBinding.studentIdTextInputLayout.setError(null);

        Editable firstNameEditable = viewBinding.firstNameEditText.getText();
        Editable lastNameEditable = viewBinding.lastNameEditText.getText();
        Editable studentIdEditable = viewBinding.studentIdEditText.getText();
        if (firstNameEditable == null) {
            viewBinding.firstNameInputLayout.setError(ResourcesExt.getString(requireContext(),
                    R.string.error_first_name_empty));
            isValidData = false;
        } else if (firstNameEditable.toString().isEmpty()) {
            viewBinding.firstNameInputLayout.setError(ResourcesExt.getString(requireContext(),
                    R.string.error_first_name_empty));
            isValidData = false;
        }
        if (lastNameEditable == null) {
            viewBinding.lastNameInputLayout.setError(ResourcesExt.getString(requireContext(),
                    R.string.error_last_name_empty));
            isValidData = false;
        } else if (lastNameEditable.toString().isEmpty()) {
            viewBinding.lastNameInputLayout.setError(ResourcesExt.getString(requireContext(),
                    R.string.error_last_name_empty));
            isValidData = false;
        }

        if (studentIdEditable == null) {
            viewBinding.studentIdTextInputLayout.setError(ResourcesExt.getString(requireContext(),
                    R.string.error_student_id_empty));
            isValidData = false;
        } else if (studentIdEditable.toString().isEmpty()) {
            viewBinding.studentIdTextInputLayout.setError(ResourcesExt.getString(requireContext(),
                    R.string.error_student_id_empty));
            isValidData = false;
        }

        if (isValidData && user != null) {
            user.setFirstName(firstNameEditable.toString());
            user.setLastName(lastNameEditable.toString());
            user.setStudentId(studentIdEditable.toString());
            editUserViewModel.updateUser(user);
        }
    }
}