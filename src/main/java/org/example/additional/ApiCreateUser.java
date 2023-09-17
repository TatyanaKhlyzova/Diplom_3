package org.example;

public class ApiCreateUser {
    public final String email;
    public final String password;
    public final String name;

    public ApiCreateUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
