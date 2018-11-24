package com.findxain.uberparentapp.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import io.reactivex.disposables.CompositeDisposable;

public class BFragment extends Fragment {
    protected CompositeDisposable compositeDisposable;

    protected void setActivityTitle(String title) {
        FragmentActivity activity = getActivity();

        ((BA) activity).textViewTitle.setText(title);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();

    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }
}
