package com.findxain.uberparentapp.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.findxain.uberparentapp.HomeActivity;
import com.findxain.uberparentapp.R;
import com.findxain.uberparentapp.dialog.DriverInformationDialog;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    int[] images = {R.drawable.checkbox_checked, R.drawable.checkbox_checked};


    @BindView(R.id.imageViewCall)
    ImageView imageViewCall;

    @OnClick(R.id.imageViewCall)
    public void onCAllClicked() {
        new DriverInformationDialog(getContext()).show();
    }


    public StudentSSFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adpater = new MyAdapter();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_s, container, false);
        ButterKnife.bind(this, view);
        infiniteCycleView.setAdapter(adpater);
        infiniteCycleView.stopAutoScroll();
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
        ((HomeActivity) Objects.requireNonNull(getActivity())).bottomNavigationView.setSelectedItemId(R.id.bottmnavigation_calender);
    }


    private class MyAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return 3;
        }


        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
            imageView.setImageResource(R.drawable.girl_profile_icon);
            container.addView(imageView);

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
