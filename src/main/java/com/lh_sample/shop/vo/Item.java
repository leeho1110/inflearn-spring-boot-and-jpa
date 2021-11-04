package com.lh_sample.shop.vo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class Item {

    @Id @GeneratedValue
    private int id;

    private String name;
    private int price;
    private int StockQuantity;

}
