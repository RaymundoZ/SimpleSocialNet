package com.raymundo.simplesocialnet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.raymundo.simplesocialnet.pojo.User;

public class MainActivity extends AppCompatActivity {

    private static final String SEND_TAG = "SEND_USER";

    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.main_fragment_layout);
        init();
        DrawerArrowDrawable arrowDrawable = new DrawerArrowDrawable(this);
        arrowDrawable.setColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(arrowDrawable);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                arrowDrawable.setProgress(slideOffset);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrowDrawable.getProgress() == 0)
                    drawerLayout.openDrawer(navView);
                else if (arrowDrawable.getProgress() == 1)
                    drawerLayout.closeDrawer(navView);
            }
        });
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.friends:
                        FragmentManager manager = getSupportFragmentManager();
                        FriendsFragment fragment = (FriendsFragment) manager.findFragmentByTag(FriendsFragment.getTAG());
                        if (fragment == null) {
                            fragment = FriendsFragment.newInstance();
                            manager.beginTransaction().replace(R.id.container, fragment, FriendsFragment.getTAG()).commit();
                        }
                        drawerLayout.closeDrawer(navView);
                        break;
                }
                return true;
            }
        });

        if (getIntent().hasExtra(SEND_TAG)) {
            User user = getIntent().getParcelableExtra(SEND_TAG);
            View view = navView.getHeaderView(0);
            MaterialTextView textView = view.findViewById(R.id.name);
            textView.setText(user.getName());
        }

    }

    private void init() {
        navView = findViewById(R.id.navView);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
    }

    public static void sendUser(User user, Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(SEND_TAG, user);
        context.startActivity(intent);
    }

}
