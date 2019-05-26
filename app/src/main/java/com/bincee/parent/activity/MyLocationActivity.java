package com.bincee.parent.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.bincee.parent.activity.MapActivity.getToken;

public class MyLocationActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {

    @BindView(R.id.buttonSkyBlue)
    Button buttonSkyBlue;

    @BindView(R.id.mapView)
    MapView mapView;

    @BindView(R.id.layout_loader)
    LinearLayout loader;

    @BindView(R.id.ic_myLocation)
    ImageView mylocationIcon;

    @BindView(R.id.editTextSearchLocation)
    EditText searchLocation;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private CompositeDisposable compositeDisposable;
    private MyProgressDialog progressDialog;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 1000;  /* 1 sec */
    private long FASTEST_INTERVAL = 500; /* 1/2 sec */

    public static void start(Context context) {
        context.startActivity(new Intent(context, MyLocationActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getToken());
        compositeDisposable = new CompositeDisposable();


        setContentView(R.layout.activity_my_location);
        ButterKnife.bind(this);


        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


    }

    int AUTOCOMPLETE_REQUEST_CODE = 1;
    @OnClick(R.id.btnGetLocation)
    public void onGetLocation() {
        enableLocationComponent();
    }

    @OnClick(R.id.editTextSearchLocation)
    public void onSearchLocation() {
//        enableLocationComponent();
// Set the fields to specify which types of place data to
// return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG);

// Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                double latitude = place.getLatLng().latitude;
                double longitude = place.getLatLng().longitude;
                searchLocation.setText(place.getName());
                final int min = 1;
                final int max = 180;
                final int random = new Random().nextInt((max - min) + 1) + min;
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(latitude, longitude)) // Sets the new camera position
                        .zoom(17) // Sets the zoom
                        .bearing(random) // Rotate the camera
                        .tilt(30) // Set the camera tilt
                        .build(); // Creates a CameraPosition from the builder

                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position), 7000);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
    @OnClick(R.id.buttonSkyBlue)
    public void onViewClicked() {
        LatLng myCameraLocation = new LatLng(0,0);
        myCameraLocation.setLatitude(mapboxMap.getCameraPosition().target.getLatitude());
        myCameraLocation.setLongitude(mapboxMap.getCameraPosition().target.getLongitude());

        progressDialog = new MyProgressDialog(MyLocationActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Wait..");
        progressDialog.show();

        EndpointObserver<MyResponse> endpointObserver = MyApp.endPoints.updateLocation(MyApp.instance.user.getValue().id + "", myCameraLocation.getLatitude(), myCameraLocation.getLongitude())
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

                            user.parentCompleteInfo.lat = myCameraLocation.getLatitude();
                            user.parentCompleteInfo.lng = myCameraLocation.getLongitude();

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
                                }).show();
                    }
                });

        compositeDisposable.add(endpointObserver);

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

        LoginResponse.User user = MyPref.GET_USER(MyLocationActivity.this);
        mapboxMap.getUiSettings().setAllGesturesEnabled(true);
        mapboxMap.getUiSettings().setCompassEnabled(true);
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(user.parentCompleteInfo.lat, user.parentCompleteInfo.lng)) // Sets the new camera position
                .zoom(17) // Sets the zoom
                .bearing(180) // Rotate the camera
                .tilt(0) // Set the camera tilt
                .build();
        mapboxMap.setCameraPosition(position);
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
            } else {
                loader.setVisibility(View.VISIBLE);
                startLocationUpdates();
            }

//
//// Get an instance of the component
//            LocationComponent locationComponent = mapboxMap.getLocationComponent();
//
//// Activate with options
//            locationComponent.activateLocationComponent(this);
//
//// Enable to make component visible
//            locationComponent.setLocationComponentEnabled(true);
//
//// Set the component's camera mode
//            locationComponent.setCameraMode(CameraMode.TRACKING);
//
//// Set the component's render mode
//            locationComponent.setRenderMode(RenderMode.COMPASS);

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressLint("RestrictedApi")
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        final LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        onLocationChanged(locationResult.getLastLocation());
                        LocationServices.getFusedLocationProviderClient(MyLocationActivity.this).removeLocationUpdates(this);
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        loader.setVisibility(View.GONE);
        mylocationIcon.setVisibility(View.VISIBLE);
        final int min = 1;
        final int max = 180;
        final int random = new Random().nextInt((max - min) + 1) + min;
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude())) // Sets the new camera position
                .zoom(17) // Sets the zoom
                .bearing(random) // Rotate the camera
                .tilt(30) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder

        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 4000);

    }

}
