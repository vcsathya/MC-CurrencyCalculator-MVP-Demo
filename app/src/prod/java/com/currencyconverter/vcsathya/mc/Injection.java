package com.currencyconverter.vcsathya.mc;

import com.currencyconverter.vcsathya.mc.data.DataRepository;
import com.currencyconverter.vcsathya.mc.data.api.McCurrencyApi;
import com.currencyconverter.vcsathya.mc.data.remote.RemoteDataSource;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.currencyconverter.vcsathya.mc.data.remote.RemoteDataSource.BASE_URL;

public class Injection {

    public static DataRepository provideDataRepository() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        McCurrencyApi mcCurrencyApi = retrofit.create(McCurrencyApi.class);

        return DataRepository.getINSTANCE(RemoteDataSource.getRemoteDatasourceInstance(mcCurrencyApi));
    }
}
