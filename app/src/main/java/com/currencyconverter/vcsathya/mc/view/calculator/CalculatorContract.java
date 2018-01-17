package com.currencyconverter.vcsathya.mc.view.calculator;

import android.content.Context;

import com.currencyconverter.vcsathya.mc.base.BasePresenterInterface;
import com.currencyconverter.vcsathya.mc.base.BaseViewInterface;
import com.currencyconverter.vcsathya.mc.data.models.Exchange;

public interface CalculatorContract {

    interface View extends BaseViewInterface {

        void showConversionRate(Exchange exchange);

    }

    interface Presenter extends BasePresenterInterface<View> {

        void getConversionRate(Context context, Exchange exchange);

    }
}
