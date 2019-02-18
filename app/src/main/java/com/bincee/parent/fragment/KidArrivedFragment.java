package com.bincee.parent.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bincee.parent.HomeActivity;
import com.bincee.parent.R;
import com.bincee.parent.base.BFragment;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class KidArrivedFragment extends BFragment {


    private static KidArrivedFragment instance;

    public static KidArrivedFragment getInstance() {
        if (instance == null) {
            instance = new KidArrivedFragment();
        }
        return instance;
    }

    public KidArrivedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kid_arrived, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goback();

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentActivity factivity = getActivity();
        if (factivity instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) factivity;
            activity.setBackButton();
        }
        setTitle();
    }

    private void goback() {
        StudentSSFragment.getInstance().getChildFragmentManager().popBackStack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            goback();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void setTitle() {
        ((HomeActivity) Objects.requireNonNull(getActivity())).textViewTitle.setText("Kid Arrived");
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        FragmentActivity factivity = getActivity();
        if (factivity instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) factivity;
            activity.setThreeLine();
        }
        StudentSSFragment.getInstance().setTitle();
        super.onDestroyView();

    }
}
