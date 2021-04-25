package com.raymundo.simplesocialnet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.raymundo.simplesocialnet.pojo.User;

public class MainActivity extends AppCompatActivity {

    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;
    private FirebaseHolder holder;

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
                FragmentManager manager = getSupportFragmentManager();
                switch (item.getItemId()) {
                    case R.id.friends:
                        FriendsFragment friendsFragment = (FriendsFragment) manager.findFragmentByTag(FriendsFragment.getTAG());
                        if (friendsFragment == null) {
                            friendsFragment = FriendsFragment.newInstance();
                            manager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .replace(R.id.container, friendsFragment, FriendsFragment.getTAG()).commit();
                        }
                        drawerLayout.closeDrawer(navView);
                        break;
                    case R.id.profile:
                        ProfileFragment profileFragment = (ProfileFragment) manager.findFragmentByTag(ProfileFragment.getTAG());
                        if (profileFragment == null) {
                            profileFragment = ProfileFragment.newInstance();
                            manager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .replace(R.id.container, profileFragment, ProfileFragment.getTAG()).commit();
                        }
                        drawerLayout.closeDrawer(navView);
                        break;
                }
                return true;
            }
        });
    }

    private void init() {
        holder = FirebaseHolder.getInstance();
        navView = findViewById(R.id.navView);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHolder.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, AuthenticationActivity.class));
                finish();
            }
        });
        String uid = holder.getFirebaseUser().getUid();
        holder.getDatabase().getReference("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                View view = navView.getHeaderView(0);
                MaterialTextView textView = view.findViewById(R.id.name);
                ShapeableImageView imageView = view.findViewById(R.id.image);
                ProgressBar progressBar = view.findViewById(R.id.progressBar);
                textView.setText(snapshot.getValue(User.class).getName());
                imageView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                holder.downloadFile("profileImage.jpg", new FirebaseHolder.DownloadFileListener() {
                    @Override
                    public void onDownloadFileCompleted(Object file) {
                        imageView.setImageBitmap((Bitmap) file);
                        progressBar.setVisibility(View.INVISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onDownloadFileFailed(String err) {
                        Snackbar.make(findViewById(android.R.id.content), err, BaseTransientBottomBar.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
