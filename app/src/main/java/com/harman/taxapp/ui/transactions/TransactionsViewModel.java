package com.harman.taxapp.ui.transactions;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.harman.taxapp.R;
import com.harman.taxapp.firebase.FirebaseDatabaseHelper;
import com.harman.taxapp.usersdata.YearStatement;

import java.util.List;

public class TransactionsViewModel extends AndroidViewModel {
    private MutableLiveData<List<YearStatement>> yearStatements;
    private MutableLiveData<String> idUser;
    private MutableLiveData<String> account;
    private SharedPreferences settings;

    public TransactionsViewModel(Application application) {
        super(application);
        yearStatements = new MutableLiveData<List<YearStatement>>();
        settings = getApplication().getSharedPreferences(String.valueOf(R.string.PREF_FILE), Context.MODE_PRIVATE);
        idUser.setValue(settings.getString(String.valueOf(R.string.PREF_BASE_ID), ""));
        account.setValue(settings.getString(String.valueOf(R.string.PREF_ACCOUNT), ""));

//        new FirebaseDatabaseHelper(idUser.getValue(), account.getValue()).readYearStatements(new FirebaseDatabaseHelper.DataStatus().DataIsLoaded(); {
//
//        });

    }



    public MutableLiveData<List<YearStatement>> getYearStatements() {
        return yearStatements;
    }

    public MutableLiveData<String> getIdUser() {
        return idUser;
    }

    public MutableLiveData<String> getAccount() {
        return account;
    }
}