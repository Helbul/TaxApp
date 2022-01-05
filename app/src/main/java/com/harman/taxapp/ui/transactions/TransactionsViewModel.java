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

    //MutableLiveData<YearStatement> yearStatement;
    MutableLiveData<List<String>> keys;
    MutableLiveData<List<Transaction>> transactions;
    //MutableLiveData<Double> sumTaxes;
    //MutableLiveData<String> year;

    public TransactionsViewModel(@NonNull Application application) {
        super(application);

        //yearStatement = new MutableLiveData<YearStatement>();
        transactions = new MutableLiveData<List<Transaction>>();
        //sumTaxes = new MutableLiveData<Double>();
        keys = new MutableLiveData<List<String>>();
        settings = getApplication().getSharedPreferences(String.valueOf(R.string.PREF_FILE), Context.MODE_PRIVATE);
        String idUser = settings.getString(String.valueOf(R.string.PREF_BASE_ID), "");
        String account = settings.getString(String.valueOf(R.string.PREF_ACCOUNT), "");
        String year = settings.getString(String.valueOf(R.string.PREF_YEAR), "");
        Log.i(TAG, "TransactionsViewModel: " + year);

        new FirebaseDatabaseHelper(idUser, account).readTransactions(year, new FirebaseDatabaseHelper.DataStatusTrans() {
            @Override
            public void DataIsLoaded(List<Transaction> transactionsDB, List<String> keysDB) {
                Log.d(TAG, "DataIsLoaded: сюда доходит???");
                Log.d(TAG, "DataIsLoaded: " + transactionsDB.size());
                //сюда даже не дошел...
                transactions.setValue(transactionsDB);
                keys.setValue(keysDB);

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

//    public MutableLiveData<YearStatement> getYearStatement() {
//        return yearStatement;
//    }

    public LiveData<List<String>> getKeys() {
        return keys;
    }

    public MutableLiveData<List<Transaction>> getTransactions() {
        return transactions;
    }

//    public MutableLiveData<Double> getSumTaxes() {
//        return sumTaxes;
//    }

//    public MutableLiveData<String> getYear() {
//        return year;
//    }
}