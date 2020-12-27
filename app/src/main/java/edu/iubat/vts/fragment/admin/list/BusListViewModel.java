package edu.iubat.vts.fragment.admin.list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.data.repository.DataRepository;
import edu.iubat.vts.extensions.FirebaseExt;
import edu.iubat.vts.recyclerview.adapter.AdminBusListAdapterCompat;

public class BusListViewModel extends AndroidViewModel {
    private static final String LOG_TAG = BusListViewModel.class.getSimpleName();

    private final FirebaseFirestore firestore;
    private final FirebaseDatabase database;

    private final AdminBusListAdapterCompat adminBusListAdapterCompat;
    private final LiveData<List<Bus>> busListLiveData;
    private final Observer<List<Bus>> busListObserver = new Observer<List<Bus>>() {
        @Override
        public void onChanged(List<Bus> buses) {
            adminBusListAdapterCompat.submitList(buses);
        }
    };

    public BusListViewModel(@NonNull Application application) {
        super(application);
        this.firestore = FirebaseFirestore.getInstance();
        this.database = FirebaseDatabase.getInstance();
        DataRepository dataRepository = DataRepository.getInstance(application.getApplicationContext());

        this.adminBusListAdapterCompat = new AdminBusListAdapterCompat();
        this.busListLiveData = dataRepository.getBusListLiveData();
        this.busListLiveData.observeForever(busListObserver);
    }

    @NonNull
    public AdminBusListAdapterCompat getAdminBusListAdapterCompat() {
        return adminBusListAdapterCompat;
    }

    public void delete(@NonNull Bus bus) {
        firestore.collection(FirebaseExt.BUS_COLLECTION)
                .document(bus.getDocumentId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (busListLiveData.hasObservers()) {
            busListLiveData.removeObserver(busListObserver);
        }
    }
}
