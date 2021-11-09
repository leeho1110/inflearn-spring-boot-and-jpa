package jpabook.jpashop;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repositoy.MemberRepository;
import jpabook.jpashop.service.MemberService;

@RunWith(SpringRunner.class)
@SpringBootTest // Spring Boot를 띄운 상태에서 테스트하기 위해서
@Transactional // rollback
public class MemberRepositoryTest {

	@Autowired
	MemberService memberService;

	@Autowired
	MemberRepository memberRepository;

	@Test
	public void 회원가입() throws Exception {
		//given
		Member member = new Member();
		member.setName("kim");

		//when
		Long saveId = memberService.join(member);

		//then
		assertThat(memberRepository.findOne(saveId)).isEqualTo(member);
	}

	@Test(expected = IllegalStateException.class)
	public void 중복_회원_예외() throws Exception {
		//given
		Member member1 = new Member();
		member1.setName("kim");

		Member member2 = new Member();
		member2.setName("kim");

		//when
		memberService.join(member1);
		memberService.join(member2);

		//then
		fail("예외가 발생해야 한다.");
	}

}