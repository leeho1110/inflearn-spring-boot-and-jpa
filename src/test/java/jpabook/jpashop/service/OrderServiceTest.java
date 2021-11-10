package jpabook.jpashop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repositoy.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

	@Autowired
	EntityManager em;

	@Autowired
	OrderService orderService;

	@Autowired
	OrderRepository orderRepository;


	@Test
	public void 상품주문() throws Exception {
	    // given
		Member member = createMember();
		Book book = craeteBook("책1", 10000, 10);

		int orderCount = 2;

		// when
		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

		// then
		Order getOrder = orderRepository.findOne(orderId);

		assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
		assertThat(getOrder.getOrderItems().size()).isEqualTo(1);
		assertThat(getOrder.getTotalPrice()).isEqualTo(10000 * orderCount);
		assertThat(book.getStockQuantity()).isEqualTo(8);
	}

	@Test(expected = NotEnoughStockException.class)
	public void 상품주문_재고수량초과() throws Exception {
	    // given
		Member member = createMember();
		Book book = craeteBook("책1", 10000, 10);

		int orderCount = 11;

		// when
		orderService.order(member.getId(), book.getId(), orderCount);

	    // then
		fail("재고 수량 부족 예외가 발생해야한다.");
	}

	@Test
	public void 주문취소() throws Exception {
	    // given
		Member member = createMember();
		Book item = craeteBook("책1", 10000,10);

		int orderCount = 2;

		Long orderId = orderService.order(member.getId(),item.getId(), orderCount);

		// when
		orderService.cancelOrder(orderId);

	    // then
		Order getOrder = orderRepository.findOne(orderId);

		assertEquals("주문 취소 시 상태는 CANCEL이다",OrderStatus.CANCEL, getOrder.getStatus());
		assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야한다.",10, item.getStockQuantity());
	}

	private Book craeteBook(String name, int price, int stockQuantity) {
		Book book = new Book();
		book.setName(name);
		book.setPrice(price);
		book.setStockQuantity(stockQuantity);
		em.persist(book);
		return book;
	}

	private Member createMember() {
		Member member = new Member();
		member.setName("회원1");
		member.setAddress(new Address("서울", "강가", "123-123"));
		em.persist(member);
		return member;
	}

}