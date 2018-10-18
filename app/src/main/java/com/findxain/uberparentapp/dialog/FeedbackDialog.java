package com.findxain.uberparentapp.dialog;

import android.content.Context;

import com.findxain.uberparentapp.R;
import com.findxain.uberparentapp.base.BDialog;

public class FeedbackDialog extends BDialog {
    public FeedbackDialog(Context context) {
        super(context);

        setContentView(getLayoutInflater().inflate(R.layout.feedback_dialog, null, false));

    }
}
