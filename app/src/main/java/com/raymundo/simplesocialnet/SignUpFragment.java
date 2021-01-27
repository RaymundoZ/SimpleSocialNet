package com.raymundo.simplesocialnet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class SignUpFragment extends Fragment {

    private static final String TAG = "raymundo/simplesocialnet/SignUpFragment";

    private FirebaseHolder firebaseHolder;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPas;
    private MaterialButton buttonSignUp;

    public static SignUpFragment newInstance() {
        Bundle bundle = new Bundle();
        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_layout, container, false);
        init(view);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPas = editTextConfirmPas.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty() || confirmPas.isEmpty()) {
                    Snackbar.make(view, "The fields are empty", BaseTransientBottomBar.LENGTH_SHORT).show();
                } else {
                    if (!password.equals(confirmPas)) {
                        Snackbar.make(view, "Please, confirm password", BaseTransientBottomBar.LENGTH_SHORT)
                                .show();
                    } else {
                        firebaseHolder.signUpUser(email, password, new FirebaseHolder.SignUpListener() {
                            @Override
                            public void onSignUpCompleted() {
                                Snackbar.make(view, "Sign up succeeded", BaseTransientBottomBar.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onSignUpFailed(String err) {
                                Snackbar.make(view, err, BaseTransientBottomBar.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        };
        buttonSignUp.setOnClickListener(listener);
        return view;
    }

    private void init(View view) {
        firebaseHolder = FirebaseHolder.getInstance();
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextConfirmPas = view.findViewById(R.id.editTextConfirmPas);
        buttonSignUp = view.findViewById(R.id.buttonSignUp);
    }

    public static String getTAG() {
        return TAG;
    }

}
