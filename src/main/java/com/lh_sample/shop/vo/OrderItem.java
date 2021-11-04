package com.lh_sample.shop.vo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    private int orderPrice;
    private int count;
}
