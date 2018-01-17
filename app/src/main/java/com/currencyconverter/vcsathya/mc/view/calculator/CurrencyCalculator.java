package com.currencyconverter.vcsathya.mc.view.calculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import com.currencyconverter.vcsathya.mc.Injection;
import com.currencyconverter.vcsathya.mc.R;
import com.currencyconverter.vcsathya.mc.data.DataRepository;
import com.currencyconverter.vcsathya.mc.data.models.Currency;
import com.currencyconverter.vcsathya.mc.data.models.Exchange;
import com.currencyconverter.vcsathya.mc.view.selector.CurrencySelector;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.currencyconverter.vcsathya.mc.data.remote.RemoteDataSource.BASE_SERVER;
import static com.currencyconverter.vcsathya.mc.view.selector.CurrencySelector.EXTRA;

public class CurrencyCalculator extends AppCompatActivity implements CalculatorContract.View, Serializable {

    @BindView(R.id.base_flag)
    ImageView baseFlag;

    @BindView(R.id.target_flag)
    ImageView targetFlag;

    @BindView(R.id.base_currency)
    TextView baseCurrency;

    @BindView(R.id.base_conversion_rate)
    TextView baseConversionRate;

    @BindView(R.id.enter_base_value)
    EditText enterBaseValue;

    @BindView(R.id.target_card)
    CardView targetCard;

    @BindView(R.id.target_currency)
    TextView targetCurrency;

    @BindView(R.id.target_conversion_rate)
    TextView targetConversionRate;

    @BindView(R.id.enter_target_value)
    EditText enterTargetValue;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private CalculatorContract.Presenter presenter;
    private Exchange exchange;
    private Currency currency;

    private static final int SELECT_BASE_CURRENCY = 1;
    private static final int SELECT_TARGET_CURRENCY = 2;
    private String DEFAULT_BASE_CURRENCY;
    private String DEFAULT_TARGET_CURRENCY;


    public CurrencyCalculator() {}

    public CurrencyCalculator newInstance() {
        return new CurrencyCalculator();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_currency_calculator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO: Align title center

        exchange = new Exchange();
        DEFAULT_BASE_CURRENCY = getString(R.string.default_base_currency);
        DEFAULT_TARGET_CURRENCY = getString(R.string.default_target_currency);

        DataRepository dataRepository = Injection.provideDataRepository();
        presenter = new CalculatorPresenter(this, dataRepository);

        ButterKnife.bind(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setCurrencyType();
            }
        });

        // TODO: Fix Glide issue with loading drawableStart image on textView and remove additional imageview
        Glide.with(this).load(R.mipmap.us)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()).override(100, 100)).into(baseFlag);
        Glide.with(this).load(R.mipmap.eu)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()).override(100, 100)).into(targetFlag);

        // Get ExchangeRate for default currencies on load
        setCurrencyType();
        setCurrencyTypeListeners();

    }

    private void setCurrencyTypeListeners() {

        baseFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecyclerView(true);
            }
        });

        targetFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecyclerView(false);
            }
        });

        // listener on baseCurrencyType
        baseCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecyclerView(true);
            }
        });

        // listener on targetCurrencyType
        targetCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecyclerView(false);
            }
        });
    }

    private void startRecyclerView(boolean base) {
        if (base) {
            Intent intent = new Intent(CurrencyCalculator.this, CurrencySelector.class);
            startActivityForResult(intent, SELECT_BASE_CURRENCY);
        } else {
            Intent intent = new Intent(CurrencyCalculator.this, CurrencySelector.class);
            startActivityForResult(intent, SELECT_TARGET_CURRENCY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onViewActive(this);
    }

    @Override
    public void showToastMessage(Context context, String message) {
        // None
    }

    @Override
    public void setProgressBar(boolean show) {
        swipeRefreshLayout.setRefreshing(show);
    }

    @Override
    public void showConversionRate(Exchange exchange) {

        if (exchange == null) {
            Crashlytics.logException(new Exception("NullExchangeException"));
            return;
        }

        showBaseExchangeRates(exchange);

        showCalculatedExchangeRates(exchange);

    }

    private void setCurrencyType() {

        String baseCurrencyType = baseCurrency.getText().toString();
        String targetCurrencyType = targetCurrency.getText().toString();

        exchange.setBaseCode(baseCurrencyType.isEmpty() ? DEFAULT_BASE_CURRENCY : baseCurrencyType);
        exchange.setTargetCode(targetCurrencyType.isEmpty() ? DEFAULT_TARGET_CURRENCY : targetCurrencyType);


        /* This could be done in reference to USD by calling the server only if the user selects currencyType other than USD
        because getCurrencies() retrieves the exchange rate from server in reference to USD
        However, doing so would not assure up-to-date exchange rate
        because the List<Currency> will be updated only once and is stored in cache. If it is unavailable in the cache
        the value is read again from the server.
        Hence, we get the exchange rate from server every time user selects/changes the currencyType
        even though they choose USD.
        Server returning the currencies list in reference to USD is irrelevant in this case. */

        fetchExchangeRate();
    }

    private void showBaseExchangeRates(Exchange exchange) {

        String default_amount = getString(R.string.default_amount);
        String equalSign = getString(R.string.equals);
        String space = " ";

        // Set exchange rate labels for the base value (Eg: $1)
        baseConversionRate.setText(default_amount + space + exchange.getBaseCode() + space
                + equalSign + space + exchange.getRate()
                + space + exchange.getTargetCode());
        targetConversionRate.setText(default_amount + space + exchange.getTargetCode() + space
                + equalSign + space + 1 / exchange.getRate()
                + space + exchange.getBaseCode());
    }

    private void showCalculatedExchangeRates(final Exchange exchange) {

        // Update the labels if the user had already entered an amount in either EditText fields
        if (!enterBaseValue.getText().toString().isEmpty()) {
            setExchangeRate(enterBaseValue, exchange);
        }

        if (!enterTargetValue.getText().toString().isEmpty()) {
            setExchangeRate(enterTargetValue, exchange);
        }

        // Set baseCurrency listener
        enterBaseValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                setExchangeRate(enterBaseValue, exchange);
            }
        });

        // Set targetCurrency listener
        enterTargetValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                setExchangeRate(enterTargetValue, exchange);
            }
        });

    }

    private void setExchangeRate(EditText editText, Exchange exchange) {
        String enteredValue = editText.getText().toString();
        View viewInFocus = getCurrentFocus();

        // One way to avoid infinite loops - not only the manual input, but setting editText value
        // programmatically triggers the listener as well
        // Check for the current focus in the condition to update the value

        if (viewInFocus == editText && !enteredValue.isEmpty()) {
            float result = Float.parseFloat(enteredValue);
            // update the baseCurrency or targetCurrency EditText fields depending on the current focus
            if (viewInFocus == enterBaseValue) {
                enterTargetValue.setText((String.valueOf(calculateBaseRate(result, exchange))));
            } else {
                enterBaseValue.setText((String.valueOf(calculateTargetRate(result, exchange))));
            }
        }
    }

    private void fetchExchangeRate() {
        // Call this only when the user selects the currency type
        presenter.getConversionRate(this, exchange);
    }

    private float calculateBaseRate(float enteredValue, Exchange exchange) {
        return  enteredValue * exchange.getRate();
    }

    private float calculateTargetRate(float enteredValue, Exchange exchange) {
        return  enteredValue / exchange.getRate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            currency = (Currency) data.getSerializableExtra(EXTRA);
            String flagPath = BASE_SERVER + currency.getFlagPath();

            if (requestCode == SELECT_BASE_CURRENCY) {
                baseCurrency.setText(currency.getCode());

                Glide.with(CurrencyCalculator.this)
                        .load(flagPath)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()).override(100, 100)).into(baseFlag);

            }

            if (requestCode == SELECT_TARGET_CURRENCY) {
                targetCurrency.setText(currency.getCode());

                Glide.with(CurrencyCalculator.this)
                        .load(flagPath)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()).override(100, 100)).into(targetFlag);

            }

            // Set the currency Type and calculate the exchange rate from server to calculate the amount
            setCurrencyType();
        } else {
            Crashlytics.log("RecyclerView Currency selection did not complete OR User did not choose a currency Type");
        }
    }
}
