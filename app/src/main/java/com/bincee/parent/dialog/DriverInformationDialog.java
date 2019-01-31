package com.bincee.parent.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bincee.parent.R;
import com.bincee.parent.api.model.ParentCompleteData;
import com.bincee.parent.base.BDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

public class DriverInformationDialog extends BDialog {

    @BindView(R.id.textViewDriverName)
    TextView driverName;
    @BindView(R.id.driverNumber)
    TextView driverNumber;
    @BindView(R.id.buttonLogout)
    Button buttonLogout;
    @BindView(R.id.buttonCancel)
    TextView buttonCancel;
    @BindView(R.id.imageViewCross)
    ImageView imageViewCross;
    CompositeDisposable compositeDisposable;
    public DriverInformationDialog(Context context, ParentCompleteData.DriverModel currentDriver) {
        super(context);
        compositeDisposable = new CompositeDisposable();

        int layout = R.layout.dialog_driver_info;
        View view = getLayoutInflater().inflate(layout, null, false);

        setContentView(view);
        ButterKnife.bind(this, view);
        updateViews(currentDriver);
    }
    public void updateViews(ParentCompleteData.DriverModel currentDriver)
    {
        driverName.setText(currentDriver.fullname.toString());
        driverNumber.setText(currentDriver.phoneNo.toString());
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
