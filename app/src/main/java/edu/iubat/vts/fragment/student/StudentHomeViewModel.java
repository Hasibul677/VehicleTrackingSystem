package edu.iubat.vts.fragment.student;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.data.repository.DataRepository;
import edu.iubat.vts.recyclerview.adapter.BusListAdapterCompat;

public class StudentHomeViewModel extends AndroidViewModel {
    private static final String LOG_TAG = StudentHomeViewModel.class.getSimpleName();

    private final BusListAdapterCompat busListAdapterCompat;
    private final LiveData<List<Bus>> busListLiveData;
    private final Observer<List<Bus>> busListObserver = new Observer<List<Bus>>() {
        @Override
        public void onChanged(List<Bus> buses) {
            busListAdapterCompat.submitList(buses);
        }
    };

    public StudentHomeViewModel(@NonNull Application application) {
        super(application);
        DataRepository dataRepository = DataRepository.getInstance(application.getApplicationContext());

        this.busListAdapterCompat = new BusListAdapterCompat();
        this.busListLiveData = dataRepository.getBusListLiveData();
        this.busListLiveData.observeForever(busListObserver);
        dataRepository.loadBusList();
    }

    @NonNull
    public BusListAdapterCompat getBusListAdapterCompat() {
        return busListAdapterCompat;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (busListLiveData.hasObservers()) {
            busListLiveData.removeObserver(busListObserver);
        }
    }
}
