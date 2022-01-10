package com.harman.taxapp.ui.ratestoday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.harman.taxapp.R;
import com.harman.taxapp.retrofit2.Currencies;
import com.harman.taxapp.retrofit2.CurrencyRate;
import com.harman.taxapp.ui.taxes.RecyclerView_YearStatement_Config;
import com.harman.taxapp.usersdata.YearStatement;

import java.util.List;

public class RecyclerView_Currency_Config {
    private Context mContext;
    private CurrencyAdapter mCurrencyAdapter;

    public void setConfig (RecyclerView recyclerView, Context context, Currencies currencies){
        mContext = context;
        mCurrencyAdapter = new CurrencyAdapter(currencies);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mCurrencyAdapter);
    }

    class CurrencyItemView extends RecyclerView.ViewHolder {
        private TextView mCurrencyCode;
        private TextView mCurrencyName;
        private TextView mCurrencyRate;


        public CurrencyItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.currency_rate_list_item, parent, false));
            mCurrencyCode = itemView.findViewById(R.id.textViewCurrencyCode);
            mCurrencyName = itemView.findViewById(R.id.textViewCurrencyName);
            mCurrencyRate = itemView.findViewById(R.id.textViewCurrencyRate);
        }

        public void bind (CurrencyRate currencyRate) {
            mCurrencyCode.setText(currencyRate.getCharCode());
            mCurrencyName.setText(currencyRate.getName());
            mCurrencyRate.setText(currencyRate.getValueString());
        }
    }



    class CurrencyAdapter extends RecyclerView.Adapter<CurrencyItemView>{
         List <CurrencyRate> rateList;

        public CurrencyAdapter(Currencies currencies) {
            rateList = currencies.getCurrencyList();
        }

        @NonNull
        @Override
        public CurrencyItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CurrencyItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CurrencyItemView holder, int position) {
            holder.bind(rateList.get(position));
        }

        @Override
        public int getItemCount() {
            return rateList.size();
        }
    }
}
