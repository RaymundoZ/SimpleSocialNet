package com.raymundo.simplesocialnet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainFragment extends Fragment {

    private static final String TAG = "raymundo/simplesocialnet/MainFragment";
    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;

    public static MainFragment newInstance() {
        Bundle bundle = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.main_fragment_layout, container, false);
        DrawerArrowDrawable arrowDrawable = new DrawerArrowDrawable(getActivity());
        navView = view.findViewById(R.id.navView);
        drawerLayout = view.findViewById(R.id.drawer);
        toolbar = view.findViewById(R.id.toolbar);
        arrowDrawable.setColor(getActivity().getResources().getColor(R.color.white));
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
        return view;
    }

    public static String getTAG() {
        return TAG;
    }
}
