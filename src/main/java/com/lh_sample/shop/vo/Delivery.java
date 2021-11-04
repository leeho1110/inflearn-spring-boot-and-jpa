package com.lh_sample.shop.vo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Delivery {

    @Id @GeneratedValue
    private Long id;

    private String city;
    private String street;
    private String zipCode;
    private DeliveryStatus status;

    @OneToOne(mappedBy = "delivery")
    private Orders order;
}
