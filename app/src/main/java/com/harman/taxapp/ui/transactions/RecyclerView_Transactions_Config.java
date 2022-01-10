package com.harman.taxapp.ui.transactions;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.harman.taxapp.R;
import com.harman.taxapp.ui.taxes.RecyclerView_YearStatement_Config;
import com.harman.taxapp.usersdata.Transaction;
import com.harman.taxapp.usersdata.YearStatement;

import java.io.Serializable;
import java.util.List;

public class RecyclerView_Transactions_Config {
    private final String TAG = "OLGA";
    private Context mContext;
    private TransactionAdapter mTransactionsAdapter;

    public void setConfig (RecyclerView recyclerView, Context context, List<Transaction> transactions, List<String> keys){
        //Log.d(TAG, "setConfig1212: " + transactions.get(0).getId());
        Log.d(TAG, "setConfig1212: " + transactions.size());
        mContext = context;
        mTransactionsAdapter = new TransactionAdapter (transactions, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mTransactionsAdapter);
    }


    class TransactionItemView extends RecyclerView.ViewHolder {
        private TextView idTrans;
        private TextView type;
        private TextView date;
        private TextView sum;
        private TextView valuta;
        private TextView cb;
        private TextView sumRub;

        private String key;

        public TransactionItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.transaction_list_item, parent, false));
            idTrans = (TextView) itemView.findViewById(R.id.text_trans_id);
            type = (TextView) itemView.findViewById(R.id.text_trans_type);
            date = (TextView) itemView.findViewById(R.id.text_trans_date);
            sum = (TextView) itemView.findViewById(R.id.text_trans_sum);
            valuta = (TextView) itemView.findViewById(R.id.text_trans_valuta);
            cb = (TextView) itemView.findViewById(R.id.text_trans_cb);
            sumRub = (TextView) itemView.findViewById(R.id.text_trans_sum_rub);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //написать обрабочик нажатия на сделку. Добавить возможность ее редактирования.
                    String s = itemView.getTransitionName();
                    Log.d(TAG, "onClick: SSSS" + s);
                    Bundle bundle = new Bundle();
                    bundle.putString("key", key);
                    Log.d(TAG, "onClick: key" + key);
                    bundle.putString("id", (String) idTrans.getText());
                    Log.d(TAG, "onClick: id" + idTrans.getText());
                    bundle.putString("type", (String) type.getText());
                    Log.d(TAG, "onClick: type" + type.getText());
                    bundle.putString("date", (String) date.getText());
                    Log.d(TAG, "onClick: date" + date.getText());
                    bundle.putString("valuta", String.valueOf(valuta.getText()));
                    Log.d(TAG, "onClick: valuta" + valuta.getText());
                    bundle.putDouble("sum", Double.parseDouble(String.valueOf(sum.getText())));
                    bundle.putDouble("sumRub", Double.parseDouble(String.valueOf(sumRub.getText())));
                    Log.d(TAG, "onClick: sum" + sum.getText());
                    Log.d(TAG, "onClick: bundle" + bundle.getString("key"));

                    Navigation.findNavController(v).navigate(R.id.action_transactionsFragment_to_transactionDetailsFragment, bundle);
                }
            });
        }

        public void bind (Transaction transaction, String key) {
            idTrans.setText(String.valueOf(transaction.getId()));
            type.setText(String.valueOf(transaction.getType()));
            date.setText(String.valueOf(transaction.getDate()));
            sum.setText(String.valueOf(transaction.getSum()));
            valuta.setText(String.valueOf(transaction.getValuta()));
            cb.setText(String.valueOf(transaction.getRateCentralBank()));
            String sumRubString = String.format("%.2f", transaction.getSumRub());
            sumRub.setText(sumRubString);
            this.key = key;
        }
    }

    class TransactionAdapter extends RecyclerView.Adapter<TransactionItemView>{
        private List<Transaction> mTransactions;
        private List<String> mKeys;

        public TransactionAdapter(List<Transaction> transactions, List<String> keys) {
            this.mTransactions = transactions;
            this.mKeys = keys;
        }

        @NonNull
        @Override
        public TransactionItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TransactionItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TransactionItemView holder, int position) {
            holder.bind(mTransactions.get(position), mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mTransactions.size();
        }
    }
}
