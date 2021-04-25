package com.raymundo.simplesocialnet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.raymundo.simplesocialnet.pojo.User;

public class EditProfileDialog extends BottomSheetDialogFragment {

    private ImageButton acceptButton;
    private ImageButton declineButton;
    private EditText emailField;
    private EditText nameField;
    private EditText nicknameField;
    private EditText birthdayField;
    private EditText cityField;

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
                        User user = new User(nameField.getText().toString(), null,
                                emailField.getText().toString(),
                                nicknameField.getText().toString(),
                                cityField.getText().toString(),
                                birthdayField.getText().toString());
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
        acceptButton = view.findViewById(R.id.acceptButton);
        declineButton = view.findViewById(R.id.declineButton);
        emailField = view.findViewById(R.id.emailField);
        nameField = view.findViewById(R.id.nameField);
        nicknameField = view.findViewById(R.id.nicknameField);
        birthdayField = view.findViewById(R.id.birthdayField);
        cityField = view.findViewById(R.id.cityField);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BottomSheetDialog) getDialog()).getBehavior().setFitToContents(false);
    }

}
