package edu.iubat.vts.fragment.common.profile;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import edu.iubat.vts.data.model.User;
import edu.iubat.vts.data.repository.DataRepository;
import edu.iubat.vts.extensions.FirebaseExt;

public class ProfileViewModel extends AndroidViewModel {
    private static final String LOG_TAG = ProfileViewModel.class.getSimpleName();

    private final DataRepository dataRepository;
    private final FirebaseFirestore firestore;
    private final FirebaseStorage storage;
    private final MutableLiveData<Boolean> isLoadingMutableLiveData;
    private final LiveData<User> userLiveData;

    private User user;

    private final Observer<User> userObserver = new Observer<User>() {
        @Override
        public void onChanged(User user) {
            if (user != null) {
                ProfileViewModel.this.user = user;
                isLoadingMutableLiveData.setValue(false);
            }
        }
    };

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.dataRepository = DataRepository.getInstance(application.getApplicationContext());
        this.firestore = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();
        this.isLoadingMutableLiveData = new MutableLiveData<>();
        this.isLoadingMutableLiveData.setValue(true);
        this.userLiveData = dataRepository.getUserLiveData();
        this.userLiveData.observeForever(userObserver);
    }

    @Nullable
    public User getUser() {
        return user;
    }

    public LiveData<Boolean> isLoadingLiveData() {
        return isLoadingMutableLiveData;
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public void updateProfileImage(String path) {
        if (getUser() != null) {
            isLoadingMutableLiveData.setValue(true);
            StorageReference reference = storage.getReference(FirebaseExt.PROFILE_IMAGE_COLLECTION)
                    .child(getUser().getDocumentId());
            UploadTask uploadTask = reference.putFile(Uri.parse(path));
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        return reference.getDownloadUrl();
                    } else {
                        isLoadingMutableLiveData.setValue(false);
                        Exception exception = task.getException();
                        if (exception != null) {
                            Log.e(LOG_TAG, exception.getMessage());
                        }
                    }
                    return null;
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri uri = task.getResult();
                        if (uri != null) {
                            getUser().setDisplayIconPath(uri.toString());
                            updateUser(getUser());
                        }
                    } else {
                        isLoadingMutableLiveData.setValue(false);
                        Exception exception = task.getException();
                        if (exception != null) {
                            Log.e(LOG_TAG, exception.getMessage());
                        }
                    }
                }
            });
        }
    }

    private void updateUser(@NonNull User user) {
        firestore.collection(FirebaseExt.USER_COLLECTION)
                .document(user.getDocumentId())
                .set(user, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dataRepository.loadUser();
                        } else {
                            isLoadingMutableLiveData.setValue(false);
                            Exception exception = task.getException();
                            if (exception != null) {
                                Log.e(LOG_TAG, exception.getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (userLiveData.hasActiveObservers()) {
            userLiveData.removeObserver(userObserver);
        }
    }
}
