package com.bincee.parent.dialog;

import android.content.Context;
import android.view.View;

import com.bincee.parent.R;
import com.bincee.parent.base.BDialog;

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
