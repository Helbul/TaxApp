package com.harman.taxapp.usersdata;

import androidx.annotation.NonNull;

public class Transaction {
    private String id, type, date, valuta;
    private Double rateCentralBank;
    private Double sum, sumRub;

    public Transaction() {
    }

    public Transaction(String id, String type, String date, String valuta, Double sum) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.valuta = valuta;
        this.sum = sum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValuta() {
        return valuta;
    }

    public void setValuta(String valuta) {
        this.valuta = valuta;
    }

    public Double getRateCentralBank() {
        return rateCentralBank;
    }

    public void setRateCentralBank(Double rateCentralBank) {
        this.rateCentralBank = rateCentralBank;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Double getSumRub() {
        return sumRub;
    }

    public void setSumRub(Double sumRub) {
        this.sumRub = sumRub;
    }

    @NonNull
    @Override
    public String toString() {
        String s = getType() + getId() + getSum().toString() + getValuta() + getRateCentralBank().toString() + getSumRub().toString();
        return s;
    }
}
