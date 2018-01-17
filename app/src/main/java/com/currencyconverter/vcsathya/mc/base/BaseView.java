package com.currencyconverter.vcsathya.mc.base;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.currencyconverter.vcsathya.mc.R;

import butterknife.BindView;

public abstract class BaseView implements BaseViewInterface {

    @BindView(R.id.progress_bar)
    protected ProgressBar progressBar;

    @Override
    public void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}