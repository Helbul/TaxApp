package com.harman.taxapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harman.taxapp.usersdata.YearStatement;

import java.util.List;


public class TaxesFragment extends Fragment {
    private RecyclerView mRecyclerView;


    public TaxesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_taxes, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerview_year_statements);
        new FirebaseDatabaseHelper().readYearStatements(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<YearStatement> yearStatements, List<String> keys) {
                new RecyclerView_Config().setConfig(mRecyclerView, rootView.getContext(), yearStatements, keys);
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