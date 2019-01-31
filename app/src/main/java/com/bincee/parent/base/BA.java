package com.bincee.parent.base;

import android.os.Bundle;
import android.widget.TextView;

import com.bincee.parent.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class BA extends AppCompatActivity {
    protected CompositeDisposable compositeDisposable;


    @Nullable
    @BindView(R.id.textViewTitle)
    public TextView textViewTitle;
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }
}
