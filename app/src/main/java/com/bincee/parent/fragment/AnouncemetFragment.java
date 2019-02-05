package com.bincee.parent.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bincee.parent.R;
import com.bincee.parent.api.model.AnnouncementModel;
import com.bincee.parent.base.BFragment;
import com.bincee.parent.dialog.AlertDialog;

import java.util.ArrayList;
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
        adapter.notifyDataSetChanged();
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

        public VH(@NonNull View itemView) {
            super(itemView);


            textView3 = itemView.findViewById(R.id.textViewMessage);
            textView2 = itemView.findViewById(R.id.textViewAlert);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog(getContext()).show();
                }
            });
        }

        public void bind() {

            AnnouncementModel.SingleAnnouncement singleAnnouncement = data.get(getAdapterPosition());
            textView2.setText(singleAnnouncement.title);
            textView3.setText(singleAnnouncement.description);

        }
    }

}
