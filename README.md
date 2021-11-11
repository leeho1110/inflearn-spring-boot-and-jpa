## INFO

주문 서비스의 `주문`, `주문 취소` 메소드는 서비스 메소드 내부가 아닌 비즈니스 로직이 대부분 엔티티에 있다. 

즉 ,서비스 계층은 단순히 엔티티에 필요한 요청을 위임하는 역할(엔티티를 조회하고 필요한 메소드들을 호출하는)만을 수행하고, 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 **도메인 모델 패턴** 이라고 한다.

---
**BindingResult**

MemberForm 에서 @NotEmpty 어노테이션 + 컨트롤러의 MemberForm @Valid을 적용시킨 후, View단에서 데이터가 넘어올 때에 validation에 걸리는 경우 
BindingResult에 해당 에러가 담긴다. 또한 Thymeleaf의 th:erros를 통해 @NotEmpty에 작성한 메시지가 노출된다.
