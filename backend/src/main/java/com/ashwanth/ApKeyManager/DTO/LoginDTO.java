package com.ashwanth.ApKeyManager.DTO;

public class LoginDTO {

    private String password;
    private String email;




    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
