package edu.iubat.vts.activity.splash;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import edu.iubat.vts.R;
import edu.iubat.vts.activity.admin.AdminActivity;
import edu.iubat.vts.activity.driver.DriverActivity;
import edu.iubat.vts.activity.signin.SignInActivity;
import edu.iubat.vts.activity.student.StudentActivity;
import edu.iubat.vts.data.model.User;
import edu.iubat.vts.data.repository.DataRepository;
import edu.iubat.vts.databinding.ActivitySplashBinding;
import edu.iubat.vts.extensions.PermissionExt;

public class SplashActivity extends AppCompatActivity {
    private static final String LOG_TAG = SplashActivity.class.getSimpleName();

    private ActivitySplashBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        if (!checkAndRequestPermissions()) {
            updateUi();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionExt.PERMISSION_REQUEST_MULTIPLE_PERMISSIONS) {
            if (grantResults.length > 0) {
                boolean isAllGranted = true;
                for (int i = 0; i < grantResults.length; ++i) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        isAllGranted = false;
                        break;
                    }
                }
                if (isAllGranted) {
                    updateUi();
                } else {
                    viewBinding.progressCircular.setVisibility(View.GONE);
                    Toast.makeText(this, R.string.toast_location_required, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void updateUi() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
            return;
        }

        DataRepository dataRepository = DataRepository.getInstance(this);
        dataRepository.loadUser();
        dataRepository.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    if (user.isAdmin()) {
                        dataRepository.loadAndOrganizeForAdmin();
                        Intent intent = new Intent(SplashActivity.this, AdminActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                    } else if (user.isDriver()) {
                        Intent intent = new Intent(SplashActivity.this, DriverActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                    } else if (user.isStudent()) {
                        Intent intent = new Intent(SplashActivity.this, StudentActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(SplashActivity.this, R.string.error_something_went_wrong, Toast.LENGTH_SHORT).show();
                    viewBinding.progressCircular.setVisibility(View.GONE);
                }
            }
        });
    }

    private boolean checkAndRequestPermissions() {
        List<String> permissionList = new ArrayList<>();
        if (!PermissionExt.hasAccessFineLocationPermission(this)) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!permissionList.isEmpty()) {
            PermissionExt.requestPermissions(this, permissionList, PermissionExt.PERMISSION_REQUEST_MULTIPLE_PERMISSIONS);
            return true;
        }
        return false;
    }
}