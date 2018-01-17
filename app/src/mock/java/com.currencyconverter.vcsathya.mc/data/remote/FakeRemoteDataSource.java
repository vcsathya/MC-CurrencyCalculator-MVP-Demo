package com.currencyconverter.vcsathya.mc.data.remote;

import android.util.Log;

import com.currencyconverter.vcsathya.mc.data.DataSource;
import com.currencyconverter.vcsathya.mc.data.models.Currency;
import com.currencyconverter.vcsathya.mc.data.models.Exchange;

import java.util.List;

public class FakeRemoteDataSource implements DataSource {

    public static FakeRemoteDataSource INSTANCE;

    private FakeRemoteDataSource() {}

    public static FakeRemoteDataSource getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new FakeRemoteDataSource();
        }

        return INSTANCE;
    }

    @Override
    public void getCurrencies(List<Currency> currencies, GetCurrenciesCallback callback) {

    }

    @Override
    public void refreshCurrencies() {

    }

    @Override
    public void getConversionRate(Exchange exchange, GetConversionRateCallback conversionRateCallback) {

    }

}
