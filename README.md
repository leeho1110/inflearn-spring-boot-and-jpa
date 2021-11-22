## INFO

**도메인 모델 패턴**

주문 서비스의 `주문`, `주문 취소` 메소드는 서비스 메소드 내부가 아닌 비즈니스 로직이 대부분 엔티티에 있다. 

즉 ,서비스 계층은 단순히 엔티티에 필요한 요청을 위임하는 역할(엔티티를 조회하고 필요한 메소드들을 호출하는)만을 수행하고, 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 **도메인 모델 패턴** 이라고 한다.

---
**BindingResult**

MemberForm 에서 @NotEmpty 어노테이션 + 컨트롤러의 MemberForm @Valid을 적용시킨 후, View단에서 데이터가 넘어올 때에 validation에 걸리는 경우 
BindingResult에 해당 에러가 담긴다. 또한 Thymeleaf의 th:erros를 통해 @NotEmpty에 작성한 메시지가 노출된다.

---

**merge란?**

영속성 컨텍스트에서 식별자로 *아이템* 을 찾는다. 그 뒤 merge의 파라미터로 넘어온 값들을 *식별자로 찾아놓은 아이템* 의 값들을 모두 변경시킨다.

하지만 merge를 하는 객체의 특정 필드에 set을 하지 않는다면? set되지 않은 필드에는 null이 들어간다. 
따라서 **항상 변경 감지를 사용**해야 한다. 

---

**Query와 Command**

Update API를 생성할 때 강의에서는 `MemberService.update`의 return 값 타입에 Member 객체를 설정하지 않는다. 
이유는 **Command와 Query를 철저히 분리하는 정책** 때문이다. 

만약 `update` 메소드에서 Member객체를 반환한다면 해당 메소드는
**Member의 이름을 업데이트하는 command**와 **변경된 Member를 조회하는 Query**가 같이 있는 꼴이 된다.

> 이 내용은 '객체지향의 사실과 오해 - 조영호' 의 2장, 기계로서의 객체 부분에서 확인할 수 있다.  
---

**양방향 연관관계에서 무한노출을 방지하려면?**

엔티티를 직접 노출할 때(당연히 DTO를 만들어서 노출하겠지만 혹시라도) 양방향 연관관계가 걸린 곳은 꼭 한곳을 `@JsonIgnore` 처리해야한다.
그렇지 않은 경우 양쪽을 서로 호출하면 무한루프 상태에 빠진다.

---

**N+1 문제**

LAZY 로딩을 통해 호출한 후 각 필드들을 강제 초기화시켜도 결국은 N+1 문제가 발생한다. 따라서 fetch 조인을 사용하기 전까지 단순 LAZY 로딩으로는 성능을 최적화시킬 수 없다.
ex) 주문(Order) 2개에서 회원정보(Member), 배송(Delivery) 정보를 확인해보고 싶다고 가정했을 때 주문을 가져오는 쿼리 1번, 가져온 주문 2개에서 각각 회원, 배송을 조회하는 쿼리 2번씩이 나가 총 1+2+2 문제가 발생하게 된다.

---

**엔티티를 DTO로 변환하라**

엔티티를 DTO로 변환하거나, DTO로 바로 조회하는 두가지 방법은 각각 장단점이 있다. 엔티티로 조회하면 리포지토리 재사용성이 높아지고 개발도 단순해진다. 따라서 어쩔수없이 DTO로 바로 조회하려고 한다면 아래와 같은 방식
을 따르는 것이 좋다.

> 쿼리 방식 선택 권장 순서
1. 우선 엔티티를 DTO로 변환하는 방법을 선택.
2. 필요하다면 Fetch 조인으로 성능을 최적화 -> 여기서 대부분의 성능 이슈는 해결.
3. 그래도 안된다면 DTO로 직접 조회.
4. 최후의 방법으로 Native SQL or Spring JDBC Tmepltae을 사용해 SQL 직접 사용

---

**1:N 조인의 경우 Fetch 조인을 통해 중복 row를 제거할 수 있지만, 페이징이 불가능하다는 치명적인 단점**

컬렉션 페치 조인을 사용하면 페이징이 불가능해진다. 우리는 `distinct`를 활용해 중복 row를 제거한 결과를 받지만 실질적으로 
DB의 조회 자체에서 중복이 제거되지는 않는다. 

*ex) distinct 를 통해 우리는 Order 2개를 전달받지만 DB 조회 결과는 Order(2) : OrderItem(4-> 2 * 2) 형태이다.*

따라서 하이버네이트는 조회된 4건에 대해 기준을 잡지 못하고 경고 로그를 남기며 모든 데이터를 DB에서 읽어온 후 메모리에서 페이징한다.
이 작업은 Out of Memory를 유발할 아주 높은 가능성이 있다. 그래서 이 방법은 절대 쓰지 않는 것을 추천한다.

또한 컬렉션 패치 조인은 하나만 가능하다. 컬렉션 둘 이상의 페치 조인을 사용하는 경우 1:N:M 경우가 생겨 데이터 정합성에 문제가 생길 수 있다.

---

