package com.harman.taxapp.usersdata;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name, email, password, phone;
    private ArrayList<Integer> list = new ArrayList<>(); //удалить

    public User() {
        this.list.add(1);//удалить
        this.list.add(2);//удалить
        this.list.add(3);//удалить
    }

    public User(String name, String email, String password, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
