package com.bincee.parent.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bincee.parent.MyApp;
import com.bincee.parent.R;
import com.bincee.parent.api.model.LoginResponse;
import com.bincee.parent.api.model.MyResponse;
import com.bincee.parent.dialog.MyProgressDialog;
import com.bincee.parent.helper.MyPref;
import com.bincee.parent.observer.EndpointObserver;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.bincee.parent.activity.MapActivity.MAPBOX_TOKEN;

public class MyLocationActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {

    @BindView(R.id.buttonSkyBlue)
    Button buttonSkyBlue;
    @BindView(R.id.mapView)
    MapView mapView;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private CompositeDisposable compositeDisposable;
    private MyProgressDialog progressDialog;
    private LocationManager locationManager;


    public static void start(Context context) {
        context.startActivity(new Intent(context, MyLocationActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, MAPBOX_TOKEN);
        compositeDisposable = new CompositeDisposable();


        setContentView(R.layout.activity_my_location);
        ButterKnife.bind(this);


        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }


    @OnClick(R.id.buttonSkyBlue)
    public void onViewClicked() {

        LocationEngine locationEngine = mapboxMap.getLocationComponent().getLocationEngine();
        if (locationEngine == null) return;
        @SuppressLint("MissingPermission") Location lastLocation = locationEngine.getLastLocation();

        if (lastLocation != null) {
            progressDialog = new MyProgressDialog(MyLocationActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Wait..");
            progressDialog.show();

            EndpointObserver<MyResponse> endpointObserver = MyApp.endPoints.updateLocation(MyApp.instance.user.getValue().id + "", lastLocation.getLatitude(), lastLocation.getLongitude())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new EndpointObserver<MyResponse>() {
                        @Override
                        public void onComplete() {
                            progressDialog.dismiss();


                        }

                        @Override
                        public void onData(MyResponse o) throws Exception {

                            if (o.status == 200) {
                                LoginResponse.User user = MyPref.GET_USER(MyLocationActivity.this);

                                user.parentCompleteInfo.lat = lastLocation.getLatitude();
                                user.parentCompleteInfo.lng = lastLocation.getLongitude();

                                MyPref.SAVE_USER(MyLocationActivity.this, user);
                                MyApp.instance.user.setValue(user);

                                MyApp.showToast("Location Updated");
                                finish();
                            } else {

                                throw new Exception(o.message);
                            }
                        }

                        @Override
                        public void onHandledError(Throwable e) {
                            progressDialog.dismiss();

                            new AlertDialog.Builder(MyLocationActivity.this)
                                    .setMessage(e.getMessage()).setCancelable(true)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            dialogInterface.dismiss();
                                        }
                                    });
                        }
                    });

            compositeDisposable.add(endpointObserver);


        } else {
            MyApp.showToast("Location Not Found");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        mapView.onStop();

        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        compositeDisposable.clear();
        compositeDisposable = null;
        mapView.onDestroy();
        super.onDestroy();


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        this.mapboxMap.getUiSettings().setRotateGesturesEnabled(false);

        mapboxMap.getUiSettings().setAllGesturesEnabled(true);
        mapboxMap.getUiSettings().setCompassEnabled(true);

        enableLocationComponent();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "Location Permission Required", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {

            enableLocationComponent();

        } else {
            Toast.makeText(this, "Failed to get Permission", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent() {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("GPS is disabled. would you like to enable it?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }


// Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

// Activate with options
            locationComponent.activateLocationComponent(this);

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }
}
