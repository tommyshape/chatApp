package com.example.terminal;

public class People {

    private String name;
    private String email;
    private String uid;

    public People(){}

    public People(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
    }

    public People(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }
}
