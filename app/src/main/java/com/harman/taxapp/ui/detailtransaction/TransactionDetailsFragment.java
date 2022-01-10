package com.harman.taxapp.ui.detailtransaction;

import static com.harman.taxapp.R.layout.spinner_item;

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

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.harman.taxapp.R;
import com.harman.taxapp.firebase.FirebaseDatabaseHelper;
import com.harman.taxapp.retrofit2.Controller;
import com.harman.taxapp.retrofit2.Currencies;
import com.harman.taxapp.ui.newtransaction.DateCheck;
import com.harman.taxapp.ui.newtransaction.DoubleCheck;
import com.harman.taxapp.usersdata.Transaction;
import com.harman.taxapp.usersdata.YearStatement;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionDetailsFragment extends Fragment {
    private final String TAG = "OLGA";

    private Double oldSumYear = 0.0;
    private Double currentYearSum;
    private SharedPreferences settings;
    private Double rateCentralBankDouble;

    public TransactionDetailsFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        View viewRoot = inflater.inflate(R.layout.fragment_transaction_details, container, false);
        settings = getContext().getSharedPreferences(String.valueOf(R.string.PREF_FILE), Context.MODE_PRIVATE);



        Spinner typeTransaction = viewRoot.findViewById(R.id.spinner_type_detail);
        EditText idTrans = viewRoot.findViewById(R.id.edit_id_detail);
        EditText date = viewRoot.findViewById(R.id.edit_date_detail);
        EditText sum = viewRoot.findViewById(R.id.edit_sum_detail);
        Spinner currencies = viewRoot.findViewById(R.id.spinner_currencies_detail);

        final Double oldSum;
        final Double oldSumRub;
        final String key;


            key = getArguments().getString("key");
            idTrans.setText(getArguments().getString("id", ""));
            date.setText(getArguments().getString("date", ""));
            oldSum = getArguments().getDouble("sum", 0.00);
            oldSumRub = getArguments().getDouble("sumRub", 0.00);
            sum.setText(oldSum.toString());


        final String[] arrayTypeTrans = getResources().getStringArray(R.array.type_transaction);
        final String[] arrayCurrencies = getResources().getStringArray(R.array.currencies);

        //Вынести ArrayAdapter в отдельный свой класс????
//        final ArrayAdapter<String> currenciesArrayAdapter = new ArrayAdapter<String>(this.getContext(), spinner_item, arrayCurrencies){
//            @Override
//            public boolean isEnabled(int position) {
//                return position != 0;
//            }
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
        final ArrayAdapter<String> currenciesArrayAdapter = new ArrayAdapter<String>(this.getContext(), spinner_item, arrayCurrencies);
        currencies.setAdapter(currenciesArrayAdapter);
        currencies.setSelection(currenciesArrayAdapter.getPosition(getArguments().getString("valuta")));


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
        typeTransaction.setSelection(typeTransArrayAdapter.getPosition(getArguments().getString("type")));

        Button buttonUpdate = viewRoot.findViewById(R.id.button_update_detail);
        Button buttonCancel = viewRoot.findViewById(R.id.button_cancel_detail);
        Button buttonDelete = viewRoot.findViewById(R.id.button_delete_detail);

        String idUser = settings.getString(String.valueOf(R.string.PREF_BASE_ID), "");
        String account = settings.getString(String.valueOf(R.string.PREF_ACCOUNT), "");
        String oldYear = settings.getString(String.valueOf(R.string.PREF_YEAR), "");


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_transactionDetailsFragment_to_transactionsFragment);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new FirebaseDatabaseHelper(idUser, account).readYearSumOnce(oldYear, new FirebaseDatabaseHelper.DataStatusYearSum() {
                    @Override
                    public void DataIsLoaded(Double sumTaxes) {

                        oldSumYear = sumTaxes;
                        Log.d(TAG, "DataIsLoaded - del - read oldSumYear: " + oldSumYear);
                        currentYearSum = oldSumYear - oldSumRub;
                        Log.d(TAG, "onClick Del: currentYearSum = oldSumYear - oldSumRub: " + currentYearSum);
                        if (currentYearSum == 0) {
                            new FirebaseDatabaseHelper(idUser, account).deleteYearStatement(oldYear, new FirebaseDatabaseHelper.DataStatus() {
                                @Override
                                public void DataIsLoaded(List<YearStatement> yearStatements, List<String> keys) {

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
                        } else {
                            new FirebaseDatabaseHelper(idUser, account).updateYearSum(oldYear, currentYearSum);
                        }

                    }
                });



                new FirebaseDatabaseHelper(idUser, account).deleteTransaction(oldYear, key, new FirebaseDatabaseHelper.DataStatusTrans() {
                    @Override
                    public void DataIsLoaded(List<Transaction> transactions, List<String> keys) { }

                    @Override
                    public void DataIsInserted() {}

                    @Override
                    public void DataIsUpdated() {}

                    @Override
                    public void DataIsDeleted() {
                        Snackbar.make(v, "Сделка удалена", BaseTransientBottomBar.LENGTH_SHORT).show();
                        Navigation.findNavController(v).navigate(R.id.action_transactionDetailsFragment_to_transactionsFragment);
                    }
                });
            }
        });


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(typeTransaction.getSelectedItemPosition() == 0) {
                    Snackbar.make(v, "Выберите тип сделки.", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(currencies.getSelectedItemPosition() == 0) {
                    Snackbar.make(v, "Выберите валюту сделки.", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                DateCheck dateCheck = new DateCheck(date.getText().toString());
                String currentYear;
                if (!dateCheck.check()) {
                    Snackbar.make(v, "Неправильный формат даты!", Snackbar.LENGTH_SHORT).show();
                    return;
                } else {
                    currentYear = dateCheck.getYear();
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
//                Double rateCentralBankDouble = 2.00;
//                transaction.setRateCentralBank(rateCentralBankDouble);
//                Double sumRubDouble = sumDouble * rateCentralBankDouble;
//                transaction.setSumRub(sumRubDouble);

                if (oldYear.equals(currentYear)) {
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

                                new FirebaseDatabaseHelper(idUser, account).updateTransaction(oldYear, key, transaction, new FirebaseDatabaseHelper.DataStatusTrans() {
                                    @Override
                                    public void DataIsLoaded(List<Transaction> transactions, List<String> keys) {
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

                                new FirebaseDatabaseHelper(idUser, account).readYearSumOnce(oldYear, new FirebaseDatabaseHelper.DataStatusYearSum() {
                                    @Override
                                    public void DataIsLoaded(Double sumTaxes) {
                                        oldSumYear = sumTaxes;
                                        currentYearSum = oldSumYear - oldSumRub + sumRubDouble;
                                        Log.d(TAG, "onClick: currentSumYear" + currentYearSum);
                                        new FirebaseDatabaseHelper(idUser, account).updateYearSum(oldYear, currentYearSum);
                                        Snackbar.make(v, "Сделка обновлена", BaseTransientBottomBar.LENGTH_SHORT).show();
                                        Navigation.findNavController(v).navigate(R.id.action_transactionDetailsFragment_to_transactionsFragment);
                                    }
                                });

                            } else { //*
                                //написать что-то//*
                            }//*
                        }//*

                        @Override //*
                        public void onFailure(Call<Currencies> call, Throwable t) { //*
                            //написать что-то //*
                        } //*
                    }); //*
                }

                //Если изменена даты сделки, то она сначала удаляется, а потом доавляется новая в другой директрии соответсвующей году
                if (!oldYear.equals(currentYear)) {

                    //удаляем сделку:
                    new FirebaseDatabaseHelper(idUser, account).readYearSumOnce(oldYear, new FirebaseDatabaseHelper.DataStatusYearSum() {
                        @Override
                        public void DataIsLoaded(Double sumTaxes) {
                            oldSumYear = sumTaxes;
                            currentYearSum = oldSumYear - oldSumRub;

                            new FirebaseDatabaseHelper(idUser, account).updateYearSum(oldYear, currentYearSum);
                        }
                    });



                    new FirebaseDatabaseHelper(idUser, account).deleteTransaction(oldYear, key, new FirebaseDatabaseHelper.DataStatusTrans() {
                        @Override
                        public void DataIsLoaded(List<Transaction> transactions, List<String> keys) {
                        }

                        @Override
                        public void DataIsInserted() {
                        }

                        @Override
                        public void DataIsUpdated() {
                        }

                        @Override
                        public void DataIsDeleted() {}
                    });

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

                                //теперь добавляем сделку:
                                new FirebaseDatabaseHelper(idUser, account).addTransaction(currentYear, transaction, new FirebaseDatabaseHelper.DataStatusTrans() {
                                    @Override
                                    public void DataIsLoaded(List<Transaction> transactions, List<String> keys) {
                                    }

                                    @Override
                                    public void DataIsInserted() {
                                        Snackbar.make(v, "Сделка обновлена", Snackbar.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void DataIsDeleted() {
                                    }

                                    @Override
                                    public void DataIsUpdated() {
                                    }
                                });

                                new FirebaseDatabaseHelper(idUser, account).readYearSum(currentYear, new FirebaseDatabaseHelper.DataStatusYearSum() {
                                    @Override
                                    public void DataIsLoaded(Double sumTaxes) {
                                        oldSumYear = sumTaxes;
                                    }
                                });

                                currentYearSum = oldSumYear + sumRubDouble;
                                Log.d(TAG, "onClick: currentSumYear");
                                new FirebaseDatabaseHelper(idUser, account).updateYearSum(currentYear, currentYearSum);
                                Navigation.findNavController(v).navigate(R.id.action_transactionDetailsFragment_to_transactionsFragment);
                            }else {
                                //написать что-то
                            }
                        }

                        @Override
                        public void onFailure(Call<Currencies> call, Throwable t) {
                            //написать что-то
                        }
                    });
                }
            }
        });


        return viewRoot;
    }
}