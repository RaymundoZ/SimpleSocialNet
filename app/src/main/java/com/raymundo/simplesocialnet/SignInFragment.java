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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class SignInFragment extends Fragment {

    private OnChangeFragment onChangeFragment;
    private OnSingInCompleted onSingInCompleted;
    private CoordinatorLayout createAccText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.authenticating_layout, container, false);
        init(view);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                validate(email, password);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editTextEmail.addTextChangedListener(watcher);
        editTextPassword.addTextChangedListener(watcher);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonSignIn:
                        String email = editTextEmail.getText().toString();
                        String password = editTextPassword.getText().toString();
                        LoadingDialog dialog = new LoadingDialog(getActivity());
                        dialog.show();
                        firebaseHolder.signInUser(email, password, new FirebaseHolder.SignInListener() {
                            @Override
                            public void onSignInCompleted() {
                                dialog.dismiss();
                                if (onSingInCompleted != null)
                                    onSingInCompleted.signInCompleted();
                            }

                            @Override
                            public void onSignInFailed(String err) {
                                dialog.dismiss();
                                Snackbar.make(view, err, BaseTransientBottomBar.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case R.id.createAccText:
                        if (onChangeFragment != null)
                            onChangeFragment.changeFragment();
                        break;
                }
            }
        };
        buttonSignIn.setOnClickListener(listener);
        createAccText.setOnClickListener(listener);
        return view;
    }

    private static final String TAG = "raymundo/simplesocialnet/SignInFragment";

    private FirebaseHolder firebaseHolder;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private MaterialButton buttonSignIn;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onChangeFragment = (OnChangeFragment) context;
        onSingInCompleted = (OnSingInCompleted) context;
    }

    public static SignInFragment newInstance() {
        Bundle bundle = new Bundle();
        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onChangeFragment = null;
        onSingInCompleted = null;
    }

    private void validate(String email, String password) {
        if (email.trim().isEmpty() && password.trim().isEmpty())
            buttonSignIn.setEnabled(false);
        else if (email.trim().isEmpty())
            buttonSignIn.setEnabled(false);
        else if (password.trim().isEmpty())
            buttonSignIn.setEnabled(false);
        else buttonSignIn.setEnabled(true);
    }

    public interface OnChangeFragment {
        void changeFragment();
    }

    private void init(View view) {
        firebaseHolder = FirebaseHolder.getInstance();
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonSignIn = view.findViewById(R.id.buttonSignIn);
        createAccText = view.findViewById(R.id.createAccText);
    }

    public interface OnSingInCompleted {
        void signInCompleted();
    }

    public static String getTAG() {
        return TAG;
    }

}
