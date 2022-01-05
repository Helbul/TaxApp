package com.harman.taxapp.ui.newtransaction;

import static com.harman.taxapp.R.*;
import static com.harman.taxapp.R.layout.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.harman.taxapp.R;
import com.harman.taxapp.firebase.FirebaseDatabaseHelper;
import com.harman.taxapp.activities.MainActivity;
import com.harman.taxapp.usersdata.Transaction;
import com.harman.taxapp.usersdata.YearStatement;

import java.util.List;


public class NewTransactionFragment extends Fragment {
    private final String TAG = "OLGA";

    Double oldSumYear = 0.0;
    Double currentSumYear;
    SharedPreferences settings;

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
        settings = getContext().getSharedPreferences(String.valueOf(string.PREF_FILE), Context.MODE_PRIVATE);

        Spinner typeTransaction = view.findViewById(id.spinner_type_transaction);
        EditText idTrans = view.findViewById(id.edit_id_transaction);
        EditText date = view.findViewById(id.edit_date_transaction);
        EditText sum = view.findViewById(id.edit_sum);
        Spinner currencies = view.findViewById(id.spinner_currencies);

        final String[] arrayTypeTrans = getResources().getStringArray(array.type_transaction);
        final String[] arrayCurrencies = getResources().getStringArray(array.currencies);

        //Вынести ArrayAdapter в отдельный свой класс????
        final ArrayAdapter<String> currenciesArrayAdapter = new ArrayAdapter<String>(this.getContext(), spinner_item, arrayCurrencies){
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
                }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        currenciesArrayAdapter.setDropDownViewResource(spinner_item);
        currencies.setAdapter(currenciesArrayAdapter);


        final ArrayAdapter<String> typeTransArrayAdapter = new ArrayAdapter<String>(this.getContext(), spinner_item, arrayTypeTrans){
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        currenciesArrayAdapter.setDropDownViewResource(spinner_item);
        typeTransaction.setAdapter(typeTransArrayAdapter);


        Button buttonAddTransaction = view.findViewById(id.button_new_transaction_add);
        Button buttonCancel = view.findViewById(id.button_new_transaction_cancel);

        String idUser = settings.getString(String.valueOf(string.PREF_BASE_ID), "");
        String account = settings.getString(String.valueOf(string.PREF_ACCOUNT), "");


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

                if(typeTransaction.getSelectedItemPosition() == 0) {
                    Snackbar.make(v, "Выберите тип сделки.", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(currencies.getSelectedItemPosition() == 0) {
                    Snackbar.make(v, "Выберите валюту сделки.", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                DateCheck dateCheck = new DateCheck(date.getText().toString());
                String year;
                if (!dateCheck.check()) {
                    Snackbar.make(v, "Неправильный формат даты!", Snackbar.LENGTH_SHORT).show();
                    return;
                } else {
                    year = dateCheck.getYear();
                }
                DoubleCheck doubleCheck = new DoubleCheck(sum.getText().toString());
                Double sumDouble;
                if (!doubleCheck.isCheck()) {
                    Snackbar.make(v, "Неправильно введена сумма!", Snackbar.LENGTH_SHORT).show();
                    return;
                } else {
                    sumDouble = doubleCheck.getNumDouble();
                }

                if (TextUtils.isEmpty(idTrans.getText())) {
                    Snackbar.make(v, "Введите наименование сделки", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                Transaction transaction = new Transaction();

                //ДОБАВИТЬ id сделки!!!!

                transaction.setType(typeTransaction.getSelectedItem().toString());
                transaction.setDate(date.getText().toString());
                transaction.setId(idTrans.getText().toString());
                transaction.setSum(sumDouble);
                transaction.setValuta(currencies.getSelectedItem().toString());

                //Добавиь поддрузку данных!!!!!!!!!!!!!!
                Double rateCentralBankDouble = 75.00;
                transaction.setRateCentralBank(rateCentralBankDouble);
                Double sumRubDouble = sumDouble * rateCentralBankDouble;
                transaction.setSumRub(sumRubDouble);

                new FirebaseDatabaseHelper(idUser, account).addTransaction(year, transaction, new FirebaseDatabaseHelper.DataStatusTrans() {
                    @Override
                    public void DataIsLoaded(List<Transaction> transactions, List<String> keys) {}

                    @Override
                    public void DataIsInserted() {
                        Snackbar.make(v, "Сделка добавлена", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void DataIsDeleted() {}

                    @Override
                    public void DataIsUpdated() {}
                });

                new FirebaseDatabaseHelper(idUser, account).readYearSum(year, new FirebaseDatabaseHelper.DataStatusYearSum() {
                    @Override
                    public void DataIsLoaded(Double sumTaxes) {
                        oldSumYear = sumTaxes;
                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }
                });

                currentSumYear = oldSumYear + sumRubDouble;
                Log.d(TAG, "onClick: currentSumYear");
                new FirebaseDatabaseHelper(idUser, account).updateYearSum(year, currentSumYear, new FirebaseDatabaseHelper.DataStatusYearSum() {
                    @Override
                    public void DataIsLoaded(Double sumTaxes) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }
                });

            }
        });

    }
}