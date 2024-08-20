package com.deepjyot.entity;

import com.deepjyot.common.Ticker;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PortfolioItem {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "customer_id")
    private Integer userId;
    private Ticker ticker ;
    private Integer quantity ;
}
