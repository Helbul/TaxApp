package com.harman.taxapp.ui.transactions;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harman.taxapp.R;
import com.harman.taxapp.databinding.FragmentTransactionsBinding;
import com.harman.taxapp.ui.taxes.TaxesViewModel;
import com.harman.taxapp.usersdata.Transaction;
import com.harman.taxapp.usersdata.YearStatement;

import java.util.ArrayList;
import java.util.List;

public class TransactionsFragment extends Fragment {
    private final String TAG = "OLGA";

    private FragmentTransactionsBinding binding;
    private TransactionsViewModel transactionsViewModel;

    private RecyclerView mRecyclerView;
    private String idUser;
    private String year;
    private SharedPreferences settings;

    public TransactionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        transactionsViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
        binding = FragmentTransactionsBinding.inflate(inflater, container, false);
        View rootView = inflater.inflate(R.layout.fragment_transactions, container, false);

        settings = getContext().getSharedPreferences(String.valueOf(R.string.PREF_FILE), Context.MODE_PRIVATE);
        year = settings.getString(String.valueOf(R.string.PREF_YEAR), "");

        RecyclerView mRecyclerView = rootView.findViewById(R.id.recyclerview_transactions);
        List<Transaction> transactions = new ArrayList<>();
        RecyclerView_Transactions_Config recyclerView_transactions = new RecyclerView_Transactions_Config();
        recyclerView_transactions.setConfig(mRecyclerView, rootView.getContext(), transactions, null);

        transactionsViewModel.getTransactions().observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                transactionsViewModel.getKeys().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> keys) {
                        recyclerView_transactions.setConfig(mRecyclerView, rootView.getContext(), transactions, keys);
                    }
                });

            }
        });
        return rootView;
    }


}