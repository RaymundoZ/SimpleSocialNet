package com.raymundo.simplesocialnet;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.main_layout);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, MainFragment.newInstance(),
                MainFragment.getTAG()).commit();
    }
}
