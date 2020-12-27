package edu.iubat.vts.activity.map;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.data.model.BusLocation;

public class GoogleMapViewModel extends AndroidViewModel {
    private static final String LOG_TAG = GoogleMapViewModel.class.getSimpleName();

    private final LocationEventListener locationEventListener;
    private final FirebaseDatabase firebaseDatabase;

    private final MutableLiveData<BusLocation> busLocationMutableLiveData;

    private DatabaseReference databaseReference;

    public GoogleMapViewModel(@NonNull Application application) {
        super(application);
        this.locationEventListener = new LocationEventListener();
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.busLocationMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<BusLocation> getBusLocationLiveData() {
        return busLocationMutableLiveData;
    }

    public void subscribeToUpdates(@NonNull Bus bus) {
        if (databaseReference != null) {
            databaseReference.removeEventListener(locationEventListener);
        }
        databaseReference = firebaseDatabase.getReference(bus.getDocumentId());
        databaseReference.addValueEventListener(locationEventListener);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (databaseReference != null) {
            databaseReference.removeEventListener(locationEventListener);
        }
    }

    private class LocationEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            BusLocation busLocation = snapshot.getValue(BusLocation.class);
            if (busLocation != null) {
                busLocationMutableLiveData.setValue(busLocation);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    }
}
