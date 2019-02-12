package com.bincee.parent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bincee.parent.activity.ContectUsActivity;
import com.bincee.parent.activity.MyLocationActivity;
import com.bincee.parent.activity.ProfileActivity;
import com.bincee.parent.activity.SplashActivity;
import com.bincee.parent.api.FireStoreHelper;
import com.bincee.parent.api.model.LoginResponse;
import com.bincee.parent.api.model.ParentCompleteData;
import com.bincee.parent.api.model.Student;
import com.bincee.parent.base.BA;
import com.bincee.parent.databinding.ActivityHomeBinding;
import com.bincee.parent.dialog.DriverInformationDialog;
import com.bincee.parent.dialog.LogoutDialog;
import com.bincee.parent.fragment.AlertsFragment;
import com.bincee.parent.fragment.CalenderFragment;
import com.bincee.parent.fragment.StudentSSFragment;
import com.bincee.parent.helper.ImageBinder;
import com.bincee.parent.helper.MyPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BA {

    public static final String MY_PROFILE = "- My Profile";
    public static final String HOME = "- Home";
    private static final String MY_KIDs_Profile = "- My Kids Profile";
    public static final String ALERTS_AND_ANNOUNCEMENT = "- Alerts and Announcement";
    public static final String SETTINGS = "- Settings";
    public static final String DRIVERS_PROFILE = "- Drivers Profile";
    public static final String ABOUT_US = "- About Us";
    public static final String CONTACT_US = "- Contact Us";
    public static final String LOCATE_ME = "- Locate Me";


    public ActivityHomeBinding binding;

    private List<String> menuItem;
    MutableLiveData<LoginResponse.User> liveUse = new MutableLiveData<>();
    private String TAG = HomeActivity.class.getSimpleName();
    private VM liveData;
    private Handler handler = new Handler();

    public static void start(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: ");
        checkNotificationForStudent(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");


        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.toolbarLayout.textViewTitle.setText("Home");

        liveData = ViewModelProviders.of(this).get(VM.class);

        liveUse.setValue(MyApp.instance.user.getValue());
        liveUse.observe(this, new Observer<LoginResponse.User>() {
            @Override
            public void onChanged(LoginResponse.User user) {
                binding.userLayout.textViewUsername.setText(user.username);
            }
        });


        setSupportActionBar(binding.toolbarLayout.toolbar);


        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.threee_line);


        menuItem = new ArrayList<String>();

//        menuItem.add(HOME);
        menuItem.add(MY_PROFILE);
//        menuItem.add(MY_KIDs_Profile);
//        menuItem.add(ALERTS_AND_ANNOUNCEMENT);
//        menuItem.add(SETTINGS);
        menuItem.add(DRIVERS_PROFILE);
        menuItem.add(CONTACT_US);
        menuItem.add(LOCATE_ME);
//        menuItem.add(ABOUT_US);

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setLayoutFrozen(true);
        binding.recycleView.setAdapter(new RecyclerView.Adapter<NavigationVH>() {
            @NonNull
            @Override
            public NavigationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new NavigationVH(LayoutInflater.from(HomeActivity.this).inflate(R.layout.naviation_row, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull NavigationVH holder, int position) {
                holder.bind();

            }

            @Override
            public int getItemCount() {
                return menuItem.size();
            }
        });

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
//
            switch (menuItem.getItemId()) {
                case R.id.bottmnavigation_summarizedsize:

                    goHomeUrDrunk();

                    break;


                case R.id.bottmnavigation_calender:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout, new CalenderFragment())
                            .commit();
                    break;

                case R.id.bottmnavigation_alerts:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout, new AlertsFragment())
                            .commit();
            }
            return true;
        });

        binding.bottomNavigationView.setItemIconTintList(null);


        binding.textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        MyApp.instance.user.observe(this, new Observer<LoginResponse.User>() {
            @Override
            public void onChanged(LoginResponse.User user) {
                if (user == null) return;
                ImageBinder.roundedCornerCenterCorpParent(binding.userLayout.imageViewProfilePic, user.parentCompleteInfo.photo);
                binding.userLayout.textViewUsername.setText(user.parentCompleteInfo.fullname);

            }
        });

        goHomeUrDrunk();


        checkNotificationForStudent(getIntent());


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


    private void checkNotificationForStudent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null) return;


        int studenId = Integer.parseInt(extras.getString(Student.STUDENT_ID, "-1"));
        String type = (extras.getString(Student.NOTIFICATION_TYPE, "-1"));

        if (type.equalsIgnoreCase("2") || type.equalsIgnoreCase("1")) {

            if (studenId != -1) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        StudentSSFragment.getInstance().setCurrentStudent(studenId);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                StudentSSFragment.getInstance().onButtonFindMeClicked();
                            }
                        }, 500);

                    }
                }, 1000);
            }
        }
    }

    private void logout() {
        binding.drawerLayout.closeDrawer(Gravity.LEFT);

        new LogoutDialog(this)
                .setListner(new LogoutDialog.Listner() {
                    @Override
                    public void _logout() {

                        MyPref.logout(HomeActivity.this);
                        MyPref.logout(HomeActivity.this);

                        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                                if (task.isSuccessful()) {

                                    String token = task.getResult().getToken();
                                    FireStoreHelper.tokenCollection(MyApp.instance.user.getValue().id + "")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                    if (task.isSuccessful()) {

                                                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                                                        for (DocumentSnapshot tokenSnapShot : documents) {

                                                            if (tokenSnapShot.getString("token").equalsIgnoreCase(token)) {
                                                                tokenSnapShot.getReference().delete();
                                                            }

                                                        }
                                                    }


                                                    Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                }
                            }
                        });


                    }

                    @Override
                    public void _cancel() {

                    }
                })
                .show();


    }

    private void goHomeUrDrunk() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, StudentSSFragment.getInstance())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            binding.drawerLayout.openDrawer(Gravity.LEFT, true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class VM extends ViewModel {


        public VM() {


        }
    }

    public class NavigationVH extends RecyclerView.ViewHolder {
        @BindView(R.id.textView)
        TextView textView;

        public NavigationVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind() {
            textView.setText(menuItem.get(getAdapterPosition()));
        }

        @OnClick(R.id.textView)
        public void onMenuItemClicked() {

            binding.drawerLayout.closeDrawer(Gravity.LEFT);
            String s = textView.getText().toString();
            if (s.equalsIgnoreCase(HOME)) {

                goHomeUrDrunk();

            } else if (s.equalsIgnoreCase(MY_PROFILE)) {

                ProfileActivity.start(HomeActivity.this);

            } else if (s.equalsIgnoreCase(HOME)) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.frameLayout, HomeFragment.getInstance())
//                        .commit();
            } else if (s.equalsIgnoreCase(CONTACT_US)) {
                ContectUsActivity.start(HomeActivity.this);

            } else if (s.equalsIgnoreCase(ALERTS_AND_ANNOUNCEMENT)) {
                binding.bottomNavigationView.setSelectedItemId(R.id.bottmnavigation_alerts);
            } else if (s.equalsIgnoreCase(DRIVERS_PROFILE)) {

                ParentCompleteData.DriverModel driver = StudentSSFragment.getInstance().currentKid.driver;
                new DriverInformationDialog(HomeActivity.this, driver)
                        .setListner(new DriverInformationDialog.Listner() {
                            @Override
                            public void call() {

                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + driver.phoneNo));
                                startActivity(intent);
                            }

                            @Override
                            public void cancel() {
                                Uri sms_uri = Uri.parse("smsto:" + driver.phoneNo);
                                Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                                sms_intent.putExtra("sms_body", "");
                                startActivity(sms_intent);
                            }
                        })
                        .show();

            } else if (s.equalsIgnoreCase(LOCATE_ME)) {
                MyLocationActivity.start(HomeActivity.this);
            }

        }
    }

}
