package com.nacosfunaabpay.paymentplatform.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private BigDecimal duesAmount;

    @OneToMany(mappedBy = "level")
    private Set<Student> students;

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

    public BigDecimal getDuesAmount() {
        return duesAmount;
    }

    public void setDuesAmount(BigDecimal duesAmount) {
        this.duesAmount = duesAmount;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
}