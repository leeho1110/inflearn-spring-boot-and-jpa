package jpabook.jpashop.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Member {

	@Id @GeneratedValue
	private Long id;
	private String name;

	@Embedded
	private Address address;

	// @JsonIgnore : 최초 멤버 리스트들을 조회하는 로직 중 주문내용을 제거하기 위해 추가했다. 결국 엔티티에 프레젠테이션 계층을 위한 로직(화면에 뿌리기 위한)이 추가되기 시작했다.
	@OneToMany(mappedBy = "member")
	private List<Order> orders = new ArrayList<>();
}
