package com.raymundo.simplesocialnet;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;

public class AuthenticationActivity extends AppCompatActivity implements SignInFragment.OnChangeFragmentListener {

    private FragmentManager manager;
    private SignInFragment signInFragment;
    private SignUpFragment signUpFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_layout);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        manager = getSupportFragmentManager();
        signInFragment = SignInFragment.newInstance();
        signUpFragment = SignUpFragment.newInstance();
        manager.beginTransaction().add(R.id.container, signInFragment, SignInFragment.getTAG()).commit();
    }


    @Override
    public void changeFragment() {
        manager.beginTransaction()
                .hide(signInFragment)
                .add(R.id.container, signUpFragment, SignUpFragment.getTAG())
                .addToBackStack(null).commit();
    }
}
