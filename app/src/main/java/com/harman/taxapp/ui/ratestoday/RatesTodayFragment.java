package com.harman.taxapp.ui.ratestoday;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harman.taxapp.R;
import com.harman.taxapp.databinding.RatesTodayFragmentBinding;
import com.harman.taxapp.retrofit2.Currencies;
import com.harman.taxapp.retrofit2.CurrencyRate;

import java.util.ArrayList;
import java.util.List;

public class RatesTodayFragment extends Fragment {

    private RatesTodayViewModel mViewModel;
    private RatesTodayFragmentBinding binding;

    public static RatesTodayFragment newInstance() {
        return new RatesTodayFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rates_today_fragment, container, false);
        binding = RatesTodayFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(RatesTodayViewModel.class);
        Currencies currencies = new Currencies();
        currencies.setCurrencyList(new ArrayList<CurrencyRate>());
        RecyclerView_Currency_Config recyclerView_currency= new RecyclerView_Currency_Config();
        RecyclerView mRecyclerView = rootView.findViewById(R.id.recyclerview_currencies);
        recyclerView_currency.setConfig(mRecyclerView, rootView.getContext(), currencies);

        mViewModel.getCurrencies().observe(getViewLifecycleOwner(), new Observer<Currencies>() {
            @Override
            public void onChanged(Currencies currencies) {
                recyclerView_currency.setConfig(mRecyclerView, rootView.getContext(), currencies);
            }
        });

        return rootView;
    }

}