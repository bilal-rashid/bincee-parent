package com.findxain.uberparentapp.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findxain.uberparentapp.HomeActivity;
import com.findxain.uberparentapp.MyApp;
import com.findxain.uberparentapp.R;
import com.findxain.uberparentapp.activity.LoginActivity;
import com.findxain.uberparentapp.api.model.MyResponse;
import com.findxain.uberparentapp.api.model.ParentCompleteData;
import com.findxain.uberparentapp.api.model.StudentLeavesModel;
import com.findxain.uberparentapp.customview.MyProgress;
import com.findxain.uberparentapp.observer.EndpointObserver;
import com.savvi.rangedatepicker.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalenderFragment extends Fragment {
    protected CompositeDisposable compositeDisposable;


    @BindView(R.id.imageView7)
    ImageView imageView7;
    @BindView(R.id.calendar_view)
    CalendarPickerView calendarView;
    @BindView(R.id.buttonCalendar)
    Button buttonCalendar;
    @BindView(R.id.buttonHistory)
    Button buttonHistory;
    @BindView(R.id.textViewFaltuText)
    TextView textViewFaltuText;
    @BindView(R.id.buttonContinue)
    Button buttonContinue;
    @BindView(R.id.textViewLeaveHistory)
    TextView textViewLeaveHistory;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    private MyAdapter adapter;
    @BindView(R.id.progressBar)
    MyProgress progressBar;
    @BindView(R.id.linearLayoutLeaveAppplication)
    LinearLayout linearLayoutLeaveAppplication;
    List<StudentLeavesModel.OneLeaveRecord> studentLeaves = new ArrayList<>();
    public CalenderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new MyAdapter();
        compositeDisposable = new CompositeDisposable();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calender, container, false);
        ButterKnife.bind(this, view);

        Calendar lastYear = Calendar.getInstance();
        lastYear.set(Calendar.YEAR, 2018);
        lastYear.set(Calendar.MONTH, 12);
        lastYear.set(Calendar.DAY_OF_MONTH, 31);
        calendarView.init(Calendar.getInstance().getTime(), lastYear.getTime()).inMode(CalendarPickerView.SelectionMode.MULTIPLE);


        buttonCalendar.performClick();
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setAdapter(adapter);


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) Objects.requireNonNull(getActivity())).textViewTitle.setText("CALENDAR");

    }

    @OnClick(R.id.buttonCalendar)
    public void onButtonCalendarClicked() {
        buttonCalendar.setBackgroundResource(R.drawable.rectangle_rounded_blue_background);
        buttonHistory.setBackground(null);

        buttonCalendar.setTextColor(Color.WHITE);
        buttonHistory.setTextColor(Color.BLACK);

        buttonContinue.setText(R.string.continue_text);
        recycleView.setVisibility(View.GONE);
        textViewLeaveHistory.setVisibility(View.GONE);

        calendarView.setVisibility(View.VISIBLE);

        textViewFaltuText.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.buttonHistory)
    public void onButtonHistoryClicked() {

        buttonHistory.setBackgroundResource(R.drawable.rectangle_rounded_blue_background);
        buttonCalendar.setBackground(null);

        buttonCalendar.setTextColor(Color.BLACK);
        buttonHistory.setTextColor(Color.WHITE);

        buttonContinue.setText(R.string.ok);

        recycleView.setVisibility(View.VISIBLE);
        textViewLeaveHistory.setVisibility(View.VISIBLE);

        calendarView.setVisibility(View.GONE);
        textViewFaltuText.setVisibility(View.GONE);
        linearLayoutLeaveAppplication.setVisibility(View.GONE);
        callApiForHistory("1");


    }
    public void callApiForHistory(String currentKidId)
    {
            progressBar.setVisibility(View.VISIBLE);
            compositeDisposable.add(MyApp.endPoints
                    .getStudentLeaves(currentKidId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new EndpointObserver<StudentLeavesModel>() {


                        @Override
                        public void onComplete() {
                        progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onData(StudentLeavesModel o) throws Exception {
                            if(o.status == 200)
                            {
                                studentLeaves = o.data;
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onHandledError(Throwable e) {

                        }
                    }));

    }
    @OnClick(R.id.buttonContinue)
    public void onButtonContinueClicked() {

        if (calendarView.getVisibility() == View.VISIBLE) {

            linearLayoutLeaveAppplication.setVisibility(View.VISIBLE);
            buttonContinue.setText(R.string.done);

            calendarView.setVisibility(View.GONE);
            textViewFaltuText.setVisibility(View.GONE);

        } else {

        }


    }

    private class MyAdapter extends RecyclerView.Adapter<VH> {
        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(getContext()).inflate(R.layout.history_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {

        }

        @Override
        public int getItemCount() {
            return studentLeaves.size();
        }
    }

    private class VH extends RecyclerView.ViewHolder {
        public VH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
