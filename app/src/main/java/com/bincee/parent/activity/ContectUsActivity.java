package com.bincee.parent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.bincee.parent.HomeActivity;
import com.bincee.parent.R;
import com.bincee.parent.base.BA;
import com.bincee.parent.fragment.ContectUsFragment;

import butterknife.BindView;

public class ContectUsActivity extends BA {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static void start(HomeActivity homeActivity) {
        homeActivity.startActivity(new Intent(homeActivity, ContectUsActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contect_us);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        textViewTitle.setText("CONTACT US");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new ContectUsFragment())
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
