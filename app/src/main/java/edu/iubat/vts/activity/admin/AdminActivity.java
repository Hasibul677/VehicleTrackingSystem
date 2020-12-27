package edu.iubat.vts.activity.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import edu.iubat.vts.R;
import edu.iubat.vts.activity.signin.SignInActivity;
import edu.iubat.vts.data.model.User;
import edu.iubat.vts.data.repository.DataRepository;
import edu.iubat.vts.databinding.ActivityAdminBinding;
import edu.iubat.vts.databinding.DrawerHeaderBinding;
import edu.iubat.vts.extensions.AppCompatExt;
import edu.iubat.vts.extensions.ResourcesExt;

public class AdminActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener,
        NavigationView.OnNavigationItemSelectedListener {
    private static final String LOG_TAG = AdminActivity.class.getSimpleName();

    private ActivityAdminBinding viewBinding;
    private DrawerHeaderBinding drawerHeaderBinding;
    private NavController navController;

    private AppBarConfiguration appBarConfiguration;

    private DataRepository dataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityAdminBinding.inflate(getLayoutInflater());
        drawerHeaderBinding = DrawerHeaderBinding.bind(viewBinding.navView.getHeaderView(0));
        setContentView(viewBinding.getRoot());
        setSupportActionBar(viewBinding.materialToolbar);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_admin_home)
                .setOpenableLayout(viewBinding.drawerLayout)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener(this);
        viewBinding.navView.setNavigationItemSelectedListener(this);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        dataRepository = DataRepository.getInstance(this);

        updateDrawerHeader();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        AppCompatExt.hideInputMethod(this);
        viewBinding.drawerLayout.close();
        viewBinding.appBarLayout.setExpanded(true);
        switch (destination.getId()) {
            case R.id.nav_admin_home: {
                viewBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                break;
            }
            default: {
                viewBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sign_out) {
            FirebaseAuth.getInstance().signOut();
            DataRepository.destroy();
            Intent intent = new Intent(this, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        } else if (item.getItemId() == R.id.action_profile) {
            navController.navigate(R.id.nav_admin_profile);
        } else if (item.getItemId() == R.id.action_add_bus) {
            navController.navigate(R.id.nav_add_bus);
        } else if (item.getItemId() == R.id.action_add_driver) {
            navController.navigate(R.id.nav_add_driver);
        } else if (item.getItemId() == R.id.action_add_student) {
            navController.navigate(R.id.nav_add_student);
        }
        return false;
    }

    private void updateDrawerHeader() {
        dataRepository.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    Glide.with(drawerHeaderBinding.displayIcon.getContext())
                            .load(user.getDisplayIconPath())
                            .placeholder(ResourcesExt.createThumbnailFromString(user.getName()))
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(drawerHeaderBinding.displayIcon);
                    drawerHeaderBinding.name.setText(user.getName());
                }
            }
        });
    }
}