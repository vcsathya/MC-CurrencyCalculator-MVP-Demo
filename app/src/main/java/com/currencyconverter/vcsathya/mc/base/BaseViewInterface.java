package com.currencyconverter.vcsathya.mc.base;

import android.content.Context;
import android.view.View;

public interface BaseViewInterface {

    // BaseViewInterface defines all methods needed to display anything in the views (activities)

    void showToastMessage(Context context, String message);

    void setProgressBar(boolean show);

}
