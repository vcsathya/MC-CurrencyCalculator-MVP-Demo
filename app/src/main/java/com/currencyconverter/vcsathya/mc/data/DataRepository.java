package com.currencyconverter.vcsathya.mc.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.currencyconverter.vcsathya.mc.data.models.Currency;
import com.currencyconverter.vcsathya.mc.data.models.Exchange;

import java.util.ArrayList;
import java.util.List;

public class DataRepository implements DataSource {

    private static DataRepository DATA_REPOSITORY_INSTANCE = null;

    private final DataSource remoteDataSource;

    private List<Currency> cachedCurrencies;
    private boolean cacheIsDirty = false;

    // Avoid direct instantiation
    private DataRepository(@NonNull DataSource rDataSource) {
        remoteDataSource = rDataSource;
    }

    // Return single class instance; create the instance if it is null
    public static DataRepository getINSTANCE(DataSource remoteDataSource) {

        if (DATA_REPOSITORY_INSTANCE == null) {
            DATA_REPOSITORY_INSTANCE = new DataRepository(remoteDataSource);
        }

        return DATA_REPOSITORY_INSTANCE;
    }

    // For testing purpose
    public static void destroyInstance() {
        DATA_REPOSITORY_INSTANCE = null;
    }


    @Override
    public void getCurrencies(List<Currency> currencies, GetCurrenciesCallback currenciesCallback) {

        if (cachedCurrencies != null && !cacheIsDirty) {
            // get data from cache. Do not make unnecessary network API calls to retrieve list of currencies.
            currenciesCallback.onSuccessfulCurrenciesRetrieval(cachedCurrencies);
            return;
        }

        if (!cacheIsDirty) {
            // Get data from remote API
            getCurrenciesFromApi(currencies, currenciesCallback);
        }
    }

    @Override
    public void refreshCurrencies() {
        // Set this in case of pull to refresh
        cacheIsDirty = true;
    }

    private void getCurrenciesFromApi(final List<Currency> currencies, final GetCurrenciesCallback callback) {
         remoteDataSource.getCurrencies(currencies, new GetCurrenciesCallback() {

            @Override
            public void onSuccessfulCurrenciesRetrieval(List<Currency> currencies) {
                refreshCache(currencies);
                callback.onSuccessfulCurrenciesRetrieval(cachedCurrencies);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Crashlytics.logException(throwable);
                callback.onFailure(throwable);
            }

            @Override
            public void onNetworkFailure() {
                Crashlytics.logException(new Exception("NetworkException"));
                callback.onNetworkFailure();
            }

        });
    }

    private void refreshCache(List<Currency> currencies) {
        if (cachedCurrencies == null) {
            cachedCurrencies = new ArrayList<>();
        }

        cachedCurrencies.clear();

        for (Currency currency : currencies) {
            cachedCurrencies.add(currency);
        }
        cacheIsDirty = false;
    }

    @Override
    public void getConversionRate(Exchange exchange, GetConversionRateCallback conversionRateCallback) {
        getExchangeRateFromApi(exchange, conversionRateCallback);
    }

    private void getExchangeRateFromApi(Exchange exchange, final GetConversionRateCallback callback) {
        remoteDataSource.getConversionRate(exchange, new GetConversionRateCallback() {
            @Override
            public void onSuccessfulExchangeRateRetrieval(Exchange exchange) {
                callback.onSuccessfulExchangeRateRetrieval(exchange);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Crashlytics.logException(throwable);
                callback.onFailure(throwable);
            }

            @Override
            public void onNetworkFailure() {
                Crashlytics.logException(new Exception("NetworkException"));
                callback.onNetworkFailure();
            }
        });
    }
}