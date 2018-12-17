package com.findxain.uberparentapp.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.findxain.uberparentapp.HomeActivity;
import com.findxain.uberparentapp.MyApp;
import com.findxain.uberparentapp.R;
import com.findxain.uberparentapp.api.model.AlertsAndAnnoucementModel;
import com.findxain.uberparentapp.customview.MyProgress;
import com.findxain.uberparentapp.observer.EndpointObserver;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlertsFragment extends Fragment {

    @BindView(R.id.progressBar)
    MyProgress progressBar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private FragmentStatePagerAdapter adapter;
    @BindView(R.id.imageViewBG)
    ImageView imageViewBG;
    protected CompositeDisposable compositeDisposable;
    List<AlertsAndAnnoucementModel.AlertModel> alertListModel = new ArrayList<>();
    List<AlertsAndAnnoucementModel.AnnouncementModel> announcementList = new ArrayList<>();
    int currentPage = 0;


    public AlertsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        adapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new AlertsVPFragment();
            }

            @Override
            public int getCount() {
                if (currentPage == 0) {
                    return announcementList.size();
                }
                else
                {
                    return alertListModel.size();
                }
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Emergency Alerts";
                    default:
                        return "School Alerts";
                }
            }
        };
        callApiForAlertsAndAnnoucements();
    }
    public void callApiForAlertsAndAnnoucements()
    {
//        progressBar.setVisibility(View.VISIBLE);
        compositeDisposable.add(MyApp.endPoints
                .getAlertsAndAnnouncements(String.valueOf(MyApp.instance.user.id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new EndpointObserver<AlertsAndAnnoucementModel>() {

                    @Override
                    public void onComplete() {
//                    progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onData(AlertsAndAnnoucementModel o) throws Exception {
                        if(o.status == 200)
                        {
                            alertListModel = o.data.emergencyAlerts;
                            announcementList = o.data.schoolAnnouncements;
                            MyApp.instance.announcementList = announcementList;
                            MyApp.instance.alertList = alertListModel;
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onHandledError(Throwable e) {

                    }
                }));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alerts, container, false);
        ButterKnife.bind(this, view);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) Objects.requireNonNull(getActivity())).textViewTitle.setText("ALERTS AND ANNOUNCEMENT");

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    imageViewBG.setImageResource(R.drawable.alerts_bg_a);
                } else {
                    imageViewBG.setImageResource(R.drawable.alerts_bg_b);

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
