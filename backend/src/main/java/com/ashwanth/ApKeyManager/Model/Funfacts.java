package com.ashwanth.ApKeyManager.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "fun_facts")
public class Funfacts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "fact")
    private String facts;

    public int getId() {
        return id;
    }

    public String getFacts() {
        return facts;
    }
}
