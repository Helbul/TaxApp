package com.harman.taxapp.ui.newtransaction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;
import com.harman.taxapp.firebase.FirebaseDatabaseHelper;
import com.harman.taxapp.R;
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
        return inflater.inflate(R.layout.fragment_new_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner typeTransaction = view.findViewById(R.id.spinner_type_transaction);
        EditText date = view.findViewById(R.id.editDateTransaction);
        EditText sum = view.findViewById(R.id.editSum);
        Spinner currencies = view.findViewById(R.id.spinner_currencies);

        Button buttonAddTransaction = view.findViewById(R.id.button_new_transaction_add);
        Button buttonCancel = view.findViewById(R.id.button_new_transacrion_cancel);

        String idUser = MainActivity.settings.getString(String.valueOf(R.string.PREF_BASE_ID), "");
        String account = MainActivity.settings.getString(String.valueOf(R.string.PREF_ACCOUNT), "");


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.taxesFragment);
            }
        });

        buttonAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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