package com.findxain.uberparentapp.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.findxain.uberparentapp.HomeActivity;
import com.findxain.uberparentapp.MyApp;
import com.findxain.uberparentapp.R;
import com.findxain.uberparentapp.api.model.ParentCompleteData;
import com.findxain.uberparentapp.dialog.DriverInformationDialog;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.gigamole.infinitecycleviewpager.OnInfiniteCyclePageTransformListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentSSFragment extends Fragment {


    @BindView(R.id.infiniteCycleView)
    HorizontalInfiniteCycleViewPager infiniteCycleView;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.textViewAddress)
    TextView textViewAddress;
    @BindView(R.id.textViewStatus)
    TextView textViewStatus;
    @BindView(R.id.textViewStatus2)
    TextView textViewStatus2;
    @BindView(R.id.imageView4)
    ImageView imageView4;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.buttonFindMe)
    Button buttonFindMe;
    @BindView(R.id.buttonCalender)
    Button buttonCalender;
    @BindView(R.id.textViewSwipe)
    TextView textViewSwipe;
    private MyAdapter adpater;
    ParentCompleteData.KidModel currentKid;
    String driverId;
    private List<ParentCompleteData.KidModel> kidsArray ;
    int[] images = {R.drawable.checkbox_checked, R.drawable.checkbox_checked};


    @BindView(R.id.imageViewCall)
    ImageView imageViewCall;

    @OnClick(R.id.imageViewCall)
    public void onCAllClicked() {
        if (kidsArray.size() != 0) {
            if (currentKid == null && kidsArray.size() != 0) {
                currentKid = kidsArray.get(0);
            }
            new DriverInformationDialog(getContext(), currentKid.driver).show();
        }
        else
        {
            Toast.makeText(getActivity().getApplicationContext(),"No drivers present",Toast.LENGTH_SHORT).show();
        }
    }


    public StudentSSFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adpater = new MyAdapter();
        kidsArray = new ArrayList<>();
        kidsArray = MyApp.instance.parentCompleteInfo.kids;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_s, container, false);
        ButterKnife.bind(this, view);
        infiniteCycleView.setAdapter(adpater);
        infiniteCycleView.stopAutoScroll();


        infiniteCycleView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("Page Change",String.valueOf(position));
                currentKid = kidsArray.get(position);
                textView5.setText(currentKid.fullname);
                textViewAddress.setText(MyApp.instance.parentCompleteInfo.address.toString());
                driverId = currentKid.driverId.toString();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


//        infiniteCycleView.setMinPageScale(0.8f);
//        infiniteCycleView.setMaxPageScale(0.8f);


        return view;
    }

    @OnClick(R.id.buttonFindMe)
    public void onButtonFindMeClicked() {

        ((AppCompatActivity) getActivity())
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new SummarizedStatusFragment())
                .commit();

    }

    @OnClick(R.id.buttonCalender)
    public void onButtonCalenderClicked() {
        ((HomeActivity) Objects.requireNonNull(getActivity())).binding.bottomNavigationView.setSelectedItemId(R.id.bottmnavigation_calender);
    }


    private class MyAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return kidsArray.size();
        }


        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
            imageView.setImageResource(R.drawable.girl_profile_icon);
            container.addView(imageView);
            currentKid = kidsArray.get(0);
            textView5.setText(currentKid.fullname.toString());
            textViewAddress.setText(MyApp.instance.parentCompleteInfo.address.toString());
            return imageView;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {


            return view.equals(object);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) Objects.requireNonNull(getActivity())).textViewTitle.setText("SUMMARIZED STATUS");

    }


}
