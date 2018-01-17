package com.currencyconverter.vcsathya.mc;

import com.currencyconverter.vcsathya.mc.data.DataRepository;
import com.currencyconverter.vcsathya.mc.data.remote.FakeRemoteDataSource;

public class Injection {

    public static DataRepository provideDataRepository() {
        return DataRepository.getINSTANCE(FakeRemoteDataSource.getINSTANCE());
    }
}
