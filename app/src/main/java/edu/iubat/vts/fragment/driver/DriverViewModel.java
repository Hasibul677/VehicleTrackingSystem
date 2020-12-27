package edu.iubat.vts.fragment.driver;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.data.model.User;
import edu.iubat.vts.data.repository.DataRepository;
import edu.iubat.vts.extensions.FirebaseExt;
import edu.iubat.vts.recyclerview.adapter.BusListAdapterCompat;

public class DriverViewModel extends AndroidViewModel {
    private static final String LOG_TAG = DriverViewModel.class.getSimpleName();

    private final FirebaseFirestore firestore;

    private final MutableLiveData<Boolean> isLoadingMutableLiveData;

    private final BusListAdapterCompat busListAdapterCompat;
    private final MutableLiveData<List<Bus>> busListMutableLiveData;
    private final Observer<List<Bus>> busListObserver = new Observer<List<Bus>>() {
        @Override
        public void onChanged(List<Bus> buses) {
            busListAdapterCompat.submitList(buses);
        }
    };

    private final LiveData<User> userLiveData;
    private final Observer<User> userObserver = new Observer<User>() {
        @Override
        public void onChanged(User user) {
            if (user != null) {
                loadBus(user);
            }
        }
    };

    public DriverViewModel(@NonNull Application application) {
        super(application);
        this.firestore = FirebaseFirestore.getInstance();
        this.isLoadingMutableLiveData = new MutableLiveData<>();
        this.busListAdapterCompat = new BusListAdapterCompat();
        this.busListMutableLiveData = new MutableLiveData<>();
        this.busListMutableLiveData.observeForever(busListObserver);

        DataRepository dataRepository = DataRepository
                .getInstance(application.getApplicationContext());
        this.userLiveData = dataRepository.getUserLiveData();
        this.userLiveData.observeForever(userObserver);
    }

    public LiveData<Boolean> isLoadingLiveData() {
        return isLoadingMutableLiveData;
    }

    private void loadBus(@NonNull User driver) {
        firestore.collection(FirebaseExt.BUS_COLLECTION)
                .whereEqualTo("driverDocumentId", driver.getDocumentId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                List<Bus> busList = new ArrayList<>();
                                for (DocumentSnapshot snapshot : querySnapshot) {
                                    Bus bus = snapshot.toObject(Bus.class);
                                    if (bus != null) {
                                        busList.add(bus);
                                    }
                                }
                                busListMutableLiveData.setValue(busList);
                            }
                        } else {
                            Exception exception = task.getException();
                            if (exception != null) {
                                Log.e(LOG_TAG, exception.getMessage());
                            }
                        }
                        isLoadingMutableLiveData.setValue(false);
                    }
                });
    }

    @NonNull
    public BusListAdapterCompat getBusListAdapterCompat() {
        return busListAdapterCompat;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (busListMutableLiveData.hasObservers()) {
            busListMutableLiveData.removeObserver(busListObserver);
        }
        if (userLiveData.hasObservers()) {
            userLiveData.removeObserver(userObserver);
        }
    }
}
