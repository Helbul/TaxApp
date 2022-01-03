package com.harman.taxapp.ui.selectaccount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.harman.taxapp.R;
import com.harman.taxapp.activities.LoginActivity;
import com.harman.taxapp.activities.MainActivity;


public class SelectAccountFragment extends Fragment {

    private static final String TAG = "OLGA say ";// Удалить!!!!!!!!!!
    private FirebaseDatabase database;
    private DatabaseReference users;
    private DatabaseReference mReference;

    public SelectAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences.Editor preferenceEditor = LoginActivity.settings.edit();
        /*
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");



        String idUser = LoginActivity.settings.getString(String.valueOf(R.string.PREF_BASE_ID), "");
        Log.d(TAG, "onViewCreated1: " + idUser);

        mReference = users.child(idUser).child("account_names");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String allAccounts = String.valueOf(snapshot.getValue());
                Log.d(TAG, "onDataChange1: " + allAccounts);
                preferenceEditor.putString(String.valueOf(R.string.PREF_ALL_ACCOUNTS), allAccounts);
                preferenceEditor.apply();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onDataChange2: " + "error!!!!!!!");
            }
        });

        String[] arrayAllAccounts = LoginActivity.settings.getString(String.valueOf(R.string.PREF_ALL_ACCOUNTS), "").split(" ");
        */
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, arrayAllAccounts);
        SpinnerAdapter adapter = new SpinnerAdapter(this.getContext(), android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = view.findViewById(R.id.spinner_accounts);
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                preferenceEditor.putString(String.valueOf(R.string.PREF_ACCOUNT), item);
                preferenceEditor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

        Button buttonOpen = view.findViewById(R.id.button_open_account);
        buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });

        Button buttonCreate = view.findViewById(R.id.button_new_account_create_from_select);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_selectAccountFragment_to_newAccountFragment);
            }
        });
    }
}