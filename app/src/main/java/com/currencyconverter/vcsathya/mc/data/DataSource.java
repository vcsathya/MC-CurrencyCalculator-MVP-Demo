package com.currencyconverter.vcsathya.mc.data;

import com.currencyconverter.vcsathya.mc.data.models.Currency;
import com.currencyconverter.vcsathya.mc.data.models.Exchange;

import java.util.List;

public interface DataSource {

    interface GetCurrenciesCallback {

        void onSuccessfulCurrenciesRetrieval(List<Currency> currencies);

        void onFailure(Throwable throwable);

        void onNetworkFailure();

    }

    interface GetConversionRateCallback {

        void onSuccessfulExchangeRateRetrieval(Exchange exchange);

        void onFailure(Throwable throwable);

        void onNetworkFailure();

    }

    void getCurrencies(List<Currency> currencies, GetCurrenciesCallback callback);

    void refreshCurrencies();

    void getConversionRate(Exchange exchange, GetConversionRateCallback conversionRateCallback);

}
