package com.nacosfunaabpay.paymentplatform.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Program {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private String name;

        private String description;

        @OneToMany(mappedBy = "program")
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

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public Set<Student> getStudents() {
                return students;
        }

        public void setStudents(Set<Student> students) {
                this.students = students;
        }
}
