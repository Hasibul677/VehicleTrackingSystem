package edu.iubat.vts.fragment.admin.list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import edu.iubat.vts.data.model.User;
import edu.iubat.vts.data.repository.DataRepository;
import edu.iubat.vts.recyclerview.adapter.UserListAdapterCompat;

public class DriverListViewModel extends AndroidViewModel {
    private static final String LOG_TAG = DriverListViewModel.class.getSimpleName();

    private final UserListAdapterCompat userListAdapterCompat;

    private final LiveData<List<User>> driverListLiveData;
    private final Observer<List<User>> driverListObserver = new Observer<List<User>>() {
        @Override
        public void onChanged(List<User> users) {
            userListAdapterCompat.submitList(users);
        }
    };

    public DriverListViewModel(@NonNull Application application) {
        super(application);
        DataRepository dataRepository = DataRepository.getInstance(application.getApplicationContext());
        this.userListAdapterCompat = new UserListAdapterCompat();

        this.driverListLiveData = dataRepository.getDriverListLiveData();
        this.driverListLiveData.observeForever(driverListObserver);
    }

    @NonNull
    public UserListAdapterCompat getUserListAdapterCompat() {
        return userListAdapterCompat;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (driverListLiveData.hasObservers()) {
            driverListLiveData.removeObserver(driverListObserver);
        }
    }
}
