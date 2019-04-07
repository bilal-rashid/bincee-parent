package com.bincee.parent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusTextView extends LinearLayout {


    @BindView(R.id.textViewTitle)
    public TextView textViewTitle;
    @BindView(R.id.textViewText)
    public TextView textViewText;
    public LinearLayout view;

    public StatusTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        view = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.status_customtextview, null, false);
        ButterKnife.bind(this, view);
        addView(view);
    }

    public StatusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public StatusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public StatusTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void selected() {
        textViewTitle.setTextColor(getResources().getColor(R.color.status_selected_title));
        textViewText.setTextColor(getResources().getColor(R.color.status_selected_subtext));
    }

    public void unSelected() {

        textViewTitle.setTextColor(getResources().getColor(android.R.color.darker_gray));
        textViewText.setTextColor(getResources().getColor(android.R.color.darker_gray));
    }

}
