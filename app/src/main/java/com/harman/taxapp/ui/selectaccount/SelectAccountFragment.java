package com.harman.taxapp.ui.selectaccount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.harman.taxapp.R;
import com.harman.taxapp.activities.LoginActivity;
import com.harman.taxapp.activities.MainActivity;
import com.harman.taxapp.databinding.FragmentSelectAccountBinding;
import com.harman.taxapp.firebase.ReadAccounts;


public class SelectAccountFragment extends Fragment {

    private static final String TAG = "OLGA say ";// Удалить!!!!!!!!!!
    private FirebaseDatabase database;
    private DatabaseReference users;
    private DatabaseReference mReference;
    private ReadAccounts readAccounts;
    private FragmentSelectAccountBinding binding;

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
        SharedPreferences.Editor preferenceEditor = LoginActivity.settings.edit();

        readAccounts = new ViewModelProvider(this).get(ReadAccounts.class);
        binding = FragmentSelectAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
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
        //SpinnerAdapter adapter = new SpinnerAdapter(this.getContext(), android.R.layout.simple_spinner_item);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item);
        readAccounts.getAllAccounts().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d(TAG, "onChanged666: " + s);
                adapter.clear();
                adapter.addAll(s.split(" "));
            }
        });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = root.findViewById(R.id.spinner_accounts);
        spinner.setAdapter(adapter);

//        String item = (String)spinner.getItemAtPosition(0);
//        preferenceEditor.putString(String.valueOf(R.string.PREF_ACCOUNT), item);
//        preferenceEditor.apply();

//        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String item = (String)parent.getItemAtPosition(position);
//                Log.d(TAG, "onItemSelected: " + item);
//                preferenceEditor.putString(String.valueOf(R.string.PREF_ACCOUNT), item);
//                preferenceEditor.apply();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        };
//        spinner.setOnItemSelectedListener(itemSelectedListener);

        Button buttonOpen = root.findViewById(R.id.button_open_account);
        buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = spinner.getSelectedItem().toString();
                if (item.isEmpty()) {
                    Snackbar.make(v, "Идет загрузка доступных счетов. Пожалуйста, подождите.", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                preferenceEditor.putString(String.valueOf(R.string.PREF_ACCOUNT), item);
                preferenceEditor.apply();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });

        Button buttonCreate = root.findViewById(R.id.button_new_account_create_from_select);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_selectAccountFragment_to_newAccountFragment);
            }
        });

        return root;
    }
}