package com.findxain.uberparentapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.findxain.uberparentapp.HomeActivity;
import com.findxain.uberparentapp.MyApp;
import com.findxain.uberparentapp.R;
import com.findxain.uberparentapp.api.model.LoginResponse;
import com.findxain.uberparentapp.api.model.MyResponse;
import com.findxain.uberparentapp.api.model.ParentCompleteData;
import com.findxain.uberparentapp.api.model.Student;
import com.findxain.uberparentapp.base.BA;
import com.findxain.uberparentapp.customview.MyProgress;
import com.findxain.uberparentapp.observer.EndpointObserver;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BA {

    @BindView(R.id.buttonLogin)
    Button buttonLogin;
    @BindView(R.id.editTextUsername)
    TextInputEditText editTextUsername;
    @BindView(R.id.editTextPassword)
    TextInputEditText editTextPassword;
    @BindView(R.id.checkBox)
    AppCompatCheckBox checkBox;
    @BindView(R.id.progressBar)
    MyProgress progressBar;


    public static void start(SplashActivity splashActivity) {
        splashActivity.startActivity(new Intent(splashActivity, LoginActivity.class));
        splashActivity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        editTextUsername.setText("testparent1");
        editTextPassword.setText("testparent1");

        checkBox.setTypeface(ResourcesCompat.getFont(this, R.font.gotham_book));
    }

    @OnClick(R.id.buttonLogin)
    public void onViewClicked() {
        progressBar.setVisibility(View.VISIBLE);
//        HomeActivity.start(LoginActivity.this);
//        return;


        compositeDisposable.add(MyApp.endPoints
                .login(editTextUsername.getText().toString(), editTextPassword.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new EndpointObserver<LoginResponse>() {
                    @Override
                    public void onComplete() {


                    }

                    @Override
                    public void onData(LoginResponse response) {
                        if (response.status == 200) {

                            response.data.save(LoginActivity.this);
                            MyApp.instance.user = response.data;
                            String parentId = String.valueOf(MyApp.instance.user.id);
                            callForParentData(parentId);
//                            Comment by arslan
                        } else {


                            MyApp.showToast(response.data.message);
                        }
                    }

                    @Override
                    public void onHandledError(Throwable e) {
                        e.printStackTrace();
                        MyApp.showToast(e.getMessage());

                    }
                }));
    }
    void callForParentData(String parentId)
    {
        compositeDisposable.add(MyApp.endPoints
                .getParentCompleteData(parentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new EndpointObserver<MyResponse<ParentCompleteData>>() {


                    @Override
                    public void onComplete() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onData(MyResponse<ParentCompleteData> response) throws Exception {
                        if(response.status == 200)
                        {
                            MyApp.instance.parentCompleteInfo = response.data;
                            HomeActivity.start(LoginActivity.this);
                            finish();

                            Log.e("Fullname", MyApp.instance.parentCompleteInfo.fullname);

//                            MyApp.instance.parentCompleteInfo
                        }
                    }

                    @Override
                    public void onHandledError(Throwable e) {

                    }
                }));
    }
}
