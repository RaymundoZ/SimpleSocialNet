package com.raymundo.simplesocialnet;

import android.content.Context;
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
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseUser;
import com.raymundo.simplesocialnet.pojo.User;

public class SignInFragment extends Fragment {

    public interface OnChangeFragmentListener {
        void changeFragment();
    }

    private OnChangeFragmentListener onChangeFragmentListener;

    private static final String TAG = "raymundo/simplesocialnet/SignInFragment";

    private FirebaseHolder firebaseHolder;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private MaterialButton buttonSignIn;
    private MaterialTextView createAccText;

    public static SignInFragment newInstance() {
        Bundle bundle = new Bundle();
        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.authenticating_layout, container, false);
        init(view);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonSignIn:
                        String email = editTextEmail.getText().toString().trim();
                        String password = editTextPassword.getText().toString().trim();
                        if (email.isEmpty() || password.isEmpty()) {
                            Snackbar.make(view, "The fields are empty", BaseTransientBottomBar.LENGTH_SHORT).show();
                        } else {
                            firebaseHolder.signInUser(email, password, new FirebaseHolder.signInListener() {
                                @Override
                                public void onSignInCompleted(FirebaseUser user) {
                                    if (user != null) {
                                        User currentUser = new User(user.getEmail(), null);
                                        MainActivity.sendUser(currentUser, getActivity());
                                        getActivity().finish();
                                    } else {
                                        Snackbar.make(view, "Incorrect email or password",
                                                BaseTransientBottomBar.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onSignInFailed(String err) {
                                    Snackbar.make(view, err, BaseTransientBottomBar.LENGTH_SHORT).show();
                                }
                            });
                        }
                        break;
                    case R.id.createAccText:
                        if (onChangeFragmentListener != null)
                            onChangeFragmentListener.changeFragment();
                        break;
                }
            }
        };
        buttonSignIn.setOnClickListener(listener);
        createAccText.setOnClickListener(listener);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onChangeFragmentListener = (OnChangeFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onChangeFragmentListener = null;
    }

    private void init(View view) {
        firebaseHolder = FirebaseHolder.getInstance();
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonSignIn = view.findViewById(R.id.buttonSignIn);
        createAccText = view.findViewById(R.id.createAccText);
    }

    public static String getTAG() {
        return TAG;
    }

}
