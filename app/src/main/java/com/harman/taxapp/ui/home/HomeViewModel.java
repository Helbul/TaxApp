package com.harman.taxapp.ui.home;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.harman.taxapp.R;


public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;


    public HomeViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        //mText.setValue("This is home fragment");

        SharedPreferences allSettings = getApplication().getSharedPreferences(String.valueOf(R.string.PREF_FILE), Context.MODE_PRIVATE);
        String settings = allSettings.getString(String.valueOf(R.string.PREF_EMAIL), "");
        settings += " " + allSettings.getString(String.valueOf(R.string.PREF_BASE_ID), "");
        settings += " " + allSettings.getString(String.valueOf(R.string.PREF_ACCOUNT), "");
        mText.setValue(settings);
    }

    public LiveData<String> getText() {
        return mText;
    }
}