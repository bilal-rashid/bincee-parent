package com.findxain.uberparentapp.dialog;

import android.content.Context;
import android.view.View;

import com.findxain.uberparentapp.R;
import com.findxain.uberparentapp.base.BDialog;

import butterknife.ButterKnife;

public class AlertDialog extends BDialog {
    public AlertDialog(Context context) {
        super(context);

        int layout = R.layout.dialog_announcement;
        View view = getLayoutInflater().inflate(layout, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);

    }
}
