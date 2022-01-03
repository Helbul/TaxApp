package com.harman.taxapp.ui.signin;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.harman.taxapp.R;
import com.harman.taxapp.activities.LoginActivity;

public class SignInFragment extends Fragment {
    private final String TAG = "Olga";
    FirebaseAuth auth;

    public SignInFragment() {
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

        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        //LoginActivity.settings = this.requireActivity().getSharedPreferences(String.valueOf(R.string.PREF_FILE), MODE_PRIVATE); //??? requireActivity

        EditText email = view.findViewById(R.id.editTextEmail);
        EditText password = view.findViewById(R.id.editTextPassword);

        Button buttonCancel = view.findViewById(R.id.button_new_account_cancel);
        buttonCancel.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_signInFragment_to_loginFragment));

        Button buttonSignIn = view.findViewById(R.id.button_new_account_create);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(v, "Введите ваш email", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (password.getText().toString().length() < 8 ) {
                    Snackbar.make(v, "Неверные параметры входа! Повторите попытку.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //авторизация пользователя
                String emailString = email.getText().toString();
                //Log.d(TAG, "onClick1: " + emailString);
                auth.signInWithEmailAndPassword(emailString, password.getText().toString())
                        //может поменять на addOnCompleteListener??
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                SharedPreferences.Editor preferenceEditor = LoginActivity.settings.edit();
                                preferenceEditor.putString(String.valueOf(R.string.PREF_EMAIL), email.getText().toString());
                                preferenceEditor.apply();
                                String idUser = auth.getCurrentUser().getUid();
                                preferenceEditor.putString(String.valueOf(R.string.PREF_BASE_ID), idUser);
                                preferenceEditor.apply();

                                Navigation.findNavController(v).navigate(R.id.action_signInFragment_to_selectAccountFragment);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(v, "Ошибка авторизации. " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                            });
            }
        });

    }
    //скрыть клавиатуру
    /*@Override
    public void onStop() {
        InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
        //val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0)
        super.onStop();
    }*/
}