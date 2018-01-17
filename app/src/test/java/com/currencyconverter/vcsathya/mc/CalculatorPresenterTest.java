package com.currencyconverter.vcsathya.mc;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.currencyconverter.vcsathya.mc.data.DataRepository;
import com.currencyconverter.vcsathya.mc.data.DataSource;
import com.currencyconverter.vcsathya.mc.data.models.Exchange;
import com.currencyconverter.vcsathya.mc.view.calculator.CalculatorContract;
import com.currencyconverter.vcsathya.mc.view.calculator.CalculatorPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.fabric.sdk.android.Fabric;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class CalculatorPresenterTest {

    @Mock
    private DataRepository dataRepository;

    @Mock
    private CalculatorContract.View calculatorView;

    @Mock
    private Context context;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<DataSource.GetConversionRateCallback> getConversionRateCallbackArgumentCaptor;

    private CalculatorPresenter calculatorPresenter;
    private Exchange exchange;

    @Before
    public void setup() {

        Fabric.with(context, new Crashlytics());
        calculatorPresenter = new CalculatorPresenter(calculatorView, dataRepository);
        exchange = new Exchange();

    }

    @Test
    public void getConversionRate() {

        calculatorPresenter.getConversionRate(context, exchange);

        verify(calculatorView).setProgressBar(true);
        verify(dataRepository).getConversionRate(eq(exchange), eq(getConversionRateCallbackArgumentCaptor.capture()));

        DataSource.GetConversionRateCallback getConversionRateCallback = getConversionRateCallbackArgumentCaptor.getValue();

        calculatorPresenter.onViewActive(calculatorView);
        getConversionRateCallback.onSuccessfulExchangeRateRetrieval(exchange);
        verify(calculatorView).showConversionRate(exchange);
        verify(calculatorView).setProgressBar(false);
    }

}
