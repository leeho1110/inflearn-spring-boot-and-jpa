package com.lh_sample.team;

import com.lh_sample.team.vo.Member;
import com.lh_sample.team.vo.Team;

import javax.persistence.*;
import java.util.List;

public class start {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("team");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        try {
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUserName("member_test_lh");
            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());

            Team findTeam = findMember.getTeam();
            List<Member> members = findMember.getTeam().getMembers();

            for(Member m : members) {
                System.out.println("member Name From Team= " + m.getUserName());
            }


            tx.commit();
        } catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
