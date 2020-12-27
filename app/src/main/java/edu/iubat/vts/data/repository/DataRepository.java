package edu.iubat.vts.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.data.model.User;
import edu.iubat.vts.extensions.FirebaseExt;

public class DataRepository {
    private static final String LOG_TAG = DataRepository.class.getSimpleName();

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    private static volatile DataRepository INSTANCE;

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore fireStore;
    private final FirebaseStorage storage;

    private final MutableLiveData<User> userMutableLiveData;
    private final MutableLiveData<List<User>> driverListMutableLiveData;
    private final MutableLiveData<List<User>> studentListMutableLiveData;
    private final MutableLiveData<List<Bus>> busListMutableLiveData;

    private DataRepository(@NonNull Context context) {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.fireStore = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();

        this.userMutableLiveData = new MutableLiveData<>();
        this.driverListMutableLiveData = new MutableLiveData<>();
        this.studentListMutableLiveData = new MutableLiveData<>();
        this.busListMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<User> getUserLiveData() {
        return userMutableLiveData;
    }

    public LiveData<List<User>> getDriverListLiveData() {
        return driverListMutableLiveData;
    }

    public LiveData<List<User>> getStudentListLiveData() {
        return studentListMutableLiveData;
    }

    public LiveData<List<Bus>> getBusListLiveData() {
        return busListMutableLiveData;
    }

    public void loadUser() {
        if (firebaseAuth.getCurrentUser() != null) {
            fireStore.collection(FirebaseExt.USER_COLLECTION)
                    .whereEqualTo("email", firebaseAuth.getCurrentUser().getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult() != null) {
                                    if (!task.getResult().getDocuments().isEmpty()) {
                                        User user = task.getResult().getDocuments().get(0).toObject(User.class);
                                        if (user != null) {
                                            userMutableLiveData.setValue(user);
                                        }
                                    }
                                }
                            } else {
                                Exception exception = task.getException();
                                if (exception != null) {
                                    Log.e(LOG_TAG, exception.getMessage());
                                }
                            }
                        }
                    });
        }
    }

    public void loadAndOrganizeForAdmin() {
        loadAllUsers();
        loadBusList();
    }

    private void loadAllUsers() {
        fireStore.collection(FirebaseExt.USER_COLLECTION)
                .orderBy("firstName")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e(LOG_TAG, error.getMessage());
                            return;
                        }
                        if (value != null) {
                            executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    List<User> driverList = new ArrayList<>();
                                    List<User> studentList = new ArrayList<>();
                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                        User user = snapshot.toObject(User.class);
                                        if (user != null) {
                                            if (user.isDriver()) {
                                                driverList.add(user);
                                            } else if (user.isStudent()) {
                                                studentList.add(user);
                                            }
                                        }
                                    }
                                    driverListMutableLiveData.postValue(driverList);
                                    studentListMutableLiveData.postValue(studentList);
                                }
                            });
                        }
                    }
                });
    }

    public void loadBusList() {
        fireStore.collection(FirebaseExt.BUS_COLLECTION)
                .orderBy("busNumber")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e(LOG_TAG, error.getMessage());
                            return;
                        }
                        if (value != null) {
                            executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    List<Bus> busList = new ArrayList<>();
                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                        Bus bus = snapshot.toObject(Bus.class);
                                        if (bus != null) {
                                            busList.add(bus);
                                        }
                                    }
                                    busListMutableLiveData.postValue(busList);
                                }
                            });
                        }
                    }
                });
    }

    public static DataRepository getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (DataRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataRepository(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    public static void destroy() {
        INSTANCE = null;
    }
}
