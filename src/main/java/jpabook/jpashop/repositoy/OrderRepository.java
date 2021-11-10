package jpabook.jpashop.repositoy;

import java.util.List;
import java.util.OptionalDouble;

import javax.persistence.EntityManager;

import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

	private final EntityManager em;

	public void save(Order order){
		em.persist(order);
	}

	public Order findOne(Long id){
		return em.find(Order.class, id);
	}
}
