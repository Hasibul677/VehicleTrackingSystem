package edu.iubat.vts.activity.signin;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import edu.iubat.vts.data.model.User;
import edu.iubat.vts.extensions.FirebaseExt;

public class SignInViewModel extends ViewModel {
    private static final String LOG_TAG = SignInViewModel.class.getSimpleName();

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore fireStore;

    private final MutableLiveData<Boolean> isLoadingMutableLiveData;
    private final MutableLiveData<Boolean> isSuccessMutableLiveData;

    public SignInViewModel() {
        this.firebaseAuth = FirebaseAuth.getInstance();
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

    public void signIn(@NonNull String email, @NonNull String password) {
        isLoadingMutableLiveData.setValue(true);
        fireStore.collection(FirebaseExt.USER_COLLECTION)
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && !task.getResult().getDocuments().isEmpty()) {
                            DocumentSnapshot snapshot = task.getResult().getDocuments().get(0);
                            User user = snapshot.toObject(User.class);
                            if (user.isActive()) {
                                signInWithEmailAndPassword(email, password);
                            } else {
                                createAccountWithEmailAndPassword(user, password);
                            }
                        } else {
                            Exception exception = task.getException();
                            if (exception != null) {
                                Log.e(LOG_TAG, exception.getMessage());
                            }
                            isLoadingMutableLiveData.setValue(false);
                            isSuccessMutableLiveData.setValue(false);
                        }
                    }
                });
    }

    private void signInWithEmailAndPassword(@NonNull String email, @NonNull String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            isLoadingMutableLiveData.setValue(false);
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            isSuccessMutableLiveData.setValue(firebaseUser != null);
                        } else {
                            Exception exception = task.getException();
                            if (exception != null) {
                                Log.e(LOG_TAG, exception.getMessage());
                            }
                            isLoadingMutableLiveData.setValue(false);
                            isSuccessMutableLiveData.setValue(false);
                        }
                    }
                });
    }

    private void createAccountWithEmailAndPassword(@NonNull User user, @NonNull String password) {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getUser() != null) {
                            updateNewUser(user);
                        } else {
                            Exception exception = task.getException();
                            if (exception != null) {
                                Log.e(LOG_TAG, exception.getMessage());
                            }
                            isLoadingMutableLiveData.setValue(false);
                            isSuccessMutableLiveData.setValue(false);
                        }
                    }
                });
    }

    private void updateNewUser(@NonNull User user) {
        user.setActive(true);
        fireStore.collection(FirebaseExt.USER_COLLECTION)
                .document(user.getDocumentId())
                .update("active", true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            isLoadingMutableLiveData.setValue(false);
                            isSuccessMutableLiveData.setValue(true);
                        } else {
                            Exception exception = task.getException();
                            if (exception != null) {
                                Log.e(LOG_TAG, exception.getMessage());
                            }
                            isLoadingMutableLiveData.setValue(false);
                            isSuccessMutableLiveData.setValue(false);
                        }
                    }
                });
    }
}
