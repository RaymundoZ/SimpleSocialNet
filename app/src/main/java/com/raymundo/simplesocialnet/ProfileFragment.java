package com.raymundo.simplesocialnet;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.raymundo.simplesocialnet.pojo.User;

public class ProfileFragment extends Fragment {

    private static final String TAG = "raymundo/simplesocialnet/ProfileFragment";

    private FirebaseHolder holder;
    private MaterialButton editProfileButton;
    private MaterialTextView profileName;
    private ShapeableImageView profileImage;
    private ProgressBar progressBar;

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
        holder = FirebaseHolder.getInstance();
        editProfileButton = view.findViewById(R.id.editButton);
        profileName = view.findViewById(R.id.profileName);
        profileImage = view.findViewById(R.id.profileImage);
        progressBar = view.findViewById(R.id.progressBar);
        String uid = holder.getFirebaseUser().getUid();
        holder.getDatabase().getReference("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profileName.setText(snapshot.getValue(User.class).getName());
                profileImage.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                holder.downloadFile("profileImage.jpg", new FirebaseHolder.DownloadFileListener() {
                    @Override
                    public void onDownloadFileCompleted(Object file) {
                        profileImage.setImageBitmap((Bitmap) file);
                        progressBar.setVisibility(View.INVISIBLE);
                        profileImage.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onDownloadFileFailed(String err) {
                        Snackbar.make(getView(), err, BaseTransientBottomBar.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        profileImage.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
