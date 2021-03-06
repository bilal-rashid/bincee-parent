package com.bincee.parent;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.gsonparserfactory.GsonParserFactory;
import com.bincee.parent.api.EndPoints;
import com.bincee.parent.api.model.AlertsModel;
import com.bincee.parent.api.model.AnnouncementModel;
import com.bincee.parent.api.model.LoginResponse;
import com.bincee.parent.helper.MyPref;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.bincee.parent.api.EndPoints.BaseUrl;


public class MyApp extends Application {
    public static MyApp instance;
    public static EndPoints endPoints;
    private static Toast toast;
    public Gson gson;

    public MutableLiveData<LoginResponse.User> user = new MutableLiveData<>();

    public List<AnnouncementModel.SingleAnnouncement> announcementList = new ArrayList<>();
    public List<AlertsModel.EnclosingData> alertList = new ArrayList<>();

    public static void showToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(instance, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        user.setValue(MyPref.GET_USER(getApplicationContext()));

//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread thread, Throwable throwable) {
//                new AlertDialog.Builder(MyApp.this)
//                        .setMessage(throwable.getMessage() + throwable.getCause())
//                        .create()
//                        .show();
//            }
//        });
        instance = this;
        setupRetrofit();


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        overRideSustemFont();


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        overRideSustemFont();

    }

    private void overRideSustemFont() {
        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = 1f;

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }

    private void setupRetrofit() {


        OkHttpClient client = new OkHttpClient
                .Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(chain -> {
                    Request.Builder builder = chain.request().newBuilder();
                    LoginResponse.User user = MyApp.instance.user.getValue();
                    if (user != null && user.token != null) {
                        builder.addHeader("Authorization", "Bearer " + user.token);
                    }
                    Request newRequest = builder
                            .build();
                    return chain.proceed(newRequest);

                })
                .build();
        gson = new Gson();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        endPoints = retrofit.create(EndPoints.class);

        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.setParserFactory(new GsonParserFactory());

    }
}
