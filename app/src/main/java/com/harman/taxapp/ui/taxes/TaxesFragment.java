package com.harman.taxapp.ui.taxes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.harman.taxapp.databinding.FragmentTaxesBinding;
import com.harman.taxapp.firebase.FirebaseDatabaseHelper;
import com.harman.taxapp.R;
import com.harman.taxapp.activities.MainActivity;
import com.harman.taxapp.usersdata.YearStatement;

import java.util.ArrayList;
import java.util.List;


public class TaxesFragment extends Fragment {
    final String TAG = "OLGA";
    private FragmentTaxesBinding binding;
    private TaxesViewModel taxesViewModel;

//    private RecyclerView mRecyclerView;
//    private String idUser;
//    private String account;
//
//    private SharedPreferences settings;

    public TaxesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        taxesViewModel = new ViewModelProvider(this).get(TaxesViewModel.class);
        binding = FragmentTaxesBinding.inflate(inflater, container, false);
        RecyclerView mRecyclerView = binding.recyclerviewYearStatements;

        List<YearStatement> yearStatements = new ArrayList<>();
        RecyclerView_YearStatement_Config recyclerView_year= new RecyclerView_YearStatement_Config();

        View rootView = inflater.inflate(R.layout.fragment_taxes, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerview_year_statements);

        recyclerView_year.setConfig(mRecyclerView, rootView.getContext(), yearStatements, null);

        RecyclerView finalMRecyclerView = mRecyclerView;
        taxesViewModel.getYearStatements().observe(getViewLifecycleOwner(), new Observer<List<YearStatement>>() {
            @Override
            public void onChanged(List<YearStatement> yearStatements) {
                taxesViewModel.getKeys().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> keys) {
                        if (taxesViewModel.getKeys().getValue() != null) {
                            recyclerView_year.setConfig(finalMRecyclerView, rootView.getContext(), yearStatements, keys);
                        }
                    }
                });

            }
        });

//        new FirebaseDatabaseHelper(idUser, account).readYearStatements(new FirebaseDatabaseHelper.DataStatus() {
//            @Override
//            public void DataIsLoaded(List<YearStatement> yearStatements, List<String> keys) {
//                recyclerView_year.setConfig(mRecyclerView, rootView.getContext(), yearStatements, keys);
//            }
//
//            @Override
//            public void DataIsInserted() {
//
//            }
//
//            @Override
//            public void DataIsUpdated() {
//
//            }
//
//            @Override
//            public void DataIsDeleted() {
//
//            }
//        });


        Button buttonAddTrans = rootView.findViewById(R.id.button_taxes_add_trans);
        buttonAddTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).navigate(R.id.action_nav_taxes_to_nav_new_transaction);
            }
        });


        Button buttonLoading = rootView.findViewById(R.id.button_taxes_loading);
        buttonLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //написать обработчик
                //прописать логику обработки отчета xls or ...
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}