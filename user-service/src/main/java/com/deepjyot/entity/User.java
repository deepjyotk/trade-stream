package com.deepjyot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customer")
@Getter
@Setter
public class User {
    @Id
    private Integer id ;
    private String name;
    private Integer balance;
}
