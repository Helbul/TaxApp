package com.harman.taxapp.ui.selectaccount;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.harman.taxapp.R;
import com.harman.taxapp.firebase.FirebaseDatabaseHelper;


public class SelectAccountViewModel extends AndroidViewModel {
    MutableLiveData<String[]> allAccounts;

    public SelectAccountViewModel(Application application) {
        super(application);
        allAccounts = new MutableLiveData<>();

        SharedPreferences settings = getApplication().getSharedPreferences(String.valueOf(R.string.PREF_FILE), Context.MODE_PRIVATE);
        String idUser = settings.getString(String.valueOf(R.string.PREF_BASE_ID), "");

        new FirebaseDatabaseHelper(idUser).readAccounts(new FirebaseDatabaseHelper.DataStatusAccount() {
            @Override
            public void DataIsLoaded(String[] accounts) {
                allAccounts.setValue(accounts);
            }
        });

    }

    public LiveData<String[]> getAllAccounts() {
        return allAccounts;
    }
}
