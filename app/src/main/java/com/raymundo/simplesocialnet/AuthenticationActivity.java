package com.raymundo.simplesocialnet;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseUser;

public class AuthenticationActivity extends AppCompatActivity implements SignInFragment.OnChangeFragment,
        SignInFragment.OnSingInCompleted, SignUpFragment.OnSignUpCompleted {

    private FragmentManager manager;
    private SignInFragment signInFragment;
    private SignUpFragment signUpFragment;
    private FirebaseHolder firebaseHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_layout);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        init();
        FirebaseUser firebaseUser = firebaseHolder.getFirebaseUser();
        if (firebaseUser == null)
            manager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.container, signInFragment, SignInFragment.getTAG())
                    .commit();
        else
            completeAuth();
    }

    private void init() {
        manager = getSupportFragmentManager();
        signInFragment = SignInFragment.newInstance();
        signUpFragment = SignUpFragment.newInstance();
        firebaseHolder = FirebaseHolder.getInstance();
    }

    private void completeAuth() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void changeFragment() {
        manager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, signUpFragment, SignUpFragment.getTAG())
                .addToBackStack(null).commit();
    }

    @Override
    public void signInCompleted() {
        completeAuth();
    }

    @Override
    public void signUpCompleted() {
        completeAuth();
    }
}
