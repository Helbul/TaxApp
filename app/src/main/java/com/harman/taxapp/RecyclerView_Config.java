package com.harman.taxapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.harman.taxapp.usersdata.YearStatement;

import java.util.List;

public class RecyclerView_Config {
    private Context mContext;
    private YearStatementAdapter mYearStatementAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<YearStatement> yearStatements, List<String> keys){
        mContext = context;
        mYearStatementAdapter = new YearStatementAdapter(yearStatements, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mYearStatementAdapter);
    }

    class YearStatementItemView extends RecyclerView.ViewHolder {
        private TextView mYear;
        private TextView mYearSum;

        private String key;

        public YearStatementItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.year_statement_list_item, parent, false));
            mYear = (TextView) itemView.findViewById(R.id.textViewYear);
            mYearSum = (TextView) itemView.findViewById(R.id.textViewYearSum);
        }

        public void bind (YearStatement yearStatement, String key) {
            mYear.setText(yearStatement.getYear());
            mYearSum.setText((yearStatement.getSumTaxes()));
            this.key = key;
        }
    }

    class YearStatementAdapter extends RecyclerView.Adapter<YearStatementItemView>{
        private List<YearStatement> mYearStatements;
        private List<String> mKeys;

        public YearStatementAdapter(List<YearStatement> mYearStatements, List<String> mKeys) {
            this.mYearStatements = mYearStatements;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public YearStatementItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new YearStatementItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull YearStatementItemView holder, int position) {
            holder.bind(mYearStatements.get(position), mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mYearStatements.size();
        }
    }

}
