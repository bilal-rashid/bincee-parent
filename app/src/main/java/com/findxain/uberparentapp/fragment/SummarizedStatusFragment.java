package com.findxain.uberparentapp.fragment;


import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.findxain.uberparentapp.R;
import com.findxain.uberparentapp.StatusTextView;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class SummarizedStatusFragment extends Fragment {


    public static Fragment instance;
    @BindView(R.id.statusTextViewAtLocation)
    StatusTextView statusTextViewAtLocation;
    @BindView(R.id.statusTextViewReached)
    StatusTextView statusTextViewReached;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.checkBoxTestLeftForSchool)
    ImageView checkBoxTestLeftForSchool;
    @BindView(R.id.statusTextViewLeftFOrSchool)
    StatusTextView statusTextViewLeftFOrSchool;
    @BindView(R.id.statusTextViewOnTheWay)
    StatusTextView statusTextViewOnTheWay;
    @BindView(R.id.checkBoxAtTheLocation)
    ImageView checkBoxAtTheLocation;
    @BindView(R.id.checkBoxOnTheWay)
    ImageView checkBoxOnTheWay;
    @BindView(R.id.checkBoxReached)
    ImageView checkBoxReached;
    @BindView(R.id.imageViewBusStauts)
    ImageView imageViewBusStauts;
    @BindView(R.id.buttonRealTimeTracking)
    Button buttonRealTimeTracking;

    public SummarizedStatusFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance() {
        if (instance == null) {
            instance = new SummarizedStatusFragment();
        }
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summarized_status_fragemnt, container, false);
        ButterKnife.bind(this, view);

        statusTextViewLeftFOrSchool.textViewTitle.setText("Left for school");
        statusTextViewLeftFOrSchool.textViewText.setText("Driver is on his way to pickup Ahmad and will be there in 00:15 minutes");

        statusTextViewAtLocation.textViewTitle.setText("At the Location");
        statusTextViewAtLocation.textViewText.setText("Driver is at your location to pickup Ahmad");
        statusTextViewAtLocation.view.setGravity(GravityCompat.END);
        statusTextViewAtLocation.textViewText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

        statusTextViewOnTheWay.textViewTitle.setText("On the way");
        statusTextViewOnTheWay.textViewText.setText("Driver is on the way to school and will be there in 00:50 minutes");

        statusTextViewReached.textViewTitle.setText("Reached");
        statusTextViewReached.textViewText.setText("Driver has reached the school");
        statusTextViewReached.view.setGravity(GravityCompat.END);
        statusTextViewReached.textViewText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);


        return view;
    }

    @OnClick(R.id.buttonRealTimeTracking)
    public void onViewClicked() {
    }
}
