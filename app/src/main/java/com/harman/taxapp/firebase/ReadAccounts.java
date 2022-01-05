package com.harman.taxapp.firebase;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.harman.taxapp.R;


//разделить на viewModel и чтение firebase!!!!!!!!!!
public class ReadAccounts extends AndroidViewModel {
    String TAG = "OLGA";
    MutableLiveData<String> allAccounts;

    public ReadAccounts(Application application) {
        super(application);
        allAccounts = new MutableLiveData<>();
        allAccounts.setValue("");

        SharedPreferences settings = getApplication().getSharedPreferences(String.valueOf(R.string.PREF_FILE), Context.MODE_PRIVATE);
        SharedPreferences.Editor preferenceEditor = settings.edit();
        String idUser = settings.getString(String.valueOf(R.string.PREF_BASE_ID), "");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference users = database.getReference("Users");
        DatabaseReference mReference = users.child(idUser).child("account_names");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String allAcc = String.valueOf(snapshot.getValue());
                allAccounts.postValue(allAcc);
                preferenceEditor.putString(String.valueOf(R.string.PREF_ALL_ACCOUNTS), allAcc);
                preferenceEditor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public LiveData<String> getAllAccounts() {
        return allAccounts;
    }
}
