package com.raymundo.simplesocialnet;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseHolder {

    public interface SignUpListener {
        void onSignUpCompleted();

        void onSignUpFailed(String err);
    }

    public interface signInListener {
        void onSignInCompleted(FirebaseUser user);

        void onSignInFailed(String err);
    }

    private static FirebaseHolder instance;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private FirebaseHolder() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

    }

    public static FirebaseHolder getInstance() {
        if (instance == null)
            instance = new FirebaseHolder();

        return instance;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void signUpUser(String email, String password, SignUpListener listener) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        listener.onSignUpCompleted();
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onSignUpFailed(e.getLocalizedMessage());
            }
        });
    }

    public void signInUser(String email, String password, signInListener listener) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        user = auth.getCurrentUser();
                        listener.onSignInCompleted(user);
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onSignInFailed(e.getLocalizedMessage());
            }
        });
    }

}
