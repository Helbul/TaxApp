package com.harman.taxapp.usersdata;

public class Transaction {
    private String type, date, valuta;
    private Double rateCentralBank;
    private Double sum, sumRub;

    public Transaction() {
    }

    public Transaction(String type, String date, String valuta, Double sum) {
        this.type = type;
        this.date = date;
        this.valuta = valuta;
        this.sum = sum;
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

    public void setDate(String data) {
        this.date = data;
    }

    public String getValuta() {
        return valuta;
    }

    public void setValuta(String valuta) {
        this.valuta = valuta;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Double getRateCentralBank() {
        return rateCentralBank;
    }

    public void setRateCentralBank(Double rateCentralBank) {
        this.rateCentralBank = rateCentralBank;
    }

    public Double getSumRub() {
        return sumRub;
    }

    public void setSumRub(Double sumRub) {
        this.sumRub = sumRub;
    }
}
