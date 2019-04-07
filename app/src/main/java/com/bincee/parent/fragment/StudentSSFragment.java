package com.bincee.parent.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bincee.parent.HomeActivity;
import com.bincee.parent.MyApp;
import com.bincee.parent.R;
import com.bincee.parent.api.model.LoginResponse;
import com.bincee.parent.api.model.ParentCompleteData;
import com.bincee.parent.api.model.Ride;
import com.bincee.parent.api.model.Student;
import com.bincee.parent.dialog.DriverInformationDialog;
import com.bincee.parent.helper.ImageBinder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.littlemango.stacklayoutmanager.StackLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bincee.parent.activity.MapActivity.REQUEST_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentSSFragment extends Fragment implements EventListener<DocumentSnapshot> {


    private static StudentSSFragment studentSSFragment;
    @BindView(R.id.infiniteCycleView)
    RecyclerView infiniteCycleView;
    @BindView(R.id.imageViewName)
    ImageView imageView2;
    @BindView(R.id.textViewAddress)
    TextView textViewAddress;
    @BindView(R.id.textViewStatus)
    TextView textViewStatus;
    public MutableLiveData<Ride> ride;
    @BindView(R.id.textViewETA)
    TextView textViewETA;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.buttonFindMe)
    Button buttonFindMe;
    @BindView(R.id.buttonCalender)
    Button buttonCalender;


    public ParentCompleteData.KidModel currentKid;
    //    private List<ParentCompleteData.KidModel> kidsArray;
    int[] images = {R.drawable.checkbox_checked, R.drawable.checkbox_checked};


    @BindView(R.id.imageViewCall)
    ImageView imageViewCall;
    private StackViewAdapter stackViewAdapter;
    private int student_ss_row = R.layout.student_ss_row;
    private StackLayoutManager layout;
    @BindView(R.id.imageViewStatusIcon)
    ImageView imageViewStatusIcon;
    private ListenerRegistration currentStudenSnapSHotListner;
    private SummarizedStatusFragment fragment;


    public static StudentSSFragment getInstance() {
        if (studentSSFragment == null) {
            studentSSFragment = new StudentSSFragment();
        }
        return studentSSFragment;
    }

    @OnClick(R.id.imageViewCall)
    public void onCAllClicked() {
        if (stackViewAdapter.getKids().size() != 0) {
            if (currentKid == null && stackViewAdapter.getItemCount() != 0) {
                currentKid = stackViewAdapter.getKids().get(0);
            }
            new DriverInformationDialog(getContext(), currentKid.driver).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No drivers present", Toast.LENGTH_SHORT).show();
        }
    }


    public StudentSSFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ride = new MutableLiveData<>();

        stackViewAdapter = new StackViewAdapter(new ArrayList<>());


        layout = new StackLayoutManager(StackLayoutManager.ScrollOrientation.RIGHT_TO_LEFT, 3);
        layout.setPagerMode(true);


        layout.setItemChangedListener(new StackLayoutManager.ItemChangedListener() {
            @Override
            public void onItemChanged(int position) {

                changeCurrentStudent(position);
            }
        });
        layout.setItemOffset(100);
//        layout.setVisibleItemCount(3);


    }

    private void changeCurrentStudent(int position) {

        currentKid = stackViewAdapter.getKids().get(position);
        textView5.setText(currentKid.fullname);
        textViewAddress.setText(MyApp.instance.user.getValue().parentCompleteInfo.school.name);
        stopSnapSHotListner();

        currentStudenSnapSHotListner = FirebaseFirestore.getInstance().collection("ride")
                .document(currentKid.driverId + "")
                .addSnapshotListener(this);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSnapSHotListner();
    }

    private void stopSnapSHotListner() {
        if (currentStudenSnapSHotListner != null) {
            currentStudenSnapSHotListner.remove();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_s, container, false);
        ButterKnife.bind(this, view);
        infiniteCycleView.setLayoutManager(layout);
        infiniteCycleView.setAdapter(stackViewAdapter);


//        MyApp.instance.user.observe(this, new Observer<LoginResponse.User>() {
//            @Override
//            public void onChanged(LoginResponse.User user) {
//                stackViewAdapter.setKids(user.parentCompleteInfo.kids);
//
//
//            }
//        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        MyApp.instance.user.observe(getViewLifecycleOwner(), new Observer<LoginResponse.User>() {
            @Override
            public void onChanged(LoginResponse.User user) {
                if (user != null) {
                    List<ParentCompleteData.KidModel> kids = user.parentCompleteInfo.kids;

                    stackViewAdapter.kids = kids;
                    stackViewAdapter.notifyDataSetChanged();
                    changeCurrentStudent(layout.getFirstVisibleItemPosition());

                }

            }
        });

        ride.observe(getViewLifecycleOwner(), new Observer<Ride>() {
            @Override
            public void onChanged(Ride ride) {

                if (ride != null && (ride.shiftId == currentKid.shiftEvening || ride.shiftId == currentKid.shiftMorning)) {


                    if (ride.students.size() > 0) {


                        Student student = SummarizedStatusFragment.getCurrentSutend(currentKid, ride.students);
                        if (student != null) {

                            if (student.id == currentKid.id) {


                                if (ride.rideInProgress) {


                                    if (student.present == Student.UNKNOWN) {
                                        enableFIndMe(true);

                                        populaeStatusesAcordingly(ride, student);

                                    } else if (student.present == Student.PRESENT) {

                                        markPresent();
                                        populaeStatusesAcordingly(ride, student);


                                    } else if (student.present == Student.ABSENT) {
                                        markAbsent();
                                    }


                                } else {

                                    rideNotPResent();

                                }

                                return;
                            }

                        } else {

                            disableFindMe();


                        }


                    } else {
                        disableFindMe();

                    }


                } else {


                    disableFindMe();

                    getActivity().finishActivity(REQUEST_CODE);


                }
            }
        });


//        showReachedFragemnt();
    }

    public void showReachedFragemnt() {
        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack("")
                .replace(R.id.frameLayout, KidArrivedFragment.getInstance())
                .commit();
    }

    @OnClick(R.id.buttonFindMe)
    public void onButtonFindMeClicked() {

        fragment = SummarizedStatusFragment.getInstance();

        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frameLayout, fragment)
                .commit();


    }

    @OnClick(R.id.buttonCalender)
    public void onButtonCalenderClicked() {
        ((HomeActivity) Objects.requireNonNull(getActivity())).binding.bottomNavigationView.setSelectedItemId(R.id.bottmnavigation_calender);
    }

    public void setCurrentStudent(int studenId) {

        for (int i = 0; i < stackViewAdapter.getKids().size(); i++) {
            if (stackViewAdapter.getKids().get(i).id == studenId) {

                infiniteCycleView.smoothScrollToPosition(i);
                return;
            }
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        setTitle();

    }

    public void setTitle() {
        ((HomeActivity) Objects.requireNonNull(getActivity())).textViewTitle.setText("SUMMARIZED STATUS");
    }

    @Override
    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {

        Ride ride = documentSnapshot.toObject(Ride.class);

        this.ride.setValue(ride);


    }

    private void populaeStatusesAcordingly(Ride ride, Student student) {

        imageViewStatusIcon.setVisibility(View.VISIBLE);
        if (student.duration != null && student.duration > 0) {
            textViewETA.setText(String.format("ETA: %d min", Math.round(student.duration)));
        } else {
//            textViewETA.setText(String.format("ETA: %d min", null));

        }


        if (ride.shift.equalsIgnoreCase(Ride.SHIFT_MORNING)) {

            switch (student.status) {


                case Student.STATUS_MORNING_BUS_IS_COMMING:
                    textViewStatus.setText("Bus is coming");
                    break;
                case Student.STATUS_MORNING_ATYOURLOCATION:
                    textViewStatus.setText("Bus is here");
                    textViewETA.setText("");
                    imageViewStatusIcon.setVisibility(View.GONE);


                    break;
                case Student.STATUS_MORNING_ONTHEWAY:
                    textViewStatus.setText("On the way");

                    break;
                case Student.STATUS_MORNING_REACHED:
                    textViewStatus.setText("Reached");
                    enableFIndMe(false);
                    imageViewStatusIcon.setVisibility(View.GONE);
                    textViewETA.setText("");
                    break;
                default:
                    textViewStatus.setText("");

                    break;
            }


        } else if (ride.shift.equalsIgnoreCase(Ride.SHIFT_AFTERNOON)) {
            switch (student.status) {
                case Student.STATUS_AFTERNOON_SCHOOLISOVER:
                    textViewStatus.setText("School is over");
                    textViewETA.setText("");


                    break;
                case Student.STATUS_AFTERNOON_INTHEBUS:
                    textViewStatus.setText("In the bus");

                    break;
                case Student.STATUS_AFTERNOON_ALMOSTTHERE:
                    textViewStatus.setText("Almost there");

                    break;
                case Student.STATUS_AFTERNOON_ATYOURDOORSTEP:
                    textViewStatus.setText("At your door step");
                    enableFIndMe(false);
                    imageViewStatusIcon.setVisibility(View.GONE);
                    textViewETA.setText("");
                    break;
                default:
                    textViewStatus.setText("");

                    break;
            }

        }


//        if (student.duration != null && student.duration > 0) {
//            textViewETA.setText(String.format("ETA: %d min", Math.round(student.duration)));
//            imageViewStatusIcon.setVisibility(View.VISIBLE);
//        } else {
//            imageViewStatusIcon.setVisibility(View.VISIBLE);
//            textViewETA.setText(String.format("ETA: %d min", null));
//
//        }
//        //TODO

    }

    private void disableFindMe() {
        enableFIndMe(false);
        textViewStatus.setText("");
        imageViewStatusIcon.setVisibility(View.GONE);
        textViewETA.setText("");
    }

    private void markPresent() {

        enableFIndMe(true);

    }

    private void enableFIndMe(boolean b) {
        buttonFindMe.setEnabled(b);
    }

    private void markAbsent() {

        enableFIndMe(false);
        textViewStatus.setText("Absent");
        imageViewStatusIcon.setVisibility(View.VISIBLE);
        textViewETA.setText("");
    }

    private void rideNotPResent() {
        disableFindMe();


    }

    public class StackViewAdapter extends RecyclerView.Adapter<VH> {

        private List<ParentCompleteData.KidModel> kids = new ArrayList<>();

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            holder.bind(kids.get(holder.getAdapterPosition()));

        }

        public StackViewAdapter(List<ParentCompleteData.KidModel> kids) {
            this.kids = kids;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            VH vh = new VH(getLayoutInflater().inflate(R.layout.student_ss_row, parent, false));
            return vh;
        }

        public List<ParentCompleteData.KidModel> getKids() {
            return kids;
        }

        @Override
        public int getItemCount() {
            return kids.size();
        }

        public void setKids(List<ParentCompleteData.KidModel> kids) {
            this.kids = kids;
            notifyDataSetChanged();
        }
    }


    public class VH extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;

        public VH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ParentCompleteData.KidModel kidModel) {

            ImageBinder.roundedCornerCenterCorpKid(imageView, kidModel.photo);
        }
    }


}
