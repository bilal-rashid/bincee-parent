package com.bincee.parent.fragment;


import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bincee.parent.HomeActivity;
import com.bincee.parent.R;
import com.bincee.parent.StatusTextView;

import java.util.Objects;

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
        eveningStatuses();



        return view;
    }
    public void morningStatuses()
    {
        statusTextViewLeftFOrSchool.textViewTitle.setText("Bus is coming");
        statusTextViewLeftFOrSchool.textViewText.setText("Bus is on its way to pickup (student name) and will be there in ETA (mins) minutes");

        statusTextViewAtLocation.textViewTitle.setText("Bus is here");
        statusTextViewAtLocation.textViewText.setText("Bus has arrived to pickup (student name) and will leave in 5 minutes");
        statusTextViewAtLocation.view.setGravity(GravityCompat.END);
        statusTextViewAtLocation.textViewText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

        statusTextViewOnTheWay.textViewTitle.setText("On the way");
        statusTextViewOnTheWay.textViewText.setText("Bus is on the way to school and will be there in (ETA) minutes");

        statusTextViewReached.textViewTitle.setText("Reached");
        statusTextViewReached.textViewText.setText("Bus has reached the school");
        statusTextViewReached.view.setGravity(GravityCompat.END);
        statusTextViewReached.textViewText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
    }
    public void eveningStatuses()
    {

            statusTextViewLeftFOrSchool.textViewTitle.setText("School is over");
            statusTextViewLeftFOrSchool.textViewText.setText("School is over and bus is waiting for (student name) to hop in");

            statusTextViewAtLocation.textViewTitle.setText("In the bus");
            statusTextViewAtLocation.textViewText.setText("(Student name) is in the bus and will reach in around ETA (eta) minutes");
            statusTextViewAtLocation.view.setGravity(GravityCompat.END);
            statusTextViewAtLocation.textViewText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

            statusTextViewOnTheWay.textViewTitle.setText("Almost There");
            statusTextViewOnTheWay.textViewText.setText("(Student name) will reach home in (eta) minutes");

            statusTextViewReached.textViewTitle.setText("At your doorstep");
            statusTextViewReached.textViewText.setText("Please open the door (Student name) is waiting outside");
            statusTextViewReached.view.setGravity(GravityCompat.END);
            statusTextViewReached.textViewText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

    }

    @OnClick(R.id.buttonRealTimeTracking)
    public void onViewClicked() {

        ((HomeActivity)getActivity()).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout,new RealTimeTrackingFragment())
                .commit();



    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) Objects.requireNonNull(getActivity())).textViewTitle.setText("BUS STATUS");

    }
}
