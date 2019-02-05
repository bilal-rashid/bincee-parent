package com.bincee.parent.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bincee.parent.HomeActivity;
import com.bincee.parent.MyApp;
import com.bincee.parent.R;
import com.bincee.parent.api.model.AlertsAndAnnoucementModel;
import com.bincee.parent.api.model.AlertsModel;
import com.bincee.parent.api.model.AnnouncementModel;
import com.bincee.parent.base.BFragment;
import com.bincee.parent.customview.MyProgress;
import com.bincee.parent.observer.EndpointObserver;
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
public class AlertsFragment extends BFragment {

    @BindView(R.id.progressBar)
    MyProgress progressBar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private FragmentStatePagerAdapter adapter;
    @BindView(R.id.imageViewBG)
    ImageView imageViewBG;
    List<AlertsModel.EnclosingData> alertListModel = new ArrayList<>();
    List<AnnouncementModel.SingleAnnouncement> announcementList = new ArrayList<>();
    int currentPage = 0;


    public AlertsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new AlertsVPFragment(currentPage);
            }

            @Override
            public int getCount() {
                if (currentPage == 0) {
                    return alertListModel.size();
                } else {
                    return announcementList.size();
                }
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Emergency Alerts";
                    default:
                        return "School Announcements";
                }
            }
        };
        this.callApiForAlerts();
    }
    public void callApiForAlerts()

    {
        compositeDisposable.add(MyApp.endPoints
                .getAlerts(String.valueOf(MyApp.instance.parentCompleteInfo.schoolId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new EndpointObserver<AlertsModel>() {


                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onData(AlertsModel o) throws Exception {
                    if(o.status == 200)
                    {
                        alertListModel = o.data ;
                        MyApp.instance.alertList = alertListModel;
                        callApiForAnnoucements();
//                        adapter.notifyDataSetChanged();
                    }
                    }

                    @Override
                    public void onHandledError(Throwable e) {

                    }
                }));
    }
    public void callApiForAnnoucements()
    {
        compositeDisposable.add(MyApp.endPoints
                .getAnnouncements(String.valueOf(MyApp.instance.parentCompleteInfo.parentId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new EndpointObserver<AnnouncementModel>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onData(AnnouncementModel o) throws Exception {
                        if(o.status == 200)
                        {
                            announcementList = o.data ;
                            MyApp.instance.announcementList = announcementList;
//                            callApiForAnnoucements();
                        adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onHandledError(Throwable e) {

                    }
                }));
    }
//    public void callApiForAlertsAndAnnoucements()
//    {
//        callApiForAlerts();
////        progressBar.setVisibility(View.VISIBLE);
////        compositeDisposable.add(MyApp.endPoints
////                .getAlertsAndAnnouncements(String.valueOf(MyApp.instance.user.id))
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribeWith(new EndpointObserver<AlertsAndAnnoucementModel>() {
////
////                    @Override
////                    public void onComplete() {
//////                    progressBar.setVisibility(View.GONE);
////                    }
////
////                    @Override
////                    public void onData(AlertsAndAnnoucementModel o) throws Exception {
////                        if(o.status == 200)
////                        {
////                            alertListModel = o.data.emergencyAlerts;
////                            announcementList = o.data.schoolAnnouncements;
////                            MyApp.instance.announcementList = announcementList;
////                            MyApp.instance.alertList = alertListModel;
////                            adapter.notifyDataSetChanged();
////                        }
////                    }
////
////                    @Override
////                    public void onHandledError(Throwable e) {
////
////                    }
////                }));
//    }
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
                currentPage = position;

                if (position == 0) {

                    imageViewBG.setImageResource(R.drawable.alerts_bg_a);

                } else {
                    imageViewBG.setImageResource(R.drawable.alerts_bg_b);

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
