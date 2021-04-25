package com.raymundo.simplesocialnet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class LoadingDialog extends AlertDialog {

    protected LoadingDialog(@NonNull Context context) {
        super(context, false, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.loading_layout, null, false);
        setContentView(view);
    }

}
