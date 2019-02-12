package com.bincee.parent.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.bincee.parent.HomeActivity;
import com.bincee.parent.MyApp;
import com.bincee.parent.R;
import com.bincee.parent.api.model.CreateLeaveResponce;
import com.bincee.parent.api.model.ParentCompleteData;
import com.bincee.parent.api.model.StudentLeavesModel;
import com.bincee.parent.customview.MyProgress;
import com.bincee.parent.helper.DateHelper;
import com.bincee.parent.observer.EndpointObserver;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

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


    @BindView(R.id.imageViewBg)
    ImageView imageViewBg;
    @BindView(R.id.calendar_view)
    CalendarView calendarView;
    //    CalendarPickerView calendarView;


    @BindView(R.id.buttonCalendar)
    Button buttonCalendar;
    @BindView(R.id.buttonHistory)
    Button buttonHistory;

    @BindView(R.id.buttonContinue)
    Button buttonContinue;
    @BindView(R.id.textViewLeaveHistory)
    TextView textViewLeaveHistory;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    //    @BindView(R.id.radioButtonMorning)
//    RadioButton radioButtonMorning;
//    @BindView(R.id.radioButtonEvening)
//    RadioButton radioButtonEvening;
//    @BindView(R.id.radioButt)
//    RadioGroup radioButt;
    @BindView(R.id.editTextComment)
    TextInputEditText editTextComment;
    @BindView(R.id.imageViewCross)
    ImageView imageViewCross;

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

//        calendarView.init(lastYear.getTime(), Calendar.getInstance().getTime()).inMode(CalendarPickerView.SelectionMode.MULTIPLE);


//        calendarView.

        Calendar minData = Calendar.getInstance();

        calendarView.setMinimumDate(minData);

        buttonCalendar.performClick();
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonCalendar.performClick();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) Objects.requireNonNull(getActivity())).textViewTitle.setText(R.string.leaves);

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
        linearLayoutLeaveAppplication.setVisibility(View.GONE);
        ParentCompleteData.KidModel currentKid = StudentSSFragment.getInstance().currentKid;
        if (currentKid == null) {
            currentKid = MyApp.instance.user.getValue().parentCompleteInfo.kids.get(0);
        }


        callApiForHistory(currentKid.id + "");


    }

    public void callApiForHistory(String currentKidId) {
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
                        if (o.status == 200) {
                            studentLeaves = o.data;
                            adapter.notifyDataSetChanged();

                        } else {
                            throw new Exception(o.status + "");
                        }
                    }

                    @Override
                    public void onHandledError(Throwable e) {
                        e.printStackTrace();
                        MyApp.showToast(e.getMessage());

                    }
                }));

    }

    @OnClick(R.id.buttonContinue)
    public void onButtonContinueClicked() {

        if (calendarView.getVisibility() == View.VISIBLE) {


            if (calendarView.getSelectedDates().size() > 0) {

                linearLayoutLeaveAppplication.setVisibility(View.VISIBLE);
                buttonContinue.setText(R.string.done);

                calendarView.setVisibility(View.GONE);
            }


        } else {

            if (recycleView.getVisibility() == View.VISIBLE) {
                onButtonCalendarClicked();
                return;
            }

//            int checkedRadioButtonId = radioButt.getCheckedRadioButtonId();

            String comment = editTextComment.getText().toString();

            List<Calendar> selectedDates = calendarView.getSelectedDates();

            if (selectedDates.size() == 0) {

                MyApp.showToast("Select date please");
                return;
            }


            progressBar.setVisibility(View.VISIBLE);
            Calendar fromDate = selectedDates.get(0);

            Calendar toDate = selectedDates.get(selectedDates.size() - 1);

            ParentCompleteData.KidModel currentKid = StudentSSFragment.getInstance().currentKid;
            EndpointObserver<CreateLeaveResponce> endpointObserver = MyApp.endPoints.createLeave(fromDate.getTimeInMillis() / 1000L + ""
                    , toDate.getTimeInMillis() / 1000L + ""
                    , currentKid.id + ""
                    , MyApp.instance.user.getValue().parentCompleteInfo.schoolId + ""
                    , comment)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new EndpointObserver<CreateLeaveResponce>() {
                        @Override
                        public void onComplete() {

                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onData(CreateLeaveResponce o) throws Exception {

                            if (o.status == 200) {
                                MyApp.showToast("Leave Created");
                            }

                        }

                        @Override
                        public void onHandledError(Throwable e) {

                            new AlertDialog.Builder(getContext())
                                    .setMessage(e.getMessage())
                                    .show();


                        }
                    });


            compositeDisposable.add(endpointObserver);

        }


    }

    @OnClick(R.id.imageViewCross)
    public void onViewClicked() {
        HomeActivity activity = (HomeActivity) getActivity();
        activity.binding.bottomNavigationView.setSelectedItemId(R.id.bottmnavigation_summarizedsize);
    }

    private class MyAdapter extends RecyclerView.Adapter<VH> {

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(getContext()).inflate(R.layout.history_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            holder.bind(studentLeaves.get(holder.getAdapterPosition()));

        }

        @Override
        public int getItemCount() {
            return studentLeaves.size();
        }
    }

    public class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView8)
        ImageView imageView8;

        @BindView(R.id.textViewTitle)
        TextView textViewTitle;
        @BindView(R.id.textViewDetailMessage)
        TextView textViewDetailMessage;

        public VH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(StudentLeavesModel.OneLeaveRecord oneLeaveRecord) {
            String fromData = DateHelper.help(oneLeaveRecord.fromDate);
            String toDate = DateHelper.help(oneLeaveRecord.toDate);
            String year = DateHelper.helpYear(oneLeaveRecord.toDate);

            textViewTitle.setText(fromData + " - " + toDate + ", " + year);
            textViewDetailMessage.setText(oneLeaveRecord.comment);
        }
    }
}
