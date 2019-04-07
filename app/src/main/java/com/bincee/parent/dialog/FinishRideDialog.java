package com.bincee.parent.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bincee.parent.R;
import com.bincee.parent.base.BDialog;

import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FinishRideDialog extends BDialog {
    @BindView(R.id.imageView12)
    ImageView imageView12;
    @BindView(R.id.textView10)
    TextView textView10;
    @BindView(R.id.textView9)
    TextView textView9;
    @BindView(R.id.buttonStartNewShift)
    Button buttonStartNewShift;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.imageViewCross)
    ImageView imageViewCross;

    public FinishRideDialog(Context context) {
        super(context);

        View view = getLayoutInflater().inflate(R.layout.finish_ride_dialog, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);

    }

    @OnClick(R.id.buttonStartNewShift)
    public void onButtonStartNewShiftClicked() {
        dismiss();

    }

    @OnClick(R.id.imageViewCross)
    public void onImageViewCrossClicked() {
        dismiss();
    }
}
