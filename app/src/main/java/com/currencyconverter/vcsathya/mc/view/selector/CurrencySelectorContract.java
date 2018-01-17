package com.currencyconverter.vcsathya.mc.view.selector;

import android.content.Context;

import com.currencyconverter.vcsathya.mc.base.BasePresenterInterface;
import com.currencyconverter.vcsathya.mc.base.BaseViewInterface;
import com.currencyconverter.vcsathya.mc.data.models.Currency;

import java.util.List;

interface CurrencySelectorContract {

    interface View extends BaseViewInterface {

        void showCurrencies(List<Currency> currencies);

    }

    interface Presenter extends BasePresenterInterface<View> {

        void getCurrencies(List<Currency> currencies, Context context);

    }
}
