//package com.harman.taxapp.ui.selectaccount;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.widget.ArrayAdapter;
//
//import androidx.annotation.NonNull;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.harman.taxapp.R;
//
//public class SpinnerAdapter extends ArrayAdapter<String> {
//
//    public SpinnerAdapter(@NonNull Context context, int resource) {
//        super(context, resource);
//        FirebaseDatabase database = FirebaseDatabase.getInstance();;
//        DatabaseReference users = database.getReference("Users");;
//        DatabaseReference mReference;
//
////        database = FirebaseDatabase.getInstance();
////        users = database.getReference("Users");
//
//        SharedPreferences settings = context.getSharedPreferences(String.valueOf(R.string.PREF_FILE), Context.MODE_PRIVATE);
//        SharedPreferences.Editor preferenceEditor = settings.edit();
//
//        String idUser = settings.getString(String.valueOf(R.string.PREF_BASE_ID), "");
//        //Log.d(TAG, "onViewCreated1: " + idUser);
//
//        //new SelectAccountViewModel(idUser).obser
//
//        mReference = users.child(idUser).child("account_names");
//        mReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String allAccounts = String.valueOf(snapshot.getValue());
//                //Log.d(TAG, "onDataChange1: " + allAccounts);
//                preferenceEditor.putString(String.valueOf(R.string.PREF_ALL_ACCOUNTS), allAccounts);
//                preferenceEditor.apply();
//                addAll(allAccounts.split(" "));
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                //Log.d(TAG, "onDataChange2: " + "error!!!!!!!");
//            }
//        });
//        //String[] arrayAllAccounts = LoginActivity.settings.getString(String.valueOf(R.string.PREF_ALL_ACCOUNTS), "").split(" ");
//        //addAll(arrayAllAccounts);
//    }
//
//}
