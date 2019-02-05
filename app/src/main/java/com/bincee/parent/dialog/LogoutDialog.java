package com.bincee.parent.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bincee.parent.R;
import com.bincee.parent.base.BDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogoutDialog extends BDialog {
    @BindView(R.id.buttonOK)
    Button buttonOK;
    @BindView(R.id.buttonCancel)
    TextView buttonCancel;
    @BindView(R.id.imageViewCross)
    ImageView imageViewCross;
    private Listner listner;

    public LogoutDialog(@NonNull Context context) {
        super(context);
        init();
    }


    private void init() {


        int layout = R.layout.logout_dialog;
        View view = getLayoutInflater().inflate(layout, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);

    }

    public LogoutDialog setListner(Listner listner) {
        this.listner = listner;
        return this;
    }

    @OnClick(R.id.buttonOK)
    public void onButtonOKClicked() {
        listner._logout();
        dismiss();
    }

    @OnClick(R.id.buttonCancel)
    public void onButtonCancelClicked() {
        listner._cancel();
        dismiss();
    }

    @OnClick(R.id.imageViewCross)
    public void onImageViewCrossClicked() {
        dismiss();
    }

    @Override
    public void show() {
        super.show();

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


    }

    public interface Listner {
        void _logout();

        void _cancel();
    }
}
