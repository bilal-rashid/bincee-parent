package com.bincee.parent.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bincee.parent.R;
import com.bincee.parent.api.model.AlertsModel;
import com.bincee.parent.dialog.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlertsVPFragment extends Fragment {


    private List<AlertsModel.EnclosingData> alertList = new ArrayList<>();
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    private MyAdapter adapter;

    public AlertsVPFragment() {
    }

    public AlertsVPFragment setAlerts(List<AlertsModel.EnclosingData> alertList) {
        this.alertList = alertList;
        adapter.notifyDataSetChanged();

        return this;
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

    private class MyAdapter extends RecyclerView.Adapter<VH> {
        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View viewGeneral = getLayoutInflater().inflate(R.layout.alert_row, parent, false);
//            TextView textViewMessage = view.findViewById(R.id.textViewMessage);
//            TextView textViewAlert = view.findViewById(R.id.textViewAlert);
//            if(currentSelectedPage == 0) {
//
//                textViewMessage.setText("abcddfsdfsf");
//                textViewAlert.setText("Announcement");
//            }
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
            return alertList.size();
        }
    }

    private class VH extends RecyclerView.ViewHolder {
        TextView message;
        TextView alert;

        public VH(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.textViewMessage);
            alert = itemView.findViewById(R.id.textViewAlert);

        }

        public void bind() {
            AlertsModel.EnclosingData enclosingData = alertList.get(getAdapterPosition());


            alert.setText(enclosingData.title);
            message.setText(enclosingData.description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog(getContext()).setModel(enclosingData).show();
                }
            });

        }
    }
}
