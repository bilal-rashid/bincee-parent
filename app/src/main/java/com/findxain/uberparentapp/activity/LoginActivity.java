package com.findxain.uberparentapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.findxain.uberparentapp.HomeActivity;
import com.findxain.uberparentapp.R;
import com.findxain.uberparentapp.base.BA;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.res.ResourcesCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BA {

    @BindView(R.id.buttonLogin)
    Button buttonLogin;
    @BindView(R.id.editText)
    AppCompatCheckBox editText;

    public static void start(SplashActivity splashActivity) {
        splashActivity.startActivity(new Intent(splashActivity, LoginActivity.class));
        splashActivity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
//        new DriverInformationDialog(this).show();
//        new AlertDialog(this).show();

        editText.setTypeface(ResourcesCompat.getFont(this, R.font.gotham_book));
    }

    @OnClick(R.id.buttonLogin)
    public void onViewClicked() {
        HomeActivity.start(this);
    }
}
