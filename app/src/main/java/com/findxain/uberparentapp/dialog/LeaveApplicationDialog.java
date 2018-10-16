package com.findxain.uberparentapp.dialog;

import android.content.Context;
import android.view.View;

import com.findxain.uberparentapp.R;
import com.findxain.uberparentapp.base.BDialog;

import butterknife.ButterKnife;

public class LeaveApplicationDialog extends BDialog {

    public LeaveApplicationDialog(Context context) {
        super(context);
        int layout = R.layout.dialog_leave_application;
        View view = getLayoutInflater().inflate(layout, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);
    }

}
