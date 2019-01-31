package com.bincee.parent.dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bincee.parent.R;
import com.bincee.parent.activity.MyLocationActivity;
import com.bincee.parent.base.BDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.location.LocationManager.PROVIDERS_CHANGED_ACTION;

public class LocateMeDialog extends BDialog {


    @BindView(R.id.buttonOK)
    Button buttonOK;
    @BindView(R.id.imageViewCross)
    ImageView imageViewCross;
    private BroadcastReceiver broadcastReceiver;

    public LocateMeDialog(Context context) {
        super(context);
        int layout = R.layout.dialog_locate_me;
        View view = getLayoutInflater().inflate(layout, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);

    }


    @Override
    public void show() {
        super.show();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {

//                    Log.i(TAG, "Location Providers Changed");


                    checkGPS(context);


//                    Toast.makeText(context, "GPS Enabled: " + isGpsEnabled + " Network Location Enabled: " + isNetworkEnabled, Toast.LENGTH_LONG).show();


                }
            }
        };
        getContext().registerReceiver(broadcastReceiver, new IntentFilter(PROVIDERS_CHANGED_ACTION));

        checkGPS(getContext());


    }

    private void checkGPS(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        buttonOK.setEnabled(isGpsEnabled);

    }

    @Override
    public void dismiss() {
        getContext().unregisterReceiver(broadcastReceiver);
        super.dismiss();
    }

    @OnClick(R.id.buttonOK)
    public void onButtonOKClicked() {

        MyLocationActivity.start(getContext());
        dismiss();

    }

    @OnClick(R.id.imageViewCross)
    public void onImageViewCrossClicked() {
        dismiss();
    }
}
