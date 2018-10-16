package com.findxain.uberparentapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.findxain.uberparentapp.HomeActivity;
import com.findxain.uberparentapp.R;
import com.findxain.uberparentapp.base.BA;
import com.findxain.uberparentapp.dialog.AlertDialog;
import com.findxain.uberparentapp.dialog.DriverInformationDialog;
import com.findxain.uberparentapp.dialog.LocateMeDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BA {

    @BindView(R.id.buttonLogin)
    Button buttonLogin;

    public static void start(SplashActivity splashActivity) {
        splashActivity.startActivity(new Intent(splashActivity, LoginActivity.class)) ;
        splashActivity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        new DriverInformationDialog(this).show();
//        new AlertDialog(this).show();
    }

    @OnClick(R.id.buttonLogin)
    public void onViewClicked() {
        HomeActivity.start(this);
    }
}
