package com.lh_sample.sample.practice_until_2.vo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "MEMBER", uniqueConstraints = {@UniqueConstraint(name = "NAME_AGE_UNIQUE", columnNames = {"NAME", "AGE"})})
public class Member {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "NAME", nullable = false, length = 10)
    private String name;

    private Integer age;

    // java enum을 통해서 컬럼을 매핑하는 경우 @Enumerated
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    // 자바의 날짜 타입은 @Temporal
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    // CLOB, BLOB 타입 매핑
    @Lob
    private String description;

}
