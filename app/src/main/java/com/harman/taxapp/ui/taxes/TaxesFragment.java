package com.harman.taxapp.ui.taxes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harman.taxapp.databinding.FragmentTaxesBinding;
import com.harman.taxapp.firebase.FirebaseDatabaseHelper;
import com.harman.taxapp.R;
import com.harman.taxapp.activities.MainActivity;
import com.harman.taxapp.ui.transactions.TransactionsViewModel;
import com.harman.taxapp.usersdata.YearStatement;

import java.util.ArrayList;
import java.util.List;


public class TaxesFragment extends Fragment {
    private TransactionsViewModel taxesViewModel;
    private FragmentTaxesBinding binding;

    private RecyclerView mRecyclerView;
    private String idUser;
    private String account;

    public TaxesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        taxesViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
//        binding = FragmentTaxesBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();

        //RecyclerView mRecyclerView = binding.recyclerviewYearStatements;

        //taxesViewModel

        List<YearStatement> yearStatements = new ArrayList<>();
        RecyclerView_YearStatement_Config recyclerView_year= new RecyclerView_YearStatement_Config();

        idUser = MainActivity.settings.getString(String.valueOf(R.string.PREF_BASE_ID), "");
        account = MainActivity.settings.getString(String.valueOf(R.string.PREF_ACCOUNT), "");

        View rootView = inflater.inflate(R.layout.fragment_taxes, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerview_year_statements);

        recyclerView_year.setConfig(mRecyclerView, rootView.getContext(), yearStatements, null);
        new FirebaseDatabaseHelper(idUser, account).readYearStatements(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<YearStatement> yearStatements, List<String> keys) {
                recyclerView_year.setConfig(mRecyclerView, rootView.getContext(), yearStatements, keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
        return rootView;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}