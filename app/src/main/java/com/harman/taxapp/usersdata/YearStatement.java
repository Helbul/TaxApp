package com.harman.taxapp.usersdata;

import java.util.ArrayList;

public class YearStatement {
    String year;
    String sumTaxes;
    ArrayList<Transaction> transactions;

    public YearStatement() {}

    public YearStatement(String year, String sumTaxes, ArrayList<Transaction> transactions) {
        this.year = year;
        this.sumTaxes = sumTaxes;
        this.transactions = transactions;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSumTaxes() {
        return sumTaxes;
    }

    public void setSumTaxes(String sumTaxes) {
        this.sumTaxes = sumTaxes;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }


}
