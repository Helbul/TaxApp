package com.harman.taxapp;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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


    public FirebaseDatabaseHelper (){
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceYearStatements = mDatabase.getReference("Users").child("lwjeu0g8SjWR3YWksSJh3yj1Uog1").child("accounts").child("acc1");
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
}
