package com.harman.taxapp.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.harman.taxapp.databinding.FragmentHomeBinding;

//import com.harman.taxapp.datausers.R;
//import com.harman.taxapp.datausers.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    SharedPreferences settings;

    private static final String PREF_FILE = "SettingsAccount";
    private static final String PREF_NAME = "Name";
    private static final String PREF_EMAIL = "Email";
    private static final String PREF_BASE_ID = "BaseId";
    private static final String PREF_ACCOUNT = "Account";
    private static final String PREF_ALL_ACCOUNTS = "AllAccounts";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        settings = getContext().getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        String allSettings = settings.getString(PREF_EMAIL, "");
        allSettings += " " + settings.getString(PREF_BASE_ID, "");
        allSettings += " " + settings.getString(PREF_ACCOUNT, "");

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        homeViewModel.setText(allSettings);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}