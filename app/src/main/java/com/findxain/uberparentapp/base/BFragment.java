package com.findxain.uberparentapp.base;

import com.findxain.uberparentapp.HomeActivity;

import java.util.Objects;

import androidx.fragment.app.Fragment;

public class BFragment extends Fragment {
    protected void setActivityTitle(String title) {
        ((HomeActivity) Objects.requireNonNull(getActivity())).textViewTitle.setText(title);
    }
}
