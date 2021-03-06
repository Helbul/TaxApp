package com.harman.taxapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.harman.taxapp.R;
import com.harman.taxapp.databinding.ActivityMainBinding;
import com.harman.taxapp.firebase.ReadName;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences settings;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private ReadName readName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences(String.valueOf(R.string.PREF_FILE), MODE_PRIVATE);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarStatement.toolbar);

        /*
        binding.appBarStatement.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.newTransactionFragment, R.id.taxesFragment, R.id.exitFragment, R.id.transactionsFragment, R.id.ratesTodayFragment)
                .setOpenableLayout(drawer)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_statement);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        View header = navigationView.getHeaderView(0);
        TextView userName = (TextView) header.findViewById(R.id.text_header_name);
        TextView userAccount = (TextView) header.findViewById(R.id.text_header_account);
        userAccount.setText(settings.getString(String.valueOf(R.string.PREF_ACCOUNT), ""));

        readName = new ViewModelProvider(this).get(ReadName.class);
        readName.getName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                userName.setText(s);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.statement, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_statement);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}