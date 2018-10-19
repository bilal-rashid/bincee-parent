package com.findxain.uberparentapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.findxain.uberparentapp.activity.ContectUsActivity;
import com.findxain.uberparentapp.activity.ProfileActivity;
import com.findxain.uberparentapp.base.BA;
import com.findxain.uberparentapp.dialog.FeedbackDialog;
import com.findxain.uberparentapp.dialog.FinishRideDialog;
import com.findxain.uberparentapp.dialog.LocateMeDialog;
import com.findxain.uberparentapp.fragment.AlertsFragment;
import com.findxain.uberparentapp.fragment.CalenderFragment;
import com.findxain.uberparentapp.fragment.StudentSSFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    public static final String FAQ = "- FAQ";
    public static final String ABOUT_US = "- About Us";
    public static final String CONTACT_US = "- Contact US";


    @BindView(R.id.imageViewProfilePic)
    ImageView imageViewProfilePic;
    @BindView(R.id.textViewUsername)
    TextView textViewUsername;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.bottomNavigationView)
    public BottomNavigationView bottomNavigationView;
    private List<String> menuItem;

    public static void start(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textViewTitle.setText("Home");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.threee_line);


        menuItem = new ArrayList<String>();

        menuItem.add(HOME);
        menuItem.add(MY_PROFILE);
//        menuItem.add(MY_KIDs_Profile);
        menuItem.add(ALERTS_AND_ANNOUNCEMENT);
//        menuItem.add(SETTINGS);
//        menuItem.add(DRIVERS_PROFILE);
        menuItem.add(CONTACT_US);
//        menuItem.add(FAQ);
//        menuItem.add(ABOUT_US);

        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setLayoutFrozen(true);
        recycleView.setAdapter(new RecyclerView.Adapter<NavigationVH>() {
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


        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
//
            switch (menuItem.getItemId()) {
                case R.id.bottmnavigation_summarizedsize:

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout, new StudentSSFragment())
                            .commit();

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

        bottomNavigationView.setItemIconTintList(null);


        new LocateMeDialog(this).show();

        new FinishRideDialog(this).show();

        new FeedbackDialog(this).show();


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new StudentSSFragment())
                .commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(Gravity.LEFT, true);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

            drawerLayout.closeDrawer(Gravity.LEFT);
            if (textView.getText().toString().equalsIgnoreCase(MY_PROFILE)) {

                ProfileActivity.start(HomeActivity.this);

            } else if (textView.getText().toString().equalsIgnoreCase(HOME)) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.frameLayout, HomeFragment.getInstance())
//                        .commit();
            } else if (textView.getText().toString().equalsIgnoreCase(CONTACT_US)) {
                ContectUsActivity.start(HomeActivity.this);

            } else if (textView.getText().toString().equalsIgnoreCase(ALERTS_AND_ANNOUNCEMENT)) {
                bottomNavigationView.setSelectedItemId(R.id.bottmnavigation_alerts);
            }

        }
    }
}
