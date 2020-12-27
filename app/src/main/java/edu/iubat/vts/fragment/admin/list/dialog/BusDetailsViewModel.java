package edu.iubat.vts.fragment.admin.list.dialog;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.data.model.User;
import edu.iubat.vts.extensions.FirebaseExt;

public class BusDetailsViewModel extends AndroidViewModel {
    private static final String LOG_TAG =BusDetailsViewModel.class.getSimpleName();

    private final FirebaseFirestore firestore;
    private final MutableLiveData<Boolean> isLoadingMutableLiveData;
    private final MutableLiveData<User> userMutableLiveData;

    public BusDetailsViewModel(@NonNull Application application) {
        super(application);
        this.firestore = FirebaseFirestore.getInstance();
        this.isLoadingMutableLiveData = new MutableLiveData<>();
        this.userMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<Boolean> isLoadingLiveData() {
        return isLoadingMutableLiveData;
    }

    public LiveData<User> getUserLiveData() {
        return userMutableLiveData;
    }

    public void loadUser(@NonNull Bus bus) {
        if (bus.getDriverDocumentId() != null && !bus.getDriverDocumentId().isEmpty()) {
            isLoadingMutableLiveData.setValue(true);
            firestore.collection(FirebaseExt.USER_COLLECTION)
                    .whereEqualTo("documentId", bus.getDriverDocumentId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (querySnapshot != null) {
                                    for (DocumentSnapshot snapshot : querySnapshot) {
                                        User user = snapshot.toObject(User.class);
                                        if (user != null) {
                                            userMutableLiveData.setValue(user);
                                            break;
                                        }
                                    }
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
    }
}
