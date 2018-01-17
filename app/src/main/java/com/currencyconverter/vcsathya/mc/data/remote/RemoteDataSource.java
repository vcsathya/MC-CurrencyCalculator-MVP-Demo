package com.currencyconverter.vcsathya.mc.data.remote;

import com.crashlytics.android.Crashlytics;
import com.currencyconverter.vcsathya.mc.data.DataSource;
import com.currencyconverter.vcsathya.mc.data.api.McCurrencyApi;
import com.currencyconverter.vcsathya.mc.data.models.Currency;
import com.currencyconverter.vcsathya.mc.data.models.Exchange;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteDataSource implements DataSource {

    private static RemoteDataSource REMOTE_DATA_SOURCE_INSTANCE = null;
    // Use Proguard for Code Obfuscation
    public static final String BASE_URL = "http://192.168.1.139:8080/api/";
    public static final String BASE_SERVER = "http://192.168.1.139:8080";

    private McCurrencyApi mcCurrencyApi;

    private RemoteDataSource(McCurrencyApi api) {
        this.mcCurrencyApi = api;
    }

    // Return single class instance; create the instance if it is null
    public static RemoteDataSource getRemoteDatasourceInstance(McCurrencyApi api) {
        if (REMOTE_DATA_SOURCE_INSTANCE == null) {
            REMOTE_DATA_SOURCE_INSTANCE = new RemoteDataSource(api);
        }
        return REMOTE_DATA_SOURCE_INSTANCE;
    }

    @Override
    public void getCurrencies(final List<Currency> currencies, final GetCurrenciesCallback callback) {

        Call<List<Currency>> call = mcCurrencyApi.getAllCurrencies();

        call.enqueue(new Callback<List<Currency>>() {

            @Override
            public void onResponse(Call<List<Currency>> call, Response<List<Currency>> response) {

                if (response.isSuccessful()) {
                    List<Currency> currencyList = response.body();
                    callback.onSuccessfulCurrenciesRetrieval(currencyList);
                } else {
                    Crashlytics.logException(new Exception("BadResponseException"));
                    callback.onFailure(new Throwable("BadResponse"));
                }
            }

            @Override
            public void onFailure(Call<List<Currency>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void refreshCurrencies() {
        // Implementation is taken care of already in the Base DataRepository class.
    }

    @Override
    public void getConversionRate(Exchange exchange, final GetConversionRateCallback callback) {

        Call<Exchange> call = mcCurrencyApi.getExchangeRate(exchange.getBaseCode(), exchange.getTargetCode());

        call.enqueue(new Callback<Exchange>() {
            @Override
            public void onResponse(Call<Exchange> call, Response<Exchange> response) {
                if (response.isSuccessful()) {
                    Exchange exchange = response.body();
                    callback.onSuccessfulExchangeRateRetrieval(exchange);
                } else {
                    Crashlytics.logException(new Exception("BadResponseException"));
                    callback.onFailure(new Throwable("BadResponse"));
                }
            }

            @Override
            public void onFailure(Call<Exchange> call, Throwable t) {
                callback.onFailure(t);
            }
        });

    }
}
