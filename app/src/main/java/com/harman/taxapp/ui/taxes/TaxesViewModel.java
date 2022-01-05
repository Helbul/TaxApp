package com.harman.taxapp.ui.taxes;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.harman.taxapp.R;
import com.harman.taxapp.firebase.FirebaseDatabaseHelper;
import com.harman.taxapp.usersdata.YearStatement;

import java.util.List;

public class TaxesViewModel extends AndroidViewModel {
    private SharedPreferences settings;

    MutableLiveData<List<YearStatement>> yearStatements;
    MutableLiveData<List<String>> keys;


    public TaxesViewModel(@NonNull Application application) {
        super(application);
        yearStatements = new MutableLiveData<List<YearStatement>>();
        keys = new MutableLiveData<List<String>>();
        settings = getApplication().getSharedPreferences(String.valueOf(R.string.PREF_FILE), Context.MODE_PRIVATE);
        String idUser = settings.getString(String.valueOf(R.string.PREF_BASE_ID), "");
        String account = settings.getString(String.valueOf(R.string.PREF_ACCOUNT), "");


        new FirebaseDatabaseHelper(idUser, account).readYearStatements(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<YearStatement> statements, List<String> keysBD) {
                yearStatements.setValue(statements);
                keys.setValue(keysBD);
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

    public LiveData<List<YearStatement>> getYearStatements() {
        return yearStatements;
    }

    public LiveData<List<String>> getKeys() {
        return keys;
    }
}
