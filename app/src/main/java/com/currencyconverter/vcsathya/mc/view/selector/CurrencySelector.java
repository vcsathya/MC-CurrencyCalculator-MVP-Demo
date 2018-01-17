package com.currencyconverter.vcsathya.mc.view.selector;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.currencyconverter.vcsathya.mc.Injection;
import com.currencyconverter.vcsathya.mc.R;
import com.currencyconverter.vcsathya.mc.data.DataRepository;
import com.currencyconverter.vcsathya.mc.data.models.Currency;
import com.currencyconverter.vcsathya.mc.view.calculator.CurrencyCalculator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrencySelector extends AppCompatActivity implements CurrencySelectorContract.View, CurrencySelectorAdapter.CurrencySelectorAdapterListener {

    @BindView(R.id.currencies_recycler_view)
    RecyclerView currenciesRecyclerView;

    private CurrencySelectorPresenter presenter;
    private List<Currency> currencies;

    private CurrencySelectorAdapter adapter;
    private SearchView searchView;
    public static final String EXTRA = "serialized_currency";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currencies = new ArrayList<>();

        DataRepository dataRepository = Injection.provideDataRepository();
        presenter = new CurrencySelectorPresenter(this, dataRepository);

        ButterKnife.bind(this);

        currenciesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        presenter.getCurrencies(currencies, this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter currencyList in RecyclerView when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter currencyList in RecyclerView when updating text
                adapter.getFilter().filter(query);

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Close SearchView on tapping back button
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
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
        // None
    }

    @Override
    public void showCurrencies(List<Currency> currencies) {

        if (currencies == null) {
            Crashlytics.logException(new Exception("NullCurrencyException"));
            return;
        }
        adapter = new CurrencySelectorAdapter(currencies, this, this);
        currenciesRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onCurrencySelected(Currency currency) {
        Intent intent = new Intent(CurrencySelector.this, CurrencyCalculator.class);
        if (currency != null) {
            intent.putExtra(EXTRA, currency);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }
        finish();
    }
}
