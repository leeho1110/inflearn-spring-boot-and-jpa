package com.lh_sample.sample.practice_until_2;

import com.lh_sample.sample.practice_until_2.vo.Member;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

@SpringBootApplication
public class start {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			logic(em);
			tx.commit();
		} catch (Exception e){
			e.printStackTrace();
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}

	private static void logic(EntityManager em) {

		Member member = new Member();
		member.setName("지한");
		member.setAge(2);

		// 등록
		em.persist(member);

		// 수정
		member.setAge(20);

		// 한 건 조회
		Member findMember = em.find(Member.class, Long.valueOf(1));
		System.out.println("findMember="+findMember.getName() + ", age=" + findMember.getAge());

		// 목록 조회
		List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
		System.out.println("members.size="+members.size());

		// 삭제
		em.remove(member);
	}


}
