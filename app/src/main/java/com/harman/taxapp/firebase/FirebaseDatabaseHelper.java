package com.harman.taxapp.firebase;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.harman.taxapp.usersdata.Transaction;
import com.harman.taxapp.usersdata.YearStatement;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {


    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceYearStatements;
    private List<YearStatement> yearStatements = new ArrayList<>();

    public interface DataStatus {
        void DataIsLoaded(List<YearStatement> yearStatements, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }


    public FirebaseDatabaseHelper (String idUser, String account){
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceYearStatements = mDatabase.getReference("Users").child(idUser).child("accounts").child(account);
    }

    public void readYearStatements(final DataStatus dataStatus){
        mReferenceYearStatements.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                yearStatements.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode: snapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    YearStatement yearStatement = keyNode.getValue(YearStatement.class);
                    yearStatements.add(yearStatement);
                }
                dataStatus.DataIsLoaded(yearStatements, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addTransaction(String year, Transaction transaction, final DataStatus dataStatus){
        DatabaseReference mReferenceCurrentTransaction = mReferenceYearStatements.child(year);
        String key = mReferenceCurrentTransaction.push().getKey();
        mReferenceCurrentTransaction.child(key).setValue(transaction)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dataStatus.DataIsInserted();
                    }
                });
    }

    public void updateTransaction (String year, String key, Transaction transaction, final DataStatus dataStatus){
        DatabaseReference mReferenceCurrentTransaction = mReferenceYearStatements.child(year);
        mReferenceCurrentTransaction.child(key).setValue(transaction)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dataStatus.DataIsUpdated();
                    }
                });
    }

    public void deleteTransaction (String year, String key, final DataStatus dataStatus){
        DatabaseReference mReferenceCurrentTransaction = mReferenceYearStatements.child(year);
        mReferenceCurrentTransaction.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }

    public void deleteYearStatement (String year, final DataStatus dataStatus){
        mReferenceYearStatements.child(year).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }



}
