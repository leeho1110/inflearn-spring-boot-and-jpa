package com.lh_sample.shop.vo;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private int id;

    @OneToMany(mappedBy = "member")
    private List<Orders> orders = new ArrayList<>();

    private String name;
    private String city;
    private String street;
    private String zipcode;

}
