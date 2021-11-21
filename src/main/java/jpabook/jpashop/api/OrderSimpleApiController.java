package jpabook.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repositoy.OrderRepository;
import jpabook.jpashop.repositoy.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repositoy.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;


// 핵심 : Order를 조회하고, Order 와 연관이 걸리고 (Order -> Member), Order -> Delivery 연관을 갖는다.
// xToOne (ManyToOne, OneToOne) 에서의 성능 최적화를 어떻게 할 것인가!
// Order -> Member (ManyToOne) , Order -> Delivery(OneToOne) <-> Order -> OrderItems(oneToMany, collection)

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

	private final OrderRepository orderRepository;
	private final OrderSimpleQueryRepository orderSimpleQueryRepository;

	@GetMapping("/api/v1/simple-orders")
	public List<Order> ordersV1(){
		List<Order> all = orderRepository.findAllByString(new OrderSearch());
		for (Order order : all) {
			// LAZY 로딩 강제 초기화
			order.getMember().getName();
			order.getDelivery().getAddress();
		}
		return all;
	}

	@GetMapping("/api/v2/simple-orders")
	public List<SimpleOrderDto> ordersV2(){
		return orderRepository.findAllByString(new OrderSearch()).stream()
			.map(SimpleOrderDto::new)
			.collect(Collectors.toList());
	}

	@GetMapping("/api/v3/simple-orders")
	public List<SimpleOrderDto> ordersV3(){
		return orderRepository.findAllWithMemberDelivery().stream()
			.map(SimpleOrderDto::new)
			.collect(Collectors.toList());
	}

	@GetMapping("/api/v4/simple-orders")
	public List<OrderSimpleQueryDto> ordersV4(){
		return orderSimpleQueryRepository.findOrderDtos();
	}

	@Data
	static class SimpleOrderDto {
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address; // value object

		public SimpleOrderDto(Order order) {
			this.orderId = order.getId();
			this.name = order.getMember().getName(); // LAZY 로딩 강제 초기화
			this.orderDate = order.getOrderDate();
			this.orderStatus = order.getStatus();
			this.address = order.getDelivery().getAddress(); // LAZY 로딩 강제 초기화
		}
	}
	
}
