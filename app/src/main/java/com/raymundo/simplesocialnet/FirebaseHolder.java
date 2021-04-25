package com.raymundo.simplesocialnet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.raymundo.simplesocialnet.pojo.User;

public class FirebaseHolder {

    public interface SignUpListener {
        void onSignUpCompleted();

        void onSignUpFailed(String err);
    }

    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private User user;

    private FirebaseHolder() {
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        getCurrentUser(new GetDataListener() {
            @Override
            public void onGetDataCompleted(User user) {
                FirebaseHolder.this.user = user;
            }

            @Override
            public void onGetDataFailed(String err) {
                user = User.getNullUser();
            }
        });
    }

    private static FirebaseHolder instance;

    private FirebaseAuth auth;

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public User getUser() {
        return user;
    }

    public static FirebaseHolder getInstance() {
        if (instance == null)
            instance = new FirebaseHolder();

        return instance;
    }

    public void signUpUser(String email, String password, SignUpListener listener) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = auth.getCurrentUser();
                            String uid = firebaseUser.getUid();
                            User currentUser = new User(null, null, firebaseUser.getEmail(), null, null, null);
                            database.getReference("users").child(uid).setValue(currentUser, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    if (error == null) {
                                        FirebaseHolder.this.user = currentUser;
                                        listener.onSignUpCompleted();
                                    } else
                                        listener.onSignUpFailed(error.getMessage());
                                }
                            });
                        } else {
                            listener.onSignUpFailed(task.getException().toString());
                        }
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onSignUpFailed(e.getLocalizedMessage());
            }
        });
    }

    public void signInUser(String email, String password, SignInListener listener) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = auth.getCurrentUser();
                            getCurrentUser(new GetDataListener() {
                                @Override
                                public void onGetDataCompleted(User user) {
                                    FirebaseHolder.this.user = user;
                                    listener.onSignInCompleted();
                                }

                                @Override
                                public void onGetDataFailed(String err) {
                                    listener.onSignInFailed(task.getException().toString());
                                }
                            });
                        } else {
                            listener.onSignInFailed(task.getException().toString());
                        }
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onSignInFailed(e.getLocalizedMessage());
            }
        });
    }

    public void signOut() {
        auth.signOut();
        firebaseUser = auth.getCurrentUser();
        user = User.getNullUser();
    }

    public void setData(User user, SetDataListener listener) {
        String uid = auth.getUid();
        DatabaseReference reference = database.getReference("users").child(uid);
        reference.setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    FirebaseHolder.this.user = user;
                    listener.onSetDataCompleted();
                } else
                    listener.onSetDataFailed(error.getMessage());
            }
        });
    }

    private void getCurrentUser(GetDataListener listener) {
        if (firebaseUser == null) {
            listener.onGetDataCompleted(User.getNullUser());
            return;
        }
        String uid = firebaseUser.getUid();
        DatabaseReference reference = database.getReference("users").child(uid);
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                    listener.onGetDataCompleted(task.getResult().getValue(User.class));
                else
                    listener.onGetDataFailed(task.getException().getLocalizedMessage());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onGetDataFailed(e.getLocalizedMessage());
            }
        });
    }

    public interface SignInListener {
        void onSignInCompleted();

        void onSignInFailed(String err);
    }

    public interface SetDataListener {
        void onSetDataCompleted();

        void onSetDataFailed(String err);
    }

    private interface GetDataListener {
        void onGetDataCompleted(User user);

        void onGetDataFailed(String err);
    }

    public interface InitHolderListener {
        void onInitHolderCompleted();
    }

//    public void initHolder(InitHolderListener listener) {
//        getInstance();
//        getCurrentUser(new GetDataListener() {
//            @Override
//            public void onGetDataCompleted(User user) {
//                FirebaseHolder.this.user = user;
//                listener.onInitHolderCompleted();
//            }
//            @Override
//            public void onGetDataFailed(String err) {
//                user = User.getNullUser();
//                listener.onInitHolderCompleted();
//            }
//        });
//    }

}
