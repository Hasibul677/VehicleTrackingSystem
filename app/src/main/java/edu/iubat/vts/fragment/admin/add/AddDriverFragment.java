package edu.iubat.vts.fragment.admin.add;

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
import edu.iubat.vts.databinding.FragmentAddDriverBinding;
import edu.iubat.vts.extensions.AppCompatExt;
import edu.iubat.vts.extensions.JavaExt;
import edu.iubat.vts.extensions.NavigationExt;
import edu.iubat.vts.extensions.ResourcesExt;

public class AddDriverFragment extends Fragment implements View.OnClickListener {
    private static final String LOG_TAG = AddDriverFragment.class.getSimpleName();

    private FragmentAddDriverBinding viewBinding;
    private NavController navController;

    private AddDriverViewModel addDriverViewModel;

    public AddDriverFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addDriverViewModel = new ViewModelProvider(this).get(AddDriverViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentAddDriverBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        viewBinding.createAccountButton.setOnClickListener(this);
        addDriverViewModel.isLoadingLiveData().observe(getViewLifecycleOwner(),
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isLoading) {
                        if (isLoading) {
                            viewBinding.progressHorizontal.setVisibility(View.VISIBLE);
                            viewBinding.createAccountButton.setEnabled(false);
                        } else {
                            viewBinding.progressHorizontal.setVisibility(View.GONE);
                            viewBinding.createAccountButton.setEnabled(true);
                        }
                    }
                });
        addDriverViewModel.isSuccessLiveData().observe(getViewLifecycleOwner(),
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isSuccess) {
                        if (isSuccess) {
                            NavigationExt.safeNavigateUp(navController, R.id.nav_add_driver);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.create_account_button) {
            onClickCreateAccount();
        }
    }

    private void onClickCreateAccount() {
        AppCompatExt.hideInputMethod(requireActivity());
        boolean isValidData = true;
        viewBinding.firstNameInputLayout.setError(null);
        viewBinding.lastNameInputLayout.setError(null);
        viewBinding.emailTextInputLayout.setError(null);

        Editable firstNameEditable = viewBinding.firstNameEditText.getText();
        Editable lastNameEditable = viewBinding.lastNameEditText.getText();
        Editable emailEditable = viewBinding.emailEditText.getText();
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
        if (emailEditable == null) {
            viewBinding.emailTextInputLayout.setError(ResourcesExt.getString(requireContext(),
                    R.string.error_email_empty));
            isValidData = false;
        } else if (emailEditable.toString().isEmpty()) {
            viewBinding.emailTextInputLayout.setError(ResourcesExt.getString(requireContext(),
                    R.string.error_email_empty));
            isValidData = false;
        } else if (!JavaExt.isValidEmail(emailEditable.toString())) {
            viewBinding.emailTextInputLayout.setError(ResourcesExt.getString(requireContext(),
                    R.string.error_invalid_email));
            isValidData = false;
        }

        if (isValidData) {
            User user = new User();
            user.setFirstName(firstNameEditable.toString());
            user.setLastName(lastNameEditable.toString());
            user.setEmail(emailEditable.toString());
            user.setDriver(true);
            addDriverViewModel.createAccount(user);
        }
    }
}