package edu.iubat.vts.activity.map;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.iubat.vts.R;
import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.data.model.BusLocation;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String LOG_TAG = GoogleMapActivity.class.getSimpleName();

    public static final String EXTRA_BUS = "extra_bus";

    private GoogleMapViewModel googleMapViewModel;

    private GoogleMap googleMap;
    private Bus bus;

    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                bus = bundle.getParcelable(EXTRA_BUS);
            }
        }

        if (bus != null) {
            googleMapViewModel = new ViewModelProvider(this).get(GoogleMapViewModel.class);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.google_maps);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
            googleMapViewModel.subscribeToUpdates(bus);
            googleMapViewModel.getBusLocationLiveData().observe(this,
                    new Observer<BusLocation>() {
                        @Override
                        public void onChanged(BusLocation busLocation) {
                            if (busLocation != null) {
                                updateMarker(busLocation);
                            }
                        }
                    });
        } else {
            Toast.makeText(this, R.string.error_something_went_wrong, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private void updateMarker(@NonNull BusLocation busLocation) {
        if (googleMap != null) {
            googleMap.setMaxZoomPreference(18);
            LatLng latLng = new LatLng(busLocation.getLatitude(), busLocation.getLongitude());
            if (marker == null) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                markerOptions.title(bus.getBusNumber());
                marker = googleMap.addMarker(markerOptions);
            } else {
                marker.setPosition(latLng);
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(marker.getPosition());
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
        }
    }
}