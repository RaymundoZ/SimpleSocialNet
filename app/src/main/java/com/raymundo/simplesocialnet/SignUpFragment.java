package com.raymundo.simplesocialnet;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

    private OnSignUpCompleted onSignUpCompleted;

    private static final String TAG = "raymundo/simplesocialnet/SignUpFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_layout, container, false);
        init(view);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPas = editTextConfirmPas.getText().toString().trim();
                validate(email, password, confirmPas);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editTextEmail.addTextChangedListener(watcher);
        editTextPassword.addTextChangedListener(watcher);
        editTextConfirmPas.addTextChangedListener(watcher);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                LoadingDialog dialog = new LoadingDialog(getActivity());
                dialog.show();
                firebaseHolder.signUpUser(email, password, new FirebaseHolder.SignUpListener() {
                    @Override
                    public void onSignUpCompleted() {
                        if (onSignUpCompleted != null) {
                            dialog.dismiss();
                            onSignUpCompleted.signUpCompleted();
                        }
                    }

                    @Override
                    public void onSignUpFailed(String err) {
                        dialog.dismiss();
                        Snackbar.make(view, err, BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });
            }
        };
        buttonSignUp.setOnClickListener(listener);
        return view;
    }

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

    private void validate(String email, String password, String confirmPas) {
        if (email.trim().isEmpty() && password.trim().isEmpty() && confirmPas.trim().isEmpty())
            buttonSignUp.setEnabled(false);
        else if (email.trim().isEmpty())
            buttonSignUp.setEnabled(false);
        else if (password.trim().isEmpty())
            buttonSignUp.setEnabled(false);
        else if (confirmPas.trim().isEmpty())
            buttonSignUp.setEnabled(false);
        else if (!password.trim().equals(confirmPas.trim()))
            buttonSignUp.setEnabled(false);
        else buttonSignUp.setEnabled(true);
    }

    private void init(View view) {
        firebaseHolder = FirebaseHolder.getInstance();
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextConfirmPas = view.findViewById(R.id.editTextConfirmPas);
        buttonSignUp = view.findViewById(R.id.buttonSignUp);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onSignUpCompleted = (OnSignUpCompleted) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSignUpCompleted = null;
    }

    public interface OnSignUpCompleted {
        void signUpCompleted();
    }

    public static String getTAG() {
        return TAG;
    }

}
