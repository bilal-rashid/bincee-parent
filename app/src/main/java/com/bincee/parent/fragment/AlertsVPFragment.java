package com.bincee.parent.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bincee.parent.MyApp;
import com.bincee.parent.R;
import com.bincee.parent.dialog.AlertDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlertsVPFragment extends Fragment {


    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    private MyAdapter adapter;
    int currentSelectedPage;
    View viewGeneral;
    public AlertsVPFragment(int currentPage) {
        currentSelectedPage = currentPage;
        // Required empty public constructor
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

             viewGeneral = getLayoutInflater().inflate(R.layout.alert_row, parent, false);
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
            if(currentSelectedPage == 0) {
                holder.textView3.setText(MyApp.instance.alertList.get(position).description);
                holder.textView3.setText("Alerts");
            }
            else
            {
                holder.textView3.setText(MyApp.instance.announcementList.get(position).description);
                holder.textView3.setText("Announcements");
            }
        }

        @Override
        public int getItemCount() {
            if (currentSelectedPage == 0) {
                return MyApp.instance.alertList.size();
            }
            else{
                return MyApp.instance.announcementList.size();
            }
        }
    }

    private class VH extends RecyclerView.ViewHolder {
        TextView textView3 = viewGeneral.findViewById(R.id.textViewMessage);
        TextView textView2 = viewGeneral.findViewById(R.id.textViewAlert);
        public VH(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog(getContext()).show();
                }
            });
        }
    }
}
