package com.ashwanth.ApKeyManager.DTO;

public class RegisterDTO {
    private String name;
    private String password;
    private int age;
    private String email;
    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
