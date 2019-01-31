package com.bincee.parent.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bincee.parent.R;
import com.bincee.parent.base.BDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlertDialog extends BDialog {
    @BindView(R.id.buttonOK)
    Button buttonOK;
    @BindView(R.id.imageViewCross)
    ImageView imageViewCross;

    public AlertDialog(Context context) {
        super(context);

        int layout = R.layout.dialog_announcement;
        View view = getLayoutInflater().inflate(layout, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);

    }

    @OnClick(R.id.buttonOK)
    public void onButtonOKClicked() {
        dismiss();
    }

    @OnClick(R.id.imageViewCross)
    public void onImageViewCrossClicked() {
        dismiss();

    }
}
