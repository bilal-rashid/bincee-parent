package com.findxain.uberparentapp.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.findxain.uberparentapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class KidArrivedFragment extends Fragment {


    public KidArrivedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kid_arrived, container, false);
    }

}
