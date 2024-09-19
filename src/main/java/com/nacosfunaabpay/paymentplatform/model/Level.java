package com.nacosfunaabpay.paymentplatform.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long levelId;

    @Column(nullable = false, unique = true)
    private String levelName;

    @Column(nullable = false)
    private BigDecimal duesAmount;

    @OneToMany(mappedBy = "level")
    private Set<Student> students;

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
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
