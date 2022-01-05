package com.harman.taxapp.ui.taxes;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.harman.taxapp.R;
import com.harman.taxapp.usersdata.YearStatement;

import java.util.List;

public class RecyclerView_YearStatement_Config {
    private Context mContext;
    private YearStatementAdapter mYearStatementAdapter;
    private SharedPreferences settings;
    private SharedPreferences.Editor editorSettings;
    

    public void setConfig(RecyclerView recyclerView, Context context, List<YearStatement> yearStatements, List<String> keys){
        mContext = context;
        settings = mContext.getSharedPreferences(String.valueOf(R.string.PREF_FILE), Context.MODE_PRIVATE);
        editorSettings = settings.edit();
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editorSettings.putString(String.valueOf(R.string.PREF_YEAR), String.valueOf(mYear.getText()));
                    editorSettings.apply();
                    editorSettings.putString(String.valueOf(R.string.PREF_YEAR_SUM), String.valueOf(mYearSum.getText()));
                    editorSettings.apply();
                    Navigation.findNavController(v).navigate(R.id.action_taxesFragment_to_transactionsFragment);
                }
            });
        }

        public void bind (YearStatement yearStatement, String key) {
            mYear.setText(String.valueOf(yearStatement.getYear()));
            mYearSum.setText(String.valueOf(yearStatement.getSumTaxes()));
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
