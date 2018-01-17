package com.currencyconverter.vcsathya.mc.view.selector;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.transition.CircularPropagation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.currencyconverter.vcsathya.mc.R;
import com.currencyconverter.vcsathya.mc.data.models.Currency;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.currencyconverter.vcsathya.mc.data.remote.RemoteDataSource.BASE_SERVER;

public class CurrencySelectorAdapter extends RecyclerView.Adapter<CurrencySelectorAdapter.ViewHolder> implements Filterable {

    private List<Currency> currencies;
    private List<Currency> filteredCurrencies;

    Context context;

    private CurrencySelectorAdapterListener listener;

    public CurrencySelectorAdapter(List<Currency> currencies, Context context, CurrencySelectorAdapterListener listener) {
        this.currencies = currencies;
        this.filteredCurrencies = currencies;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String input = constraint.toString();
                if (input.isEmpty()) {
                    filteredCurrencies = currencies;
                } else {
                    List<Currency> filteredList = new ArrayList<>();
                    for (Currency currency : currencies) {
                        if (currency.getCode().toLowerCase().contains(input.toLowerCase())) {
                            filteredList.add(currency);
                        }
                    }

                    filteredCurrencies = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredCurrencies;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredCurrencies = (ArrayList<Currency>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.flag_path)
        ImageView flagPath;

        @BindView(R.id.currency_code)
        TextView currencyCode;

        @BindView(R.id.currency_name)
        TextView currencyName;


        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCurrencySelected(filteredCurrencies.get(getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public CurrencySelectorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.currency_row, parent, false));
    }

    @Override
    public void onBindViewHolder(CurrencySelectorAdapter.ViewHolder holder, int position) {

        Currency currency = filteredCurrencies.get(position);
        String flagUrl = BASE_SERVER+currency.getFlagPath();

        Glide.with(context)
                .load(flagUrl)
                .apply(RequestOptions
                        .bitmapTransform(new CircleCrop())
                        // TODO: Set the image size dynamically according to device screen size
                        .override(100, 100))
                .into(holder.flagPath);

        holder.currencyCode.setText(currency.getCode());
        holder.currencyName.setText(currency.getCurrencyName());

    }

    @Override
    public int getItemCount() {
        return filteredCurrencies.size();
    }


    public interface CurrencySelectorAdapterListener {
        void onCurrencySelected(Currency currency);
    }

}
