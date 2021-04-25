package com.raymundo.simplesocialnet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.raymundo.simplesocialnet.pojo.User;

import static android.app.Activity.RESULT_OK;

public class EditProfileDialog extends BottomSheetDialogFragment {

    private static final int GET_IMAGE_REQUEST_CODE = 10;

    private FirebaseHolder holder;
    private ImageButton acceptButton;
    private ImageButton declineButton;
    private EditText emailField;
    private EditText nameField;
    private EditText nicknameField;
    private EditText birthdayField;
    private EditText cityField;
    private ShapeableImageView profileImage;
    private ProgressBar progressBar;

    public static EditProfileDialog newInstance() {

        Bundle args = new Bundle();

        EditProfileDialog fragment = new EditProfileDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_profile_layout, container, false);
        init(view);
        FirebaseHolder holder = FirebaseHolder.getInstance();
        User user = holder.getUser();
        emailField.setText(user.getEmail());
        nameField.setText(user.getName());
        nicknameField.setText(user.getNickname());
        birthdayField.setText(user.getBirthday());
        cityField.setText(user.getCity());
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.acceptButton:
                        User user = new User(nameField.getText().toString(),
                                emailField.getText().toString(),
                                nicknameField.getText().toString(),
                                cityField.getText().toString(),
                                birthdayField.getText().toString(), null);
                        holder.setData(user, new FirebaseHolder.SetDataListener() {
                            @Override
                            public void onSetDataCompleted() {
                                dismiss();
                            }

                            @Override
                            public void onSetDataFailed(String err) {
                                Snackbar.make(view, err, BaseTransientBottomBar.LENGTH_SHORT).show();
                            }
                        });
                        break;

                    case R.id.declineButton:
                        dismiss();
                        break;
                }
            }
        };
        acceptButton.setOnClickListener(listener);
        declineButton.setOnClickListener(listener);
        return view;
    }

    private void init(View view) {
        holder = FirebaseHolder.getInstance();
        acceptButton = view.findViewById(R.id.acceptButton);
        declineButton = view.findViewById(R.id.declineButton);
        emailField = view.findViewById(R.id.emailField);
        nameField = view.findViewById(R.id.nameField);
        nicknameField = view.findViewById(R.id.nicknameField);
        birthdayField = view.findViewById(R.id.birthdayField);
        cityField = view.findViewById(R.id.cityField);
        profileImage = view.findViewById(R.id.profileImage);
        progressBar = view.findViewById(R.id.progressBar);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GET_IMAGE_REQUEST_CODE);
            }
        });
        String uid = holder.getFirebaseUser().getUid();
        holder.getDatabase().getReference("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BottomSheetDialog) getDialog()).getBehavior().setFitToContents(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            holder.uploadFile("profileImage.jpg", imageUri, new FirebaseHolder.UploadFileListener() {
                @Override
                public void onUploadFileCompleted() {
                    User user = holder.getUser();
                    String uid = holder.getFirebaseUser().getUid();
                    holder.getStorage().getReference("images").child(uid)
                            .child("profileImage.jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                String imagePath = task.getResult().toString();
                                user.setImagePath(imagePath);
                                holder.setData(user, new FirebaseHolder.SetDataListener() {
                                    @Override
                                    public void onSetDataCompleted() {

                                    }

                                    @Override
                                    public void onSetDataFailed(String err) {
                                        Snackbar.make(getView(), err, BaseTransientBottomBar.LENGTH_SHORT).show();
                                    }
                                });
                            } else
                                Snackbar.make(getView(), task.getException().getLocalizedMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(getView(), e.getLocalizedMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onUploadFileFailed(String err) {
                    Snackbar.make(getView(), err, BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            });
        }
        dismiss();
    }
}
