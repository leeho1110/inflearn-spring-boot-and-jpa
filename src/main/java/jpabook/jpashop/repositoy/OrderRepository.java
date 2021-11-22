package jpabook.jpashop.repositoy;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
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

	public List<Order> findAllByString(OrderSearch orderSearch) {

		String jpql = "select o from Order o join o.member m";
		boolean isFirstCondition = true;

		//주문 상태 검색
		if (orderSearch.getOrderStatus() != null) {
			if (isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " o.status = :status";
		}

		//회원 이름 검색
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			if (isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " m.name like :name";
		}

		TypedQuery<Order> query = em.createQuery(jpql, Order.class)
			.setMaxResults(1000);

		if (orderSearch.getOrderStatus() != null) {
			query = query.setParameter("status", orderSearch.getOrderStatus());
		}
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			query = query.setParameter("name", orderSearch.getMemberName());
		}

		return query.getResultList();
	}

	/**
	 * JPA Criteria
	 */
	public List<Order> findAllByCriteria(OrderSearch orderSearch) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		Root<Order> o = cq.from(Order.class);
		Join<Object, Object> m = o.join("member", JoinType.INNER);

		List<Predicate> criteria = new ArrayList<>();

		//주문 상태 검색
		if (orderSearch.getOrderStatus() != null) {
			Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
			criteria.add(status);
		}
		//회원 이름 검색
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			Predicate name =
				cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
			criteria.add(name);
		}

		cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
		TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
		return query.getResultList();
	}

	public List<Order> findAllWithMemberDelivery() {
		return em.createQuery(
			"select o from Order o" +
					"join fetch o.member m" +
					"join fetch o.delivery d", Order.class
		).getResultList();
	}

	public List<Order> findAllWithItem() {
		return em.createQuery(
			"select distinct o from Order o" // `distinct` 는 1:N 조인으로 인한 데이터베이스 row 증가를 막아준다.
				+ " join fetch o.member m"
				+ " join fetch o.delivery d"
				+ " join fetch o.orderItems oi"
				+ " join fetch oi.item i", Order.class)
			.setFirstResult(1)
			.setMaxResults(100)
			.getResultList(); // 컬렉

	}
}
