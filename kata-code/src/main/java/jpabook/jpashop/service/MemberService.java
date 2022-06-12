package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
// @AllArgsConstructor : [2] 객체의 모든 필드에 대해서 생성자를 생성
@RequiredArgsConstructor // [3] final 필드에 대해서 생성자를 생성
public class MemberService {

	private final MemberRepository memberRepository;

	/*  [1] 생성자 주입 방식
	   	@Autowired
	 	public MemberService(MemberRepository memberRepository) {
	 	this.memberRepository = memberRepository;
	 } */

	// 회원 가입
	@Transactional
	public Long join(Member member){
		validateDuplicateMember(member);
		memberRepository.save(member);

		return member.getId();
	}

	private void validateDuplicateMember(Member member) {
		if(isDuplicatedMember(member)) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}

	private boolean isDuplicatedMember(Member member) {
		return !memberRepository.findByName(member.getName()).isEmpty();
	}

	// 회원 전체 조회 (읽기에는 readOnly=true 를 넣어서 성능 최적화, 최상단 @Transactional 따라감)
	public List<Member> finMembers(){
		return memberRepository.findAll();
	}

	public Member findOne(Long memberId){
		return memberRepository.findOne(memberId);
	}

	@Transactional
	public void update(Long id, String name) {
		Member member = memberRepository.findOne(id);
		member.setName(name);
	}
}
