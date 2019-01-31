package com.bincee.parent.dialog;

import android.content.Context;

import com.bincee.parent.R;
import com.bincee.parent.base.BDialog;

public class FeedbackDialog extends BDialog {
    public FeedbackDialog(Context context) {
        super(context);

        setContentView(getLayoutInflater().inflate(R.layout.feedback_dialog, null, false));

    }
}
