package edu.iubat.vts.fragment.common.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import edu.iubat.vts.R;
import edu.iubat.vts.data.model.User;
import edu.iubat.vts.databinding.FragmentProfileBinding;
import edu.iubat.vts.extensions.ResourcesExt;

public class ProfileFragment extends Fragment {
    private static final String LOG_TAG = ProfileFragment.class.getSimpleName();

    private static final int IMAGE_PICKER_CODE = 103;

    private FragmentProfileBinding viewBinding;
    private NavController navController;
    private ProfileViewModel profileViewModel;

    public ProfileFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentProfileBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        profileViewModel.isLoadingLiveData().observe(getViewLifecycleOwner(),
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
        profileViewModel.getUserLiveData().observe(getViewLifecycleOwner(),
                new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        if (user != null) {
                            viewBinding.nameTextView.setText(user.getName());
                            viewBinding.emailTextView.setText(user.getEmail());
                            Glide.with(requireContext())
                                    .load(user.getDisplayIconPath())
                                    .placeholder(ResourcesExt.createThumbnailFromString(user.getName()))
                                    .centerCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(viewBinding.profileImageView);
                            viewBinding.profileImageView.setVisibility(View.VISIBLE);
                            viewBinding.nameTextView.setVisibility(View.VISIBLE);
                            viewBinding.emailTextView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_update_profile_image) {
            updateProfileImage();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICKER_CODE && data != null) {
            profileViewModel.updateProfileImage(data.getDataString());
        }
    }

    private void updateProfileImage() {
        if (profileViewModel.getUser() != null) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICKER_CODE);
        }
    }
}