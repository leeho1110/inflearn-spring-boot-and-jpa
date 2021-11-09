package jpabook.jpashop.repositoy;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor // [3]
public class MemberRepository {

	// @PersistenceContext [1] 어노테이션을 통해 엔티티 매니저 주입
	// @Autowired // [2] 스프링 데이터 JPA가 지원해줌
	private final EntityManager em;

	public void save(Member member) {
		em.persist(member);
	}

	public Member findOne(Long id){
		return em.find(Member.class, id);
	}

	public List<Member> findAll() {
		return em.createQuery("select m from Member m", Member.class)
			.getResultList();
	}

	public List<Member> findByName(String name){
		return em.createQuery("select m from Member m where m.name = :name",Member.class)
			.setParameter("name", name)
			.getResultList();
	}

}
