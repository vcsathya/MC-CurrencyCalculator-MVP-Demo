package com.currencyconverter.vcsathya.mc.view.selector;

import android.content.Context;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.currencyconverter.vcsathya.mc.R;
import com.currencyconverter.vcsathya.mc.base.BasePresenter;
import com.currencyconverter.vcsathya.mc.data.DataRepository;
import com.currencyconverter.vcsathya.mc.data.DataSource;
import com.currencyconverter.vcsathya.mc.data.models.Currency;
import com.currencyconverter.vcsathya.mc.utility.NetworkUtil;

import java.util.List;

import io.fabric.sdk.android.services.common.Crash;

public class CurrencySelectorPresenter extends BasePresenter<CurrencySelectorContract.View>
        implements CurrencySelectorContract.Presenter {

    private DataRepository dataRepository;

    public CurrencySelectorPresenter(CurrencySelectorContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void getCurrencies(List<Currency> currencies, final Context context) {

        if (view == null) {
            Crashlytics.logException(new Exception("NullViewException"));
            return;
        }

        if (!NetworkUtil.isNetworkAvailable(context)) {
            // TODO: Fix mvp to to work from BaseView
//            view.showToastMessage(context, context.getString(R.string.network_error));
            Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_LONG).show();
        }

        view.setProgressBar(true);

        dataRepository.getCurrencies(currencies, new DataSource.GetCurrenciesCallback() {

            @Override
            public void onSuccessfulCurrenciesRetrieval(List<Currency> currencies) {
                if (view != null) {
                    view.showCurrencies(currencies);
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
