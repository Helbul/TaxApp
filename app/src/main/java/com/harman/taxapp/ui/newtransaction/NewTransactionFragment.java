package com.harman.taxapp.ui.newtransaction;

import static com.harman.taxapp.R.*;
import static com.harman.taxapp.R.layout.*;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.harman.taxapp.firebase.FirebaseDatabaseHelper;
import com.harman.taxapp.activities.MainActivity;
import com.harman.taxapp.usersdata.Transaction;
import com.harman.taxapp.usersdata.YearStatement;

import java.util.List;


public class NewTransactionFragment extends Fragment {

    public NewTransactionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(fragment_new_transaction, container, false);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner typeTransaction = view.findViewById(id.spinner_type_transaction);


        EditText date = view.findViewById(id.editDateTransaction);
        EditText sum = view.findViewById(id.editSum);


        Spinner currencies = view.findViewById(id.spinner_currencies);


        final String[] arrayCurrencies = getResources().getStringArray(array.currencies);

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this.getContext(), spinner_item){
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
                }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(spinner_item);
        spinnerArrayAdapter.addAll(arrayCurrencies);
        currencies.setAdapter(spinnerArrayAdapter);


        Button buttonAddTransaction = view.findViewById(id.button_new_transaction_add);
        Button buttonCancel = view.findViewById(id.button_new_transaction_cancel);

        String idUser = MainActivity.settings.getString(String.valueOf(string.PREF_BASE_ID), "");
        String account = MainActivity.settings.getString(String.valueOf(string.PREF_ACCOUNT), "");


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(id.taxesFragment);
            }
        });

        buttonAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //добавить проверку правильности введенных значений или их отсутсвия

                Transaction transaction = new Transaction();
                transaction.setType(typeTransaction.getSelectedItem().toString());
                transaction.setDate(date.getText().toString());

                String year = "2021"; // вычленить year из date!!!!!!!!!!!!!!!

                Double sumDouble = Double.parseDouble(sum.getText().toString());
                transaction.setSum(sumDouble);
                transaction.setValuta(currencies.getSelectedItem().toString());
                Double rateCentralBankDouble = 75.00; //Добавиь поддрузку данных!!!!
                Double sumRubDouble = sumDouble * rateCentralBankDouble;
                transaction.setSumRub(sumRubDouble);

                new FirebaseDatabaseHelper(idUser, account).addTransaction(year, transaction, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<YearStatement> yearStatements, List<String> keys) {
                        Snackbar.make(v, "Сделка добавлена", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });

            }
        });

    }
}