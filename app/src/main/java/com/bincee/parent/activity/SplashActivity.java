package com.bincee.parent.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bincee.parent.HomeActivity;
import com.bincee.parent.MyApp;
import com.bincee.parent.R;
import com.bincee.parent.api.model.FCMNotification;
import com.bincee.parent.api.model.LoginResponse;
import com.bincee.parent.api.model.Student;
import com.bincee.parent.helper.MyPref;
import com.google.firebase.messaging.RemoteMessage;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {


    @BindView(R.id.imageView)
    ImageView imageView;
    private int duration = 100;


    @Override// from,student_id
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        LoginResponse.User user = MyPref.GET_USER(SplashActivity.this);
        if (user != null) {
            Intent i = getIntent();
            Bundle bundle = i.getExtras();
            if(bundle!=null){
                String  from = bundle.getString("from");
                if(from != null) {
                    String student_id = "";
                    if (from.contains("school_" + user.parentCompleteInfo.schoolId)) {
                        FCMNotification notification = new FCMNotification();
                        notification.school = true;
                        notification.student = null;
                        MyPref.SaveNotification(SplashActivity.this, notification);
                    } else if (from.contains("parent_" + user.id)) {
                        student_id = bundle.getString("student_id");
                        FCMNotification notification = new FCMNotification();
                        notification.school = false;
                        notification.student = student_id;
                        MyPref.SaveNotification(SplashActivity.this, notification);
                    } else {
                        MyPref.SaveNotification(SplashActivity.this, null);
                    }
                }else {
                    MyPref.SaveNotification(SplashActivity.this, null);
                }
            }
        }

        AnimationDrawable frameAnimation = new AnimationDrawable();
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._1), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._2), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._3), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._4), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._5), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._6), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._7), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._8), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._9), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._10), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._11), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._12), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._13), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._14), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._15), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._16), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._17), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._18), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._19), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._20), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._21), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._22), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._23), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._24), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._25), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._26), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._27), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._28), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._29), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._30), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._31), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._32), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._33), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._34), duration);
        frameAnimation.addFrame(getResources().getDrawable(R.drawable._35), duration);
        frameAnimation.setOneShot(true);

        imageView.setImageDrawable(frameAnimation);
        frameAnimation.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginResponse.User user = MyPref.GET_USER(SplashActivity.this);
                if (user != null) {
                    MyApp.instance.user.setValue(user);
                    HomeActivity.start(SplashActivity.this);
                } else {
                    LoginActivity.start(SplashActivity.this);
                }
                finish();
            }
        }, duration * 35);

    }
}
