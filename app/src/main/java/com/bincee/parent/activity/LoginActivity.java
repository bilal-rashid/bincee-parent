package com.bincee.parent.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.res.ResourcesCompat;

import com.bincee.parent.HomeActivity;
import com.bincee.parent.MyApp;
import com.bincee.parent.R;
import com.bincee.parent.api.FireStoreHelper;
import com.bincee.parent.api.model.LoginResponse;
import com.bincee.parent.api.model.MyResponse;
import com.bincee.parent.api.model.ParentCompleteData;
import com.bincee.parent.base.BA;
import com.bincee.parent.customview.MyProgress;
import com.bincee.parent.helper.MyPref;
import com.bincee.parent.observer.EndpointObserver;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;

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
    @BindView(R.id.textViewPassword)
    TextView textViewPassword;
    private String TAG = LoginActivity.class.getSimpleName();


    public static void start(SplashActivity splashActivity) {
        splashActivity.startActivity(new Intent(splashActivity, LoginActivity.class));
        splashActivity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        editTextUsername.setText(MyPref.getUSER_NAME(this));
        editTextPassword.setText(MyPref.getPASSWORD(this));

        checkBox.setChecked(MyPref.getREMEMBERME(this));


//        editTextUsername.setText("test_parentp1");
//        editTextPassword.setText("Changeme.1");
//
        //        editTextUsername.setText("arslan_parentp3");
//        editTextPassword.setText("ChangeMe@4");


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

                        progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onData(LoginResponse response) {
                        if (response.status == 200) {
                            if (response.data.type != 4) {
                                new AlertDialog.Builder(LoginActivity.this).setMessage("Only Parent can login").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                                return;
                            }

                            MyApp.instance.user.setValue(response.data);
                            String parentId = String.valueOf(MyApp.instance.user.getValue().id);
                            callForParentData(parentId);
                            saveTokenToFirebase();


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

    private void saveTokenToFirebase() {


        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {

                        HashMap<String, Object> data = new HashMap<>();
                        LoginResponse.User user = MyApp.instance.user.getValue();

                        String token = instanceIdResult.getToken();
                        data.put("token", token);
                        data.put("userId", user.id);
                        Log.e("FcmToken", token);

                        FireStoreHelper.tokenCollection(user.id + "")
                                .add(data)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "Token Updated");

                                    }
                                });

                    }
                });


    }

    void callForParentData(String parentId) {
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
                        if (response.status == 200) {

                            MyApp.instance.user.getValue().parentCompleteInfo = response.data;
                            HomeActivity.start(LoginActivity.this);
                            MyPref.SAVE_USER(LoginActivity.this, MyApp.instance.user.getValue());
                            if (checkBox.isChecked()) {
                                MyPref.SAVE_CREDATIALS(LoginActivity.this
                                        , editTextUsername.getText().toString()
                                        , editTextPassword.getText().toString());
                            } else {
                                MyPref.REMOVE_REMEBER(LoginActivity.this);

                            }

                            finish();

                        } else {
                            throw new Exception(response.status + "");
                        }
                    }

                    @Override
                    public void onHandledError(Throwable e) {

                        e.printStackTrace();
                        MyApp.showToast(e.getMessage());
                    }
                }));
    }

    @OnClick(R.id.textViewPassword)
    public void onPasswordClthicked() {
        ForgetPasswordActivity.start(this);
    }
}
