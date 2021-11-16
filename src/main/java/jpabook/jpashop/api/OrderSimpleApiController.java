package jpabook.jpashop.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.repositoy.OrderRepository;
import lombok.RequiredArgsConstructor;


// 핵심 : Order를 조회하고, Order 와 연관이 걸리고 (Order -> Member), Order -> Delivery 연관을 갖는다.
// xToOne (ManyToOne, OneToOne) 에서의 성능 최적화를 어떻게 할 것인가!
// Order -> Member (ManyToOne) , Order -> Delivery(OneToOne) <-> Order -> OrderItems(oneToMany, collection)

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

	private final OrderRepository orderRepository;

	@GetMapping("/api/v1/simple-orders")
	public List<Order> ordersV1(){
		List<Order> all = orderRepository.findAllByString(new OrderSearch());

		return all;
	}
}
