package com.bincee.parent.activity;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bincee.parent.MyApp;
import com.bincee.parent.R;
import com.bincee.parent.api.model.ParentCompleteData;
import com.bincee.parent.api.model.Ride;
import com.bincee.parent.api.model.Student;
import com.bincee.parent.base.BA;
import com.bincee.parent.fragment.SummarizedStatusFragment;
import com.bincee.parent.helper.ImageBinder;
import com.bincee.parent.helper.LatLngHelper;
import com.bincee.parent.helper.PermissionHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.gson.Gson;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.bincee.parent.api.model.ParentCompleteData.KidModel.KID;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class MapActivity extends BA implements OnMapReadyCallback {

    public static final int DELAY = 60 * 1000;


    public static final int REQUEST_CODE = 669;
    private static final long DURATION = 3000;
    private IconFactory getInstance;


    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.imageViewPic)
    ImageView imageViewPic;
    @BindView(R.id.textViewName)
    TextView textViewName;
    @BindView(R.id.textViewNameKM)
    TextView textViewNameKM;
    @BindView(R.id.textViewTime)
    TextView textViewTime;
    //    @BindView(R.id.)
//    FrameLayout toolbarFrameLayout;
    String LINE_SOURCE = "line-source";
    String LINE_LAYER = "linelayer";
    Timer timer = new Timer();
    private MapboxMap mapboxMap;
    private Icon iconBusMyLoc;
    private String BUS_ICON_LAYER = "bus-icon-layer";
    private String BUS_ICON_SOURCE = "bus-icon_source";
    private String BUS_ICON = "bus-icon";
    private ParentCompleteData.KidModel kidModel;
    private Marker busMarker;
    private Marker destinationMarker;
    private PermissionHelper permissionHelper;

    public static String getToken() {

        //client
        return "pk.eyJ1IjoiYmluY2VlIiwiYSI6ImNqc2E2Nm0wYjAwaGM0OXFjd3kxazBnNmYifQ.Wnu7rjFfU_qpl1Pmi062vg";
//        return "pk.eyJ1IjoiZmluZHhhaW4iLCJhIjoiY2pxOTY1bjY3MTMwYjQzbDEwN3h2aTdsbCJ9.fKLD1_UzlMIWhXfUZ3aRYQ";

    }


    public static void start(Activity context, ParentCompleteData.KidModel currentKid) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra(KID, new Gson().toJson(currentKid));
        context.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getToken());
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        getInstance = IconFactory.getInstance(MapActivity.this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        textViewTitle.setText("REAL TIME TRACKING");
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


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
        timer.cancel();
        timer.purge();
        timer = null;
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

        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{ACCESS_FINE_LOCATION}, 44);
            return;
        }


        permissionHelper = new PermissionHelper();
        permissionHelper.with(this).requiredPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}).setListner(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                String kidString = getIntent().getStringExtra(KID);
                kidModel = new Gson().fromJson(kidString, ParentCompleteData.KidModel.class);


                if (kidModel != null) {
                    fetchData(kidModel, 0);

                } else {
                    MyApp.showToast("Null Kid");
                }
            }

            @Override
            public void onPermissionFailed() {

            }
        }).permissionId(55).request();


        FeatureCollection featureCollection =
                FeatureCollection.fromFeatures(new Feature[]{});

        Source geoJsonSource = new GeoJsonSource(LINE_SOURCE, featureCollection);
        mapboxMap.addSource(geoJsonSource);
        LineLayer lineLayer = new LineLayer(LINE_LAYER, LINE_SOURCE);


        lineLayer.setProperties(
                PropertyFactory.lineDasharray(new Float[]{0.01f, 3f}),
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                PropertyFactory.lineWidth(3f),
                PropertyFactory.lineColor(getResources().getColor(R.color.sky_blue)));


        mapboxMap.addLayer(lineLayer);


        FeatureCollection iconFeatureCollection = FeatureCollection.fromFeatures(new Feature[]{
        });

        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(BUS_ICON_SOURCE, iconFeatureCollection);
        mapboxMap.addSource(iconGeoJsonSource);

        mapboxMap.addImage(BUS_ICON, BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.bus_icon_green)));

        SymbolLayer startEndIconLayer = new SymbolLayer(BUS_ICON_LAYER, BUS_ICON_SOURCE);
        startEndIconLayer.setProperties(
                iconImage(BUS_ICON),
                iconIgnorePlacement(true),
                iconIgnorePlacement(true));

        mapboxMap.addLayer(startEndIconLayer);


//        getHomeActivity().liveData.myLocaton.observe(getViewLifecycleOwner(), new Observer<Location>() {
//            @Override
//            public void onChanged(Location location) {
//
//
//                LatLng nowLocation = LatLngHelper.toLatLng(location);
//
//                FeatureCollection busCollectionSource = FeatureCollection.fromFeatures(new Feature[]{
//                        Feature.fromGeometry(Point.fromLngLat(nowLocation.getLongitude(), nowLocation.getLatitude())),
////                Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))
//
//                });
//
//                GeoJsonSource busSource = mapboxMap.getSourceAs(BUS_ICON_SOURCE);
//
//                SymbolLayer busLayer = mapboxMap.getLayerAs(BUS_ICON_LAYER);
//
//
//                if (oldLocation != null) {
//
//                    double displacemnet = oldLocation.distanceTo(nowLocation);
//
//                    if (displacemnet > 1) {
//                        double nowBearing = bearingBetweenLocations(oldLocation, nowLocation);
//
////                    smothRotation(busLayer, nowBearing, lastBearing);
////                    animateMarker(oldLocation, nowLocation, busSource);
//
////                        markerView.setPosition(nowLocation);
//                        markerView.setRotation((float) nowBearing);
//
//
//                        ValueAnimator rotationAnimator = new ValueAnimator();
//                        rotationAnimator.setObjectValues(Float.parseFloat(lastBearing + ""), nowBearing);
//                        rotationAnimator.setDuration(DURATION);
//                        rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//                            @Override
//                            public void onAnimationUpdate(ValueAnimator animator) {
//
//                                markerView.setRotation((Float) animator.getAnimatedValue());
//
//                            }
//                        });
//                        rotationAnimator.start();
//
//
//                        ValueAnimator markerAnimator = ValueAnimator.ofObject(new LatLngEvaluator(), (Object[]) new LatLng[]{oldLocation, nowLocation});
//                        markerAnimator.setDuration(DURATION);
//                        markerAnimator.setRepeatCount(0);
//                        markerAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//                        markerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                            @Override
//                            public void onAnimationUpdate(ValueAnimator animation) {
//                                if (markerView != null) {
//                                    markerView.setPosition((LatLng) animation.getAnimatedValue());
//                                }
//                            }
//                        });
//                        markerAnimator.start();
//
//
//                        lastBearing = nowBearing;
//                    }
//
//
//                } else {
////                    busSource.setGeoJson(busCollectionSource);
//                    markerView = mapboxMap.addMarker(new MarkerViewOptions().icon(iconBusMyLoc).position(nowLocation));
//
//                }
//                oldLocation = nowLocation;
//
//                if (directionRoute != null) {
//                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds.Builder()
//                            .includes(LatLngHelper.toLatLng(directionRoute.routeOptions().coordinates()))
//                            .build(), 5));
//                }
//
//            }
//        });
//
//
//        getHomeActivity().liveData.currentRoute.observe(getViewLifecycleOwner(), new Observer<DirectionsRoute>() {
//            @Override
//            public void onChanged(DirectionsRoute directionsRoute) {
////                mapboxMap.removeAnnotations();
//                setupRoute(directionsRoute);
////                getHomeActivity().liveData.currentRoute.removeObserver(this);
////                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocationMarker.getPosition(),14));
//
//                MapFragment.this.directionRoute = directionsRoute;
//                if (directionsRoute != null) {
//                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds.Builder()
//                            .includes(LatLngHelper.toLatLng(directionsRoute.routeOptions().coordinates()))
//                            .build(), padding));
//                }
//
//            }
//        });


    }

    private void fetchData(ParentCompleteData.KidModel kidModel, int delay) {
        if (isFinishing()) return;
        if (isDestroyed()) return;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                FirebaseFirestore.getInstance()
                        .collection("ride")
                        .document(kidModel.driverId + "")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (isDestroyed()) return;
                                if (task.isSuccessful()) {


                                    Ride ride = task.getResult().toObject(Ride.class);


                                    if (ride != null) {


                                        Student student = SummarizedStatusFragment.getCurrentSutend(kidModel, ride.students);
                                        if (student != null) {
                                            setupStudent(ride, student);
                                        }

                                        fetchData(kidModel, DELAY);

                                        setupBusLocation(ride);


                                    } else {
                                        finish();
//                                        fetchData(kidModel, 60 * 1000);

                                    }


                                } else {
                                    fetchData(kidModel, DELAY);
                                }

                            }
                        });

            }
        }, delay);
    }

    private void setupBusLocation(Ride ride) {

        updateusMrker(ride.latLng);
    }

    private void setupStudent(Ride ride, Student student) {

        textViewName.setText(student.fullname + "");
        textViewTime.setText("ETA: " + (student.duration != null ? Math.round(student.duration) : 0) + " min");
        ImageBinder.roundedCornerCenterCorpKid(imageViewPic, student.photo);

        if (ride.shift.equalsIgnoreCase(Ride.SHIFT_MORNING)) {

            if (student.status == 1) {
                getRoute(LatLngHelper.toPoint(ride.latLng), Point.fromLngLat(student.lng, student.lat), student);

            }
//
//            else if (student.status == Student.STATUS_MORNING_ONTHEWAY) {
//
//                getRoute(LatLngHelper.toPoint(ride.latLng), LatLngHelper.toPoint(ride.schoolLatLng), student);
//
//            }

            else {
                getRoute(LatLngHelper.toPoint(ride.latLng), LatLngHelper.toPoint(ride.schoolLatLng), student);

            }


        } else if (ride.shift.equalsIgnoreCase(Ride.SHIFT_AFTERNOON)) {

            getRoute(LatLngHelper.toPoint(ride.latLng), Point.fromLngLat(student.lng, student.lat), student);


        }

    }


    private void getRoute(Point origin, Point destination, Student student) {

        if (origin == null) {
            MyApp.showToast("Buss Location Not found");
            return;
        }
        MapboxDirections.Builder builder = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(getToken());


        MapboxDirections client = builder
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {


                if (isDestroyed()) return;
                DirectionsRoute directionsRoute = response.body().routes().get(0);

                showRoute(directionsRoute, origin, destination, student);

                fetchData(kidModel, DELAY);


            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                fetchData(kidModel, DELAY);


                throwable.printStackTrace();
                Toast.makeText(MapActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


//        List<Point> points = new ArrayList<>();
//        for (Student point : wayPoints) {
//            points.add(Point.fromLngLat(point.lng, point.lat));
//        }

//        MapboxMatrix directionsMatrixClient = MapboxMatrix.builder()
//                .accessToken(MAPBOX_TOKEN)
//                .profile(DirectionsCriteria.PROFILE_DRIVING)
//                .coordinates(points)
//                .build();
//        directionsMatrixClient.enqueueCall(new Callback<MatrixResponse>() {
//            @Override
//            public void onResponse(Call<MatrixResponse> call, Response<MatrixResponse> response) {
//
//
//            }
//
//            @Override
//            public void onFailure(Call<MatrixResponse> call, Throwable t) {
//
//                t.printStackTrace();
//            }
//        });


    }

    private void showRoute(DirectionsRoute currentRoute, Point origin, Point destination, Student student) {


        List<Point> points = LineString.fromPolyline(currentRoute.geometry(), Constants.PRECISION_6).coordinates();


        LineString lineString = LineString.fromLngLats(points);
        FeatureCollection featureCollection =
                FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(lineString)});


        GeoJsonSource lineSource = mapboxMap.getSourceAs(LINE_SOURCE);

        lineSource.setGeoJson(featureCollection);


//        Point busLatLng = Point.fromLngLat(origin.longitude(), origin.latitude());


        //dont show because already shown
//        updateusMrker(origin);

        String title = "ETA: " + (student.duration != null ? Math.round(student.duration) : 0) + " min";

        if (destinationMarker == null) {

            destinationMarker = mapboxMap.addMarker(new MarkerOptions()
                    .setPosition(LatLngHelper.toLatLng(destination))
                    .setIcon(getInstance.defaultMarker())
                    .setTitle(title));

        } else {
            destinationMarker.setPosition(LatLngHelper.toLatLng(destination));
            destinationMarker.setTitle(title);

        }

        mapboxMap.selectMarker(destinationMarker);


        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds.Builder()
                .include(destinationMarker.getPosition())
                .include(busMarker.getPosition())
                .build(), 50));


    }

    private void updateusMrker(GeoPoint origin) {
        if (busMarker == null) {
            LatLng busLocation = LatLngHelper.toLatLng(origin);

            busMarker = mapboxMap.addMarker(new MarkerOptions()
                    .setPosition(busLocation)
                    .setIcon(getInstance.fromResource(R.drawable.bus_icon_green))
                    .setTitle("Bus"));
        } else {

//            busMarker.setPosition(LatLngHelper.toLatLng(origin));

            LatLng oldPosition = busMarker.getPosition();
            LatLng newPosition = LatLngHelper.toLatLng(origin);

            ValueAnimator markerAnimator = ValueAnimator.ofObject(new LatLngEvaluator(), (Object[]) new LatLng[]{oldPosition, newPosition});
            markerAnimator.setDuration(DURATION);
            markerAnimator.setRepeatCount(0);
            markerAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            markerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (busMarker != null) {

                        busMarker.setPosition((LatLng) animation.getAnimatedValue());

                    }
                }
            });
            markerAnimator.start();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private static class LatLngEvaluator implements TypeEvaluator<LatLng> {

        // Method is used to interpolate the marker animation.

        private LatLng latLng = new LatLng();

        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            latLng.setLatitude(startValue.getLatitude()
                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
            latLng.setLongitude(startValue.getLongitude()
                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
            return latLng;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }
}
