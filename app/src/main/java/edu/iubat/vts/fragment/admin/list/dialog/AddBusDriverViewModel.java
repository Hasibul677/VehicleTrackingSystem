package edu.iubat.vts.fragment.admin.list.dialog;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.List;

import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.data.model.User;
import edu.iubat.vts.data.repository.DataRepository;
import edu.iubat.vts.extensions.FirebaseExt;
import edu.iubat.vts.recyclerview.adapter.DriverListAdapterCompat;

public class AddBusDriverViewModel extends AndroidViewModel {
    private static final String LOG_TAG = AddBusDriverViewModel.class.getSimpleName();

    private final FirebaseFirestore fireStore;
    private final MutableLiveData<Boolean> isLoadingMutableLiveData;
    private final MutableLiveData<Boolean> isSuccessMutableLiveData;

    private final DriverListAdapterCompat driverListAdapterCompat;

    private final LiveData<List<User>> driverListLiveData;
    private final Observer<List<User>> driverListObserver = new Observer<List<User>>() {
        @Override
        public void onChanged(List<User> users) {
            driverListAdapterCompat.submitList(users);
        }
    };

    public AddBusDriverViewModel(@NonNull Application application) {
        super(application);
        DataRepository dataRepository = DataRepository.getInstance(application.getApplicationContext());
        this.driverListAdapterCompat = new DriverListAdapterCompat();

        this.driverListLiveData = dataRepository.getDriverListLiveData();
        this.driverListLiveData.observeForever(driverListObserver);

        this.fireStore = FirebaseFirestore.getInstance();
        this.isLoadingMutableLiveData = new MutableLiveData<>();
        this.isSuccessMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<Boolean> isLoadingLiveData() {
        return isLoadingMutableLiveData;
    }

    public LiveData<Boolean> isSuccessLiveData() {
        return isSuccessMutableLiveData;
    }

    @NonNull
    public DriverListAdapterCompat getDriverListAdapterCompat() {
        return driverListAdapterCompat;
    }

    public void updateBus(@NonNull Bus bus) {
        isLoadingMutableLiveData.setValue(true);
        fireStore.collection(FirebaseExt.BUS_COLLECTION)
                .document(bus.getDocumentId())
                .set(bus, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            isSuccessMutableLiveData.setValue(true);
                        } else {
                            Exception exception = task.getException();
                            if (exception != null) {
                                Log.e(LOG_TAG, exception.getMessage());
                            }
                            isSuccessMutableLiveData.setValue(false);
                        }
                        isLoadingMutableLiveData.setValue(false);
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (driverListLiveData.hasObservers()) {
            driverListLiveData.removeObserver(driverListObserver);
        }
    }
}
