package com.bincee.parent.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bincee.parent.HomeActivity;
import com.bincee.parent.MyApp;
import com.bincee.parent.R;
import com.bincee.parent.api.model.ParentCompleteData;
import com.bincee.parent.dialog.DriverInformationDialog;
import com.bumptech.glide.Glide;
import com.littlemango.stacklayoutmanager.StackLayoutManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentSSFragment extends Fragment {


    private static StudentSSFragment studentSSFragment;
    @BindView(R.id.infiniteCycleView)
    RecyclerView infiniteCycleView;
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
    public ParentCompleteData.KidModel currentKid;
    String driverId;
    private List<ParentCompleteData.KidModel> kidsArray;
    int[] images = {R.drawable.checkbox_checked, R.drawable.checkbox_checked};


    @BindView(R.id.imageViewCall)
    ImageView imageViewCall;
    private StackViewAdapter stackViewAdapter;
    private int student_ss_row = R.layout.student_ss_row;
    private StackLayoutManager layout;

    public static StudentSSFragment getInstance() {
        if (studentSSFragment == null) {
            studentSSFragment = new StudentSSFragment();
        }
        return studentSSFragment;
    }

    @OnClick(R.id.imageViewCall)
    public void onCAllClicked() {
        if (kidsArray.size() != 0) {
            if (currentKid == null && kidsArray.size() != 0) {
                currentKid = kidsArray.get(0);
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

        adpater = new MyAdapter();
        kidsArray = new ArrayList<>();
        kidsArray = MyApp.instance.parentCompleteInfo.kids;
        stackViewAdapter = new StackViewAdapter(kidsArray);

        layout = new StackLayoutManager();


        layout.setItemChangedListener(new StackLayoutManager.ItemChangedListener() {
            @Override
            public void onItemChanged(int position) {

                changeCurrentStudent(position);
            }
        });


    }

    private void changeCurrentStudent(int position) {
        currentKid = kidsArray.get(position);
        textView5.setText(currentKid.fullname);
        textViewAddress.setText(MyApp.instance.parentCompleteInfo.address.toString());
        driverId = currentKid.driverId.toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_s, container, false);
        ButterKnife.bind(this, view);
        infiniteCycleView.setLayoutManager(layout);
        infiniteCycleView.setAdapter(stackViewAdapter);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeCurrentStudent(layout.getFirstVisibleItemPosition());

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

    public class StackViewAdapter extends RecyclerView.Adapter<VH> {

        private List<ParentCompleteData.KidModel> kids = new ArrayList<>();

        public StackViewAdapter(List<ParentCompleteData.KidModel> kids) {
            this.kids = kids;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            VH vh = new VH(getLayoutInflater().inflate(R.layout.student_ss_row, parent, false));
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {

        }

        @Override
        public int getItemCount() {
            return kids.size();
        }
    }


    public class VH extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;

        public VH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
