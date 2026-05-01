package com.ashwanth.ApKeyManager.DTO;

public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private Integer age;

    public UserDTO(Long id, String email, String name, Integer age) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}