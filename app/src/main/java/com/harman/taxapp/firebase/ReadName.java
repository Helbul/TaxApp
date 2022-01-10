package com.harman.taxapp.firebase;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.harman.taxapp.R;

public class ReadName extends AndroidViewModel {
    String TAG = "OLGA";
    MutableLiveData<String> name;

    public ReadName(Application application) {
        super(application);
        name = new MutableLiveData<>();
        name.setValue("");

        SharedPreferences settings = getApplication().getSharedPreferences(String.valueOf(R.string.PREF_FILE), Context.MODE_PRIVATE);
        SharedPreferences.Editor preferenceEditor = settings.edit();
        String idUser = settings.getString(String.valueOf(R.string.PREF_BASE_ID), "");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference users = database.getReference("Users");
        DatabaseReference mReference = users.child(idUser).child("name");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nameUser = String.valueOf(snapshot.getValue());
                name.postValue(nameUser);
                preferenceEditor.putString(String.valueOf(R.string.PREF_NAME), nameUser);
                preferenceEditor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public LiveData<String> getName() {
        return name;
    }
}
