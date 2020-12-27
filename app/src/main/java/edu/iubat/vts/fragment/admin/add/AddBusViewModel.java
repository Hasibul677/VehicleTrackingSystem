package edu.iubat.vts.fragment.admin.add;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.extensions.FirebaseExt;

public class AddBusViewModel extends ViewModel {
    private static final String LOG_TAG = AddBusViewModel.class.getSimpleName();

    private final FirebaseFirestore fireStore;
    private final MutableLiveData<Boolean> isLoadingMutableLiveData;
    private final MutableLiveData<Boolean> isSuccessMutableLiveData;

    public AddBusViewModel() {
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

    public void addBus(@NonNull Bus bus) {
        isLoadingMutableLiveData.setValue(true);
        DocumentReference documentReference = fireStore.collection(FirebaseExt.BUS_COLLECTION)
                .document();
        bus.setDocumentId(documentReference.getId());
        documentReference.set(bus)
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
