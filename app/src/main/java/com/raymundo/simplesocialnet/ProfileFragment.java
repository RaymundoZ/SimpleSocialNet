package com.raymundo.simplesocialnet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {

    private static final String TAG = "raymundo/simplesocialnet/ProfileFragment";

    private MaterialButton editProfileButton;

    public static ProfileFragment newInstance() {

        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static String getTAG() {
        return TAG;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_layout, container, false);
        init(view);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getChildFragmentManager();
                EditProfileDialog dialog = EditProfileDialog.newInstance();
                dialog.show(manager, dialog.getTag());
            }
        });
        return view;
    }

    private void init(View view) {
        editProfileButton = view.findViewById(R.id.editButton);
    }
}
