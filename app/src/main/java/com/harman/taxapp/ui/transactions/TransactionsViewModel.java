package com.harman.taxapp.ui.transactions;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.harman.taxapp.R;
import com.harman.taxapp.firebase.FirebaseDatabaseHelper;
import com.harman.taxapp.usersdata.Transaction;
import com.harman.taxapp.usersdata.YearStatement;

import java.util.List;

public class TransactionsViewModel extends AndroidViewModel {
    private final String TAG = "OLGA";
    private SharedPreferences settings;

    MutableLiveData<List<String>> keys;
    MutableLiveData<List<Transaction>> transactions;
    MutableLiveData<Double> sumTaxes;

    public TransactionsViewModel(@NonNull Application application) {
        super(application);

        transactions = new MutableLiveData<List<Transaction>>();
        sumTaxes = new MutableLiveData<Double>();
        keys = new MutableLiveData<List<String>>();

        settings = getApplication().getSharedPreferences(String.valueOf(R.string.PREF_FILE), Context.MODE_PRIVATE);
        String idUser = settings.getString(String.valueOf(R.string.PREF_BASE_ID), "");
        String account = settings.getString(String.valueOf(R.string.PREF_ACCOUNT), "");
        String mYear = settings.getString(String.valueOf(R.string.PREF_YEAR), "");

        new FirebaseDatabaseHelper(idUser, account).readTransactions(mYear, new FirebaseDatabaseHelper.DataStatusTrans() {
            @Override
            public void DataIsLoaded(List<Transaction> transactionsDB, List<String> keysDB) {
                transactions.setValue(transactionsDB);
                keys.setValue(keysDB);
            }

            @Override
            public void DataIsInserted() { }

            @Override
            public void DataIsUpdated() { }

            @Override
            public void DataIsDeleted() { }
        });

        new FirebaseDatabaseHelper(idUser, account).readYearSum(mYear, new FirebaseDatabaseHelper.DataStatusYearSum() {
            @Override
            public void DataIsLoaded(Double sumTaxesDB) {
                sumTaxes.setValue(sumTaxesDB);
            }
        });


    }

    public LiveData<List<String>> getKeys() {
        return keys;
    }

    public MutableLiveData<List<Transaction>> getTransactions() {
        return transactions;
    }

    public MutableLiveData<Double> getSumTaxes() {
        return sumTaxes;
    }

    public void deleteYear (String year) {
        settings = getApplication().getSharedPreferences(String.valueOf(R.string.PREF_FILE), Context.MODE_PRIVATE);
        String idUser = settings.getString(String.valueOf(R.string.PREF_BASE_ID), "");
        String account = settings.getString(String.valueOf(R.string.PREF_ACCOUNT), "");
        new FirebaseDatabaseHelper(idUser, account).deleteYearStatement(year, new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<YearStatement> yearStatements, List<String> keys) {
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
    }
}