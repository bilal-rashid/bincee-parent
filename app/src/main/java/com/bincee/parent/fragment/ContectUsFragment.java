package com.bincee.parent.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bincee.parent.R;
import com.bincee.parent.base.BA;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContectUsFragment extends Fragment {


    @BindView(R.id.textViewContextUs)
    TextView textViewContextUs;

    public ContectUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contect_us, container, false);
        ButterKnife.bind(this, view);
        SpannableStringBuilder append = new SpannableStringBuilder().append(getResources().getString(R.string.u_contactus_bincee_com_u));
//        append.setSpan(new UnderlineSpan(),0,append.length(),);


        textViewContextUs.setText(Html.fromHtml("<u>" + getResources().getString(R.string.u_contactus_bincee_com_u) + "</u>"));
        textViewContextUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, getResources().getString(R.string.u_contactus_bincee_com_u));
                startActivity(Intent.createChooser(intent, "Send Email"));


            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BA) Objects.requireNonNull(getActivity())).textViewTitle.setText("Contact Us");

    }

}
