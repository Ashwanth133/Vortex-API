package com.ashwanth.ApKeyManager.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    private Long id;

    private String name;

    @JsonIgnore
    private String password;

    private int age;

    @Column(unique = true, nullable = false)   // ✅ important improvement
    private String email;

    public User() {}

    public User(String name, String password, int age, String email) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.email = email;
    }

    // ❌ remove this — useless constructor
    // public User(LoginDTO loginDTO) {}

    // ✅ getters & setters (clean naming)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}