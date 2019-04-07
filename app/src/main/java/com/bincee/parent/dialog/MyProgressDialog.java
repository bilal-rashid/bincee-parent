package com.bincee.parent.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.bincee.parent.R;
import com.bincee.parent.base.BDialog;


public class MyProgressDialog extends BDialog {
    public MyProgressDialog(@NonNull Context context) {
        super(context);

        View view = getLayoutInflater().inflate(R.layout.progress_layout, null, false);
        setContentView(view);
    }


    public void setMessage(String creating_route) {

    }
}
