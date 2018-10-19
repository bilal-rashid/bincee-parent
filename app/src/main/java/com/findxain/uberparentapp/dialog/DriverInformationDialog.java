package com.findxain.uberparentapp.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.findxain.uberparentapp.R;
import com.findxain.uberparentapp.base.BDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DriverInformationDialog extends BDialog {

    @BindView(R.id.buttonLogout)
    Button buttonLogout;
    @BindView(R.id.buttonCancel)
    TextView buttonCancel;
    @BindView(R.id.imageViewCross)
    ImageView imageViewCross;

    public DriverInformationDialog(Context context) {
        super(context);
        int layout = R.layout.dialog_driver_info;
        View view = getLayoutInflater().inflate(layout, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.buttonLogout)
    public void onButtonLogoutClicked() {
    }

    @OnClick(R.id.buttonCancel)
    public void onButtonCancelClicked() {
        dismiss();
    }

    @OnClick(R.id.imageViewCross)
    public void onImageViewCrossClicked() {
        dismiss();
    }
}
