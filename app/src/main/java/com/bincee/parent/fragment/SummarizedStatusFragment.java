package com.bincee.parent.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.bincee.parent.HomeActivity;
import com.bincee.parent.R;
import com.bincee.parent.StatusTextView;
import com.bincee.parent.activity.MapActivity;
import com.bincee.parent.api.model.Ride;
import com.bincee.parent.api.model.Student;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class SummarizedStatusFragment extends Fragment {


    public static Fragment instance;


    @BindView(R.id.buttonRealTimeTracking)
    Button buttonRealTimeTracking;
    @BindView(R.id.statusTextView2)
    StatusTextView statusTextView2;
    @BindView(R.id.statusTextView4)
    StatusTextView statusTextView4;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.checkBox1)
    ImageView checkBox1;
    @BindView(R.id.statusTextView1)
    StatusTextView statusTextView1;
    @BindView(R.id.statusTextView3)
    StatusTextView statusTextView3;
    @BindView(R.id.checkBox2)
    ImageView checkBox2;
    @BindView(R.id.checkBox3)
    ImageView checkBox3;
    @BindView(R.id.checkBox4)
    ImageView checkBox4;
    @BindView(R.id.imageViewBusStauts)
    ImageView imageViewBusStauts;

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

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Fragment parentFragment = getParentFragment();

        if (parentFragment instanceof StudentSSFragment) {
            StudentSSFragment studentSSFragment = (StudentSSFragment) parentFragment;

            studentSSFragment.ride.observe(getViewLifecycleOwner(), new Observer<Ride>() {
                @Override
                public void onChanged(Ride ride) {

                    if (ride == null) return;


                    for (Student student : ride.students) {

                        if (ride.shift.equalsIgnoreCase(Ride.SHIFT_MORNING)) {
                            morningStatuses(student);

                            switch (student.status) {
                                case 1:
                                    checkBox1.setImageResource(R.drawable.checkbox_checked);
                                    statusTextView1.selected();

                                    break;
                                case 2:
                                    checkBox1.setImageResource(R.drawable.checkbox_checked);
                                    checkBox2.setImageResource(R.drawable.checkbox_checked);

                                    statusTextView1.selected();
                                    statusTextView2.selected();


                                    break;
                                case 3:
                                    checkBox1.setImageResource(R.drawable.checkbox_checked);
                                    checkBox2.setImageResource(R.drawable.checkbox_checked);
                                    checkBox3.setImageResource(R.drawable.checkbox_checked);

                                    statusTextView1.selected();
                                    statusTextView2.selected();
                                    statusTextView3.selected();

                                    break;
                                case 4:
                                    checkBox1.setImageResource(R.drawable.checkbox_checked);
                                    checkBox2.setImageResource(R.drawable.checkbox_checked);
                                    checkBox3.setImageResource(R.drawable.checkbox_checked);
                                    checkBox4.setImageResource(R.drawable.checkbox_checked);

                                    statusTextView1.selected();
                                    statusTextView2.selected();
                                    statusTextView3.selected();
                                    statusTextView4.selected();

                                    break;
                                default:

                                    break;
                            }


                        } else if (ride.shift.equalsIgnoreCase(Ride.SHIFT_AFTERNOON)) {

                            eveningStatuses(student);


                            switch (student.status) {
                                case 1:
                                    checkBox1.setImageResource(R.drawable.checkbox_checked);

                                    statusTextView1.selected();

                                    break;
                                case 2:
                                    checkBox1.setImageResource(R.drawable.checkbox_checked);
                                    checkBox2.setImageResource(R.drawable.checkbox_checked);


                                    statusTextView1.selected();
                                    statusTextView2.selected();

                                    break;
                                case 3:
                                    checkBox1.setImageResource(R.drawable.checkbox_checked);
                                    checkBox2.setImageResource(R.drawable.checkbox_checked);
                                    checkBox3.setImageResource(R.drawable.checkbox_checked);


                                    statusTextView1.selected();
                                    statusTextView2.selected();
                                    statusTextView3.selected();
                                    break;
                                case 4:
                                    checkBox1.setImageResource(R.drawable.checkbox_checked);
                                    checkBox2.setImageResource(R.drawable.checkbox_checked);
                                    checkBox3.setImageResource(R.drawable.checkbox_checked);
                                    checkBox4.setImageResource(R.drawable.checkbox_checked);
                                    statusTextView1.selected();
                                    statusTextView2.selected();
                                    statusTextView3.selected();
                                    statusTextView4.selected();
                                    break;
                                default:

                                    break;

                            }

                        }


                        return;

                    }


                }
            });

        }


    }

    public void morningStatuses(Student student) {
        statusTextView1.textViewTitle.setText("Bus is coming");
        statusTextView1.textViewText.setText("Bus is on its way to pickup " + student.fullname + " and will be there in " + (student.duration != null ? Math.round(student.duration) : 0) + " minutes");
        statusTextView1.unSelected();


        statusTextView2.textViewTitle.setText("Bus is here");
        statusTextView2.textViewText.setText("Bus has arrived to pickup " + student.fullname + " and will leave in " + (student.duration != null ? Math.round(student.duration) : 0) + " minutes");
        statusTextView2.view.setGravity(GravityCompat.END);
        statusTextView2.textViewText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        statusTextView2.unSelected();

        statusTextView3.textViewTitle.setText("On the way");
        statusTextView3.textViewText.setText("Bus is on the way to school and will be there in " + (student.duration != null ? Math.round(student.duration) : 0) + " minutes");
        statusTextView3.unSelected();

        statusTextView4.textViewTitle.setText("Reached");
        statusTextView4.textViewText.setText("Bus has reached the school");
        statusTextView4.view.setGravity(GravityCompat.END);
        statusTextView4.textViewText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        statusTextView4.unSelected();


        unchechAllCheckox();
    }

    private void unchechAllCheckox() {
        checkBox1.setImageResource(R.drawable.checkbox_unchecked);
        checkBox2.setImageResource(R.drawable.checkbox_unchecked);
        checkBox3.setImageResource(R.drawable.checkbox_unchecked);
        checkBox4.setImageResource(R.drawable.checkbox_unchecked);
    }


    public void eveningStatuses(Student student) {

        statusTextView1.textViewTitle.setText("School is over");
        statusTextView1.textViewText.setText("School is over and bus is waiting for " + student.fullname + " to hop in");
        statusTextView1.unSelected();

        statusTextView2.textViewTitle.setText("In the bus");
        statusTextView2.textViewText.setText(student.fullname + " is in the bus and will reach in around  " + (student.duration != null ? Math.round(student.duration) : 0) + " minutes");
        statusTextView2.view.setGravity(GravityCompat.END);
        statusTextView2.textViewText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        statusTextView4.unSelected();


        statusTextView3.textViewTitle.setText("Almost There");
        statusTextView3.textViewText.setText(student.fullname + " will reach home in " + (student.duration != null ? Math.round(student.duration) : 0) + " minutes");
        statusTextView4.unSelected();

        statusTextView4.textViewTitle.setText("At your doorstep");
        statusTextView4.textViewText.setText("Please open the door " + student.fullname + " is waiting outside");
        statusTextView4.view.setGravity(GravityCompat.END);
        statusTextView4.textViewText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        statusTextView4.unSelected();


        unchechAllCheckox();

    }

    @OnClick(R.id.buttonRealTimeTracking)
    public void onViewClicked() {

//        ((HomeActivity) getActivity()).getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.frameLayout, new RealTimeTrackingFragment())
//                .commit();


        StudentSSFragment parentFragment = (StudentSSFragment) getParentFragment();
        MapActivity.start(getActivity(), parentFragment.currentKid);


    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) Objects.requireNonNull(getActivity())).textViewTitle.setText("BUS STATUS");

    }
}
