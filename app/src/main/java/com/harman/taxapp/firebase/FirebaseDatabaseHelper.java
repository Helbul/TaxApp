package com.harman.taxapp.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.harman.taxapp.R;
import com.harman.taxapp.usersdata.Transaction;
import com.harman.taxapp.usersdata.YearStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirebaseDatabaseHelper {
    private final String TAG = "OLGA";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceYearStatements;
    private List<YearStatement> yearStatements = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private Double sumTaxes = new Double(0);
    //private YearStatement yearStatement = new YearStatement();

    public interface DataStatus{
        void DataIsLoaded(List<YearStatement> yearStatements, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public interface DataStatusTrans {
        void DataIsLoaded(List<Transaction> transactions, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public interface DataStatusYearSum {
        void DataIsLoaded(Double sumTaxes);
        void DataIsInserted();
        void DataIsUpdated();
    }


    public FirebaseDatabaseHelper (String idUser, String account){
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceYearStatements = mDatabase.getReference("Users").child(idUser).child("accounts").child(account);
    }

    public void readYearSum (String year, final DataStatusYearSum dataStatus) {
        DatabaseReference mReferenceYearStatement = mReferenceYearStatements.child(year).child("sumTaxes");
        mReferenceYearStatement.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    sumTaxes = 0.0;
                } else {
                    String s = snapshot.getValue().toString();
                    sumTaxes = Double.parseDouble(s);
                    //sumTaxes = (Double) snapshot.getValue() + 0.0;
                }
                dataStatus.DataIsLoaded(sumTaxes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateYearSum (String year, Double sum, final DataStatusYearSum dataStatusYearSum){
        //DatabaseReference mReferenceYearStatement = mReferenceYearStatements.child(year);
        DatabaseReference mReferenceYearStatement = mReferenceYearStatements.child(year).child("sumTaxes");
        //mReferenceYearStatement.child("year").setValue(year);
        //mReferenceYearStatement.child("sumTaxes").setValue(sum)
        mReferenceYearStatement.setValue(sum)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dataStatusYearSum.DataIsUpdated();
                    }
                });
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

    public void readTransactions(String year, final DataStatusTrans dataStatusTrans) {
        DatabaseReference mReferenceTransactions = mReferenceYearStatements.child(year).child("transactions");
        mReferenceTransactions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> keys = new ArrayList<>();
                transactions.clear();
                for (DataSnapshot keyNode: snapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Transaction transaction = keyNode.getValue(Transaction.class);
                    transactions.add(transaction);
                    //yearStatement = keyNode.getValue(YearStatement.class);
//                    Log.d(TAG, "onDataChange: ну здесь-то должны быть");
//                    if (keyNode.getKey().equals("year")){
//                        yearStatement.setYear(keyNode.getValue(String.class));
//                        Log.d(TAG, "onDataChange: а тут побываем?");
//                        return;
//                    }
//                    if (keyNode.getKey().equals("sumTaxes")){
//                        yearStatement.setSumTaxes(keyNode.getValue(Double.class));
//                        return;
//                    }
//                    Transaction transaction = keyNode.getValue(Transaction.class);
//                    transactions.put(keyNode.getKey(), transaction);
//                    Log.d(TAG, "onDataChange789789: " + transactions.size());
                }
                //yearStatement.setTransactions(transactions);
                dataStatusTrans.DataIsLoaded(transactions, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: ??????????????");
            }
        });
    }

    public void addTransaction(String year, Transaction transaction, final DataStatusTrans dataStatusTrans){
        //DatabaseReference mReferenceCurrentTransaction = mReferenceYearStatements.child(year).child("transactions");
        DatabaseReference mReferenceCurrentTransaction = mReferenceYearStatements.child(year);
        String key = mReferenceCurrentTransaction.child("transactions").push().getKey();
        Log.d(TAG, "addTransaction: " + transaction.toString());
        mReferenceCurrentTransaction.child("transactions").child(key).setValue(transaction)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dataStatusTrans.DataIsInserted();
                        Log.d(TAG, "onSuccess: успех????");
                    }
                });
        mReferenceCurrentTransaction.child("year").setValue(year);
        //добавить запись суммы годового налога
    }

    public void updateTransaction (String year, String key, Transaction transaction, final DataStatusTrans dataStatusTrans){
        DatabaseReference mReferenceCurrentTransaction = mReferenceYearStatements.child(year).child("transactions");
        mReferenceCurrentTransaction.child(key).setValue(transaction)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dataStatusTrans.DataIsUpdated();
                    }
                });
    }

    public void deleteTransaction (String year, String key, final DataStatusTrans dataStatusTrans){
        DatabaseReference mReferenceCurrentTransaction = mReferenceYearStatements.child(year).child("transactions");
        mReferenceCurrentTransaction.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dataStatusTrans.DataIsDeleted();
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
