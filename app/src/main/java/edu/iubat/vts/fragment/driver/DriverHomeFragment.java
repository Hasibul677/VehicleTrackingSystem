package edu.iubat.vts.fragment.driver;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import dev.alshakib.rvcompat.viewholder.ViewHolderCompat;
import edu.iubat.vts.R;
import edu.iubat.vts.activity.map.GoogleMapActivity;
import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.databinding.FragmentDriverHomeBinding;
import edu.iubat.vts.service.TrackerService;

import static android.content.Context.LOCATION_SERVICE;

public class DriverHomeFragment extends Fragment implements View.OnClickListener,
        ViewHolderCompat.OnItemClickListener, TrackerService.TrackerCallback {
    private static final String LOG_TAG = DriverHomeFragment.class.getSimpleName();

    private FragmentDriverHomeBinding viewBinding;
    private NavController navController;
    private DriverViewModel driverViewModel;

    public DriverHomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driverViewModel = new ViewModelProvider(this).get(DriverViewModel.class);
        driverViewModel.getBusListAdapterCompat().setOnItemClickListener(this);
        TrackerService.setTrackerCallback(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentDriverHomeBinding.inflate(inflater, container, false);
        if (TrackerService.isTracking) {
            viewBinding.trackerStatusTextView.setText(requireContext().getString(R.string.text_tracking_now, TrackerService.trackingNowBus.getBusNumber()));
            showTrackingNowView();
        } else {
            hideTrackingNowView();
        }
        viewBinding.stopTrackingButton.setOnClickListener(this);
        viewBinding.openMapButton.setOnClickListener(this);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        viewBinding.recyclerView.setHasFixedSize(true);
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        viewBinding.recyclerView.setAdapter(driverViewModel.getBusListAdapterCompat());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.stop_tracking_button) {
            onClickStopTracking();
        } else if (v.getId() == R.id.open_map_button) {
            if (TrackerService.trackingNowBus != null) {
                Intent intent = new Intent(requireContext(), GoogleMapActivity.class);
                intent.putExtra(GoogleMapActivity.EXTRA_BUS, TrackerService.trackingNowBus);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onItemClick(@NonNull View v, int viewType, int position) {
        Bus bus = driverViewModel.getBusListAdapterCompat().getCurrentList().get(position);
        if (bus != null) {
            if (v.getId() == R.id.overflow_button) {
                // TODO: Show some details or menu here
            } else {
                if (TrackerService.isTracking) {
                    Toast.makeText(requireContext(), R.string.toast_stop_current_tracking, Toast.LENGTH_SHORT).show();
                } else {
                    onClickStartTracking(bus);
                }
            }
        }
    }

    private void onClickStartTracking(@NonNull Bus bus) {
        LocationManager lm = (LocationManager) requireContext().getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(requireContext(), R.string.toast_enable_location, Toast.LENGTH_SHORT).show();
            return;
        }
        TrackerService.startForegroundService(requireContext(), bus);
        viewBinding.trackerStatusTextView.setText(requireContext().getString(R.string.text_tracking_now, bus.getBusNumber()));
        showTrackingNowView();
    }

    private void onClickStopTracking() {
        Intent intent = new Intent();
        intent.setAction(TrackerService.ACTION_STOP_TRACKING);
        requireContext().sendBroadcast(intent);
        hideTrackingNowView();
    }

    private void hideTrackingNowView() {
        viewBinding.trackerStatusTextView.setVisibility(View.GONE);
        viewBinding.openMapButton.setVisibility(View.GONE);
        viewBinding.stopTrackingButton.setVisibility(View.GONE);
    }

    private void showTrackingNowView() {
        viewBinding.trackerStatusTextView.setVisibility(View.VISIBLE);
        viewBinding.openMapButton.setVisibility(View.VISIBLE);
        viewBinding.stopTrackingButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTrackerStatusUpdate(boolean isActive) {
        if (!isActive) {
            hideTrackingNowView();
        }
    }
}