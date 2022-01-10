package com.harman.taxapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.SharedPreferences;

import com.harman.taxapp.R;

public class LoginActivity extends AppCompatActivity {
    public static SharedPreferences settings;//избавиться от статик!!!!!!!!!!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        settings = getSharedPreferences(String.valueOf(R.string.PREF_FILE), MODE_PRIVATE); //избавиться от статика!!!!!!!!!!!!!
        /*

        settings = getSharedPreferences(PREF_FILE, MODE_PRIVATE);


        */
    }



}