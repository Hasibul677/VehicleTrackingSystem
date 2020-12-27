package edu.iubat.vts.fragment.admin.edit;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.extensions.FirebaseExt;

public class EditBusViewModel extends ViewModel {
    private static final String LOG_TAG = EditBusViewModel.class.getSimpleName();

    private final FirebaseFirestore firestore;
    private final MutableLiveData<Boolean> isLoadingMutableLiveData;
    private final MutableLiveData<Boolean> isSuccessMutableLiveData;

    public EditBusViewModel() {
        this.firestore = FirebaseFirestore.getInstance();
        this.isLoadingMutableLiveData = new MutableLiveData<>();
        this.isSuccessMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<Boolean> isLoadingLiveData() {
        return isSuccessMutableLiveData;
    }

    public LiveData<Boolean> isSuccessLiveData() {
        return isSuccessMutableLiveData;
    }

    public void updateBus(@NonNull Bus bus) {
        isLoadingMutableLiveData.setValue(true);
        firestore.collection(FirebaseExt.BUS_COLLECTION)
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
                        }
                        isLoadingMutableLiveData.setValue(false);
                    }
                });
    }
}
