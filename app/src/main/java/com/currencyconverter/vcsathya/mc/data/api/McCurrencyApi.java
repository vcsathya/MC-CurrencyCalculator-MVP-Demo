package com.currencyconverter.vcsathya.mc.data.api;

import com.currencyconverter.vcsathya.mc.data.models.Currency;
import com.currencyconverter.vcsathya.mc.data.models.Exchange;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface McCurrencyApi {


    @GET("currency")
    Call<List<Currency>> getAllCurrencies();

    @GET("exchange")
    Call<Exchange> getExchangeRate(@Query("baseCode") String baseCode, @Query("targetCode") String targetCode);

}
