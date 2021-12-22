package com.harman.taxapp.usersdata;

public class Transaction {
    private String id, type, data, valuta;
    private String sum, rateCentralBank, sumRub;

    public Transaction(String id, String type, String data, String valuta, String sum) {
        this.id = id;
        this.type = type;
        this.data = data;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getValuta() {
        return valuta;
    }

    public void setValuta(String valuta) {
        this.valuta = valuta;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getRateCentralBank() {
        return rateCentralBank;
    }

    public void setRateCentralBank(String rateCentralBank) {
        this.rateCentralBank = rateCentralBank;
    }

    public String getSumRub() {
        return sumRub;
    }

    public void setSumRub(String sumRub) {
        this.sumRub = sumRub;
    }
}
