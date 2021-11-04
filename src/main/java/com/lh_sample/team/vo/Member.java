package com.lh_sample.team.vo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private long id;

    @Column(name = "USERNAME")
    private String userName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }


}
