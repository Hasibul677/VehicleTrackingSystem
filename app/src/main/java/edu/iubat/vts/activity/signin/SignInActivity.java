package edu.iubat.vts.activity.signin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import edu.iubat.vts.R;
import edu.iubat.vts.activity.splash.SplashActivity;
import edu.iubat.vts.databinding.ActivitySignInBinding;
import edu.iubat.vts.extensions.AppCompatExt;
import edu.iubat.vts.extensions.JavaExt;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = SignInActivity.class.getSimpleName();

    private ActivitySignInBinding viewBinding;
    private SignInViewModel signInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        viewBinding.signInButton.setOnClickListener(this);

        signInViewModel.isLoadingLiveData().observe(this,
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isLoading) {
                        if (isLoading) {
                            viewBinding.progressHorizontal.setVisibility(View.VISIBLE);
                            viewBinding.signInButton.setEnabled(false);
                        } else {
                            viewBinding.progressHorizontal.setVisibility(View.GONE);
                            viewBinding.signInButton.setEnabled(true);
                        }
                    }
                });

        signInViewModel.isSuccessLiveData().observe(this,
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isSuccess) {
                        if (isSuccess) {
                            Intent intent = new Intent(SignInActivity.this, SplashActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            finish();
                        } else {
                            Snackbar.make(viewBinding.getRoot(), getString(R.string.error_email_password_did_not_match),
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppCompatExt.hideInputMethod(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_button) {
            onClickSignIn();
        }
    }

    private void onClickSignIn() {
        AppCompatExt.hideInputMethod(this);
        boolean isValidData = true;
        viewBinding.emailTextInputLayout.setError(null);
        viewBinding.passwordTextInputLayout.setError(null);
        Editable emailEditable = viewBinding.emailEditText.getText();
        Editable passwordEditable = viewBinding.passwordEditText.getText();
        if (emailEditable == null) {
            viewBinding.emailTextInputLayout.setError(getString(R.string.error_email_empty));
            isValidData = false;
        } else if (emailEditable.toString().isEmpty()) {
            viewBinding.emailTextInputLayout.setError(getString(R.string.error_email_empty));
            isValidData = false;
        } else if (!JavaExt.isValidEmail(emailEditable.toString())) {
            viewBinding.emailTextInputLayout.setError(getString(R.string.error_invalid_email));
            isValidData = false;
        }
        if (passwordEditable == null) {
            viewBinding.passwordTextInputLayout.setError(getString(R.string.error_password_empty));
            isValidData = false;
        } else if (passwordEditable.toString().isEmpty()) {
            viewBinding.passwordTextInputLayout.setError(getString(R.string.error_password_empty));
            isValidData = false;
        }
        if (isValidData) {
            String email = emailEditable.toString();
            String password = passwordEditable.toString();
            signInViewModel.signIn(email, password);
        }
    }
}