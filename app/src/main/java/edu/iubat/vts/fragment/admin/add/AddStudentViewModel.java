package edu.iubat.vts.fragment.admin.add;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import edu.iubat.vts.data.model.User;
import edu.iubat.vts.extensions.FirebaseExt;

public class AddStudentViewModel extends ViewModel {
    private static final String LOG_TAG = AddStudentViewModel.class.getSimpleName();

    private final FirebaseFirestore fireStore;

    private final MutableLiveData<Boolean> isLoadingMutableLiveData;
    private final MutableLiveData<Boolean> isSuccessMutableLiveData;

    public AddStudentViewModel() {
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

    public void createAccount(@NonNull User user) {
        isLoadingMutableLiveData.setValue(true);
        fireStore.collection(FirebaseExt.USER_COLLECTION)
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean isValid = true;
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                                if (snapshot.exists()) {
                                    isValid = false;
                                }
                            }
                        }
                        if (isValid) {
                            saveUserInformation(user);
                        }
                    }
                });

    }

    private void saveUserInformation(@NonNull User user) {
        DocumentReference documentReference = fireStore.collection(FirebaseExt.USER_COLLECTION)
                .document();
        user.setDocumentId(documentReference.getId());
        user.setActive(false);
        documentReference.set(user)
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
}
