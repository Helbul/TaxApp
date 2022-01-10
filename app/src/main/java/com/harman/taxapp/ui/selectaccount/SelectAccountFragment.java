package com.harman.taxapp.ui.selectaccount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

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


public class SelectAccountFragment extends Fragment {

    private static final String TAG = "OLGA say ";// Удалить!!!!!!!!!!
    private FirebaseDatabase database;
    private DatabaseReference users;
    private DatabaseReference mReference;
    private SelectAccountViewModel selectAccountViewModel;
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
        SharedPreferences.Editor preferenceEditor = LoginActivity.settings.edit();

        selectAccountViewModel = new ViewModelProvider(this).get(SelectAccountViewModel.class);
        binding = FragmentSelectAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item);
        selectAccountViewModel.getAllAccounts().observe(getViewLifecycleOwner(), new Observer<String[]>() {
            @Override
            public void onChanged(String[] accounts) {
                adapter.clear();
                adapter.addAll(accounts);
            }
        });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = root.findViewById(R.id.spinner_accounts);
        spinner.setAdapter(adapter);

        Button buttonOpen = root.findViewById(R.id.button_open_account);
        buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItem() == null) {
                    Snackbar.make(v, "Идет загрузка доступных счетов. Пожалуйста, подождите.", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                String item = spinner.getSelectedItem().toString();
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