package com.bincee.parent.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bincee.parent.R;
import com.bincee.parent.api.model.AnnouncementModel;
import com.bincee.parent.api.model.FCMNotification;
import com.bincee.parent.base.BFragment;
import com.bincee.parent.dialog.AlertDialog;
import com.bincee.parent.helper.MyPref;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnouncemetFragment extends BFragment {


    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    private MyAdapter adapter;
    private List<AnnouncementModel.SingleAnnouncement> data = new ArrayList<>();

    public AnouncemetFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new MyAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alerts_v, container, false);
        ButterKnife.bind(this, view);

        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setAdapter(adapter);

        return view;
    }

    public AnouncemetFragment setData(List<AnnouncementModel.SingleAnnouncement> data) {
        this.data = data;
        Collections.reverse(data);
        adapter.notifyDataSetChanged();
        FCMNotification notificationFromTray = MyPref.GetNotification(getContext());
        if (notificationFromTray != null) {
            if (!notificationFromTray.school && notificationFromTray.student != null) {
                MyPref.SaveNotification(getContext(),null);
                final int pos = 0;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recycleView.findViewHolderForAdapterPosition(pos).itemView.performClick();
                    }
                },1);
            }
        }
        return this;
    }

    private class MyAdapter extends RecyclerView.Adapter<VH> {
        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View viewGeneral = getLayoutInflater().inflate(R.layout.alert_row, parent, false);
            return new VH(viewGeneral);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
//            if (currentSelectedPage == 0) {
//                holder.message.setText(MyApp.instance.alertList.get(position).description);
//                holder.message.setText("Alerts");
//            } else {
//                holder.message.setText(MyApp.instance.announcementList.get(position).description);
//                holder.message.setText("Announcements");
//            }


            holder.bind();

        }

        @Override
        public int getItemCount() {

            return data.size();
        }
    }

    private class VH extends RecyclerView.ViewHolder {

        TextView textView3;
        TextView textView2;
        ImageView imageView;

        public VH(@NonNull View itemView) {
            super(itemView);


            textView3 = itemView.findViewById(R.id.textViewMessage);
            textView2 = itemView.findViewById(R.id.textViewAlert);
            imageView = itemView.findViewById(R.id.imageView6);


        }

        public void bind() {

            AnnouncementModel.SingleAnnouncement singleAnnouncement = data.get(getAdapterPosition());
            textView2.setText(singleAnnouncement.title);
            textView3.setText(singleAnnouncement.description);

            if(MyPref.GET_NOTIFICATION_SEEN(getContext(),singleAnnouncement.id+""+singleAnnouncement.title+singleAnnouncement.description)){
                imageView.setImageResource(R.drawable.alert_green);
            }else {
                imageView.setImageResource(R.drawable.alert_red);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog(getContext()).setModel(singleAnnouncement).show();
                    MyPref.SAVE_NOTIFICATION_SEEN(getContext(),singleAnnouncement.id+""+singleAnnouncement.title+singleAnnouncement.description);
                    if(MyPref.GET_NOTIFICATION_SEEN(getContext(),singleAnnouncement.id+""+singleAnnouncement.title+singleAnnouncement.description)){
                        imageView.setImageResource(R.drawable.alert_green);
                    }else {
                        imageView.setImageResource(R.drawable.alert_red);
                    }
                }
            });
        }
    }

}
