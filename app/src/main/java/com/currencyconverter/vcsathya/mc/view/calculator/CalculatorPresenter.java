package com.currencyconverter.vcsathya.mc.view.calculator;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.currencyconverter.vcsathya.mc.R;
import com.currencyconverter.vcsathya.mc.base.BasePresenter;
import com.currencyconverter.vcsathya.mc.data.DataRepository;
import com.currencyconverter.vcsathya.mc.data.DataSource;
import com.currencyconverter.vcsathya.mc.data.models.Exchange;
import com.currencyconverter.vcsathya.mc.utility.NetworkUtil;

public class CalculatorPresenter extends BasePresenter<CalculatorContract.View> implements CalculatorContract.Presenter {

    private DataRepository dataRepository;

    public CalculatorPresenter(CalculatorContract.View view, DataRepository dataRepository) {

        this.view = view;
        this.dataRepository = dataRepository;

    }

    @Override
    public void getConversionRate(final Context context, Exchange exchange) {

        if (view == null) {
            Crashlytics.logException(new Exception("NullViewException"));
            return;
        }

        if (!NetworkUtil.isNetworkAvailable(context)) {
            // TODO: Fix flow to work from BaseView
//            view.showToastMessage(context, context.getString(R.string.network_error));
            Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        view.setProgressBar(true);

        dataRepository.getConversionRate(exchange, new DataSource.GetConversionRateCallback() {
            @Override
            public void onSuccessfulExchangeRateRetrieval(Exchange exchange) {
                if (view != null) {
                    view.showConversionRate(exchange);
                    view.setProgressBar(false);
                }

            }

            @Override
            public void onFailure(Throwable throwable) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage(context, context.getString(R.string.generic_error));
                }

            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage(context, context.getString(R.string.network_error));
                }
            }
        });

    }

}
