package com.example.moblab2;

public class ContactModel {
    private  String name;
    private String phoneNumber;

    public ContactModel(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return name + " | " + phoneNumber;
    }
}

