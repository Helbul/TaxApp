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
import com.harman.taxapp.firebase.FirebaseDatabaseHelper;
import com.harman.taxapp.retrofit2.Controller;
import com.harman.taxapp.retrofit2.Currencies;
import com.harman.taxapp.usersdata.Transaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewTransactionFragment extends Fragment {
    private final String TAG = "OLGA";

    private Double oldSumYear = 0.0;
    private Double currentSumYear;
    private SharedPreferences settings;
    private Double rateCentralBankDouble;

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

        Spinner typeTransaction = view.findViewById(id.spinner_type_new_trans);
        EditText idTrans = view.findViewById(id.edit_id_new_trans);
        EditText date = view.findViewById(id.edit_date_new_trans);
        EditText sum = view.findViewById(id.edit_sum_new_trans);
        Spinner currencies = view.findViewById(id.spinner_currencies_new_trans);

        final String[] arrayTypeTrans = getResources().getStringArray(array.type_transaction);
        final String[] arrayCurrencies = getResources().getStringArray(array.currencies);

        //Вынести ArrayAdapter в отдельный свой класс????
//        final ArrayAdapter<String> currenciesArrayAdapter = new ArrayAdapter<String>(this.getContext(), spinner_item, arrayCurrencies){
//            @Override
//            public boolean isEnabled(int position) {
//                return position != 0;
//                }
//
//            @Override
//            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view;
//                if(position == 0){
//                    tv.setTextColor(Color.GRAY);
//                }
//                else {
//                    tv.setTextColor(Color.BLACK);
//                }
//                return view;
//            }
//        };
//        currenciesArrayAdapter.setDropDownViewResource(spinner_item);
//        currencies.setAdapter(currenciesArrayAdapter);
        final ArrayAdapter<String> currenciesArrayAdapter = new ArrayAdapter<String>(this.getContext(), spinner_item, arrayCurrencies);
        currencies.setAdapter(currenciesArrayAdapter);
        currencies.setSelection(currenciesArrayAdapter.getPosition("USD"));


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
        typeTransArrayAdapter.setDropDownViewResource(spinner_item);
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

                transaction.setType(typeTransaction.getSelectedItem().toString());
                String dateString = date.getText().toString();
                transaction.setDate(dateString);
                transaction.setId(idTrans.getText().toString());
                transaction.setSum(sumDouble);
                String currency = currencies.getSelectedItem().toString();
                transaction.setValuta(currency);

                //Добавиь поддрузку данных!!!!!!!!!!!!!!
                //rateCentralBankDouble = 1.00;
                Controller ctrl = new Controller(dateString);
                Call<Currencies> currenciesCall = ctrl.prepareCurrenciesCall();
                currenciesCall.enqueue(new Callback<Currencies>() {
                    @Override
                    public void onResponse(Call<Currencies> call, Response<Currencies> response) {
                        if (response.isSuccessful()) {
                            Currencies cur = response.body();
                            rateCentralBankDouble = cur.getCurrencyRate(currency);
                            //currencies.setValue(response.body());
                            transaction.setRateCentralBank(rateCentralBankDouble);
                            Double sumRubDouble = sumDouble * rateCentralBankDouble * 0.13;
                            transaction.setSumRub(sumRubDouble);

                            new FirebaseDatabaseHelper(idUser, account).addTransaction(year, transaction, new FirebaseDatabaseHelper.DataStatusTrans() {
                                @Override
                                public void DataIsLoaded(List<Transaction> transactions, List<String> keys) {}
                                @Override
                                public void DataIsInserted() {
                                    Snackbar.make(v, "Сделка добавлена", Snackbar.LENGTH_SHORT).show();}
                                @Override
                                public void DataIsDeleted() {}
                                @Override
                                public void DataIsUpdated() {}
                            });

                            new FirebaseDatabaseHelper(idUser, account).readYearSumOnce(year, new FirebaseDatabaseHelper.DataStatusYearSum() {
                                @Override
                                public void DataIsLoaded(Double sumTaxes) {
                                    oldSumYear = sumTaxes;
                                    currentSumYear = oldSumYear + sumRubDouble;
                                    Log.d(TAG, "onClick: currentSumYear");
                                    new FirebaseDatabaseHelper(idUser, account).updateYearSum(year, currentSumYear);
                                }

                            });
                        } else {
                            //написать что-то
                        }
                    }

                    @Override
                    public void onFailure(Call<Currencies> call, Throwable t) {
                        //написать что-то
                    }
                });

            }
        });

    }
}