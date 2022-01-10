package com.example.terminal;

public class RegisterSign {

    private String name;
    private String email;
    private String Uid;


    public RegisterSign(String name, String email, String Uid) {
        this.name = name;
        this.email = email;
        this.Uid = Uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return Uid;
    }
}
