package com.harman.taxapp.ui.firstaccount;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.harman.taxapp.R;
import com.harman.taxapp.activities.MainActivity;

public class FirstAccountFragment extends Fragment {

    private SharedPreferences settings;
    private SharedPreferences.Editor preferenceEditor;

    public FirstAccountFragment() {
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
        return inflater.inflate(R.layout.fragment_new_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settings = this.requireActivity().getSharedPreferences(String.valueOf(R.string.PREF_FILE), MODE_PRIVATE);
        preferenceEditor = settings.edit();
        EditText account = view.findViewById(R.id.editAccountName);

        Button buttonCancel = view.findViewById(R.id.button_new_account_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceEditor.putString(String.valueOf(R.string.PREF_ACCOUNT), "DefaultAccount");
                //String.valueOf(R.string.default_account)
                preferenceEditor.apply();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish(); //как по другому закрыть Login Activity???

            }
        });

        Button buttonCreate = view.findViewById(R.id.button_new_account_create);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameNewAccount = account.getText().toString();
                if(TextUtils.isEmpty(nameNewAccount)) {
                    Snackbar.make(v, "Введите имя счета (без пробелов)", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                preferenceEditor.putString(String.valueOf(R.string.PREF_ACCOUNT), nameNewAccount);
                preferenceEditor.apply();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish(); //как по другому закрыть Login Activity???
            }
        });

    }
}