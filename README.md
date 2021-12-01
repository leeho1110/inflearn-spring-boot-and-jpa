## INFO

1. [실전! 스프링 부트와 JPA 활용1 - 웹 애플리케이션 개발](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-%ED%99%9C%EC%9A%A9-1/dashboard)
2. [실전! 스프링 부트와 JPA 활용2 - API 개발과 성능 최적화](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-API%EA%B0%9C%EB%B0%9C-%EC%84%B1%EB%8A%A5%EC%B5%9C%EC%A0%81%ED%99%94/dashboard)
3. [실전! 스프링 데이터 JPA](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-JPA-%EC%8B%A4%EC%A0%84)
4. [실전! Querydsl](https://www.inflearn.com/course/Querydsl-%EC%8B%A4%EC%A0%84)

---

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

따라서 하이버네이트는 우리가 의도한 Order 2건에 대한 페이징이 아닌 OrderItem 4건으로 기준을 잡게 된다. 이후 경고 로그를 남기며 모든 데이터를 DB에서 읽어온 후 메모리에서 페이징한다.
이 작업은 Out of Memory를 유발할 아주 높은 가능성이 있다. 그래서 이 방법은 절대 쓰지 않는 것을 추천한다.

또한 컬렉션 패치 조인은 하나만 가능하다. 컬렉션 둘 이상의 페치 조인을 사용하는 경우 1:N:M 경우가 생겨 데이터 정합성에 문제가 생길 수 있다.

---

**페이징 한계 돌파**

1. ToOne(OneToOne, ManyToOne) 관계는 모두 Fetch 조인한다. 그 이유는 ToOne 관계는 row수를 증가시키지(뻥튀기) 않기 때문이다.
2. 문제가 생겼었던 컬렉션은 지연 로딩으로 조회하되, 성능 최적화를 위해 `hibernate.default_batch_fetch_size`-글로벌 설정, `@BatchSize`-개별 최적화 를 적용한다.
3. 위 옵션을 통해 컬렉션이나, 프록시 객체를 설정한 size만큼 한꺼번에 `IN` 쿼리로 조회한다.

#### 장점 ####
1. `1+N` -> `1+1` 로 최적화된다.
2. 조인(`Orderv3`)보다 DB 데이터 전송량이 최적화된다 -> Fetch 조인 방식과 비교해서 쿼리 호출 수는 증가하지만, 데이터 전송량이 감소
3. 컬렉션 페치 조인은 페이징이 불가능하지만 이 방법은 페이징이 가능하다 -> 아주 큰 장점

**결론** : ToOne 관계는 Fetch 조인해서 쿼리수를 줄이는 것 + 페이징에 영향을 주지 않도록 해결하고, 나머지는 `hibernate.default_batch_fetch_size` 로 최적화하자!

---

**컬렉션 조회 최적화, Flat 데이터 최적화**

컬렉션 조회 최적화: ToOne 관계들을 먼저 조회하고, 여기서 얻은 식별자 OrderId로 ToMany 관계인 `OrderItem` 을 Map, IN 을 통해 한꺼번에 조회하여 매칭 성능을 향상
Flat 데이터 최적화: 쿼리를 한번으로 줄이긴 했지만 조인으로 인해 중복 데이터가 추가되어 상황에 따라 성능이 떨어질수도 있다. 또한 플랫해주는 과정때문에 애플리케이션에서 작업이 많아지며, 페이징이 불가능하다(데이터 중복,ToMany 조인이므로).

둘다 쿼리를 최적화했지만 근본적인 성능의 개선을 쿼리의 횟수로 기준을 잡았다. 컬렉션 조회 최적화와 Flat 데이터 최적화 이전에 적용했던 `hibernate.default_batch_fetch_size` 가 나을수도 있겠다는 생각.

---

**엔티티 조회 방식 vs DTO 직접 조회 방식**

엔티티 조회 방식은 Fetch 조인이나, `hibernate.default_batch_fetch_size`,`@BatchSzie` 같이 코드를 거의 수정하지 않고, 옵션만 약간 변경해서
다양한 성능 최적화를 시도할 수 있다. 하지만 DTO를 직접 조회하는 방식은 성능을 최적화하거나 성능 최적화 방식을 변경할때 많은 코드를 변경해야한다.

DTO 조회방식은 단순히 쿼리가 줄어든다고 좋은 것이 아니다. N+1 문제나 페이징 문제에 대해서 알맞게 처리하기 위해서 V4~V6 방식을 사용한 것이지 항상 좋은 것은 아니다.

---

**OSIV(Open Session In View)**

> Open Session In View: 하이버네이트
Open EntityManager In View: JPA

`WARN 14628 --- [  restartedMain] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default.
Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning`

애플리케이션 시작 시점에 아래와 같은 WARN 이 뜬다. 그 이유는 JPA가 언제 데이터베이스 커넥션을 가져오고 DB에 다시 반환하는지에 연관이 있다. 
트랜잭션이 시작할때 영속성 컨텍스트가 데이터베이스 커넥션을 가져온다. 그런데 이때 OSIV 옵션이 켜저있으면 트랜잭션이 끝나도 커넥션을 반환하지 않고, 
끝까지 살려두면서 API의 경우는 API 결과가 사용자에게 반환될 때까지 혹은 템플릿 엔진이 렌더링할때까지 유지시킨다.

결국 고객의 모든 요청이 끝날때까지 영속성 컨텍스트는 데이터베이스 커넥션의 연결을 끊지 않는다. 이때문에 우리는 지연 로딩이 가능하다. 
하지만 이 전략에는 치명적인 장점이 있다. 그 이유는 너무 오랫동안 커넥션 리소스를 사용하기 때문에 실시간 트래픽이 중요한 애플리케이션에서는 커넥션이 모자른 경우가 생기고
**결국 커넥션이 말라버리는 장애가 발생한다.**

그래서 우리는 OSIV를 OFF시킬 수 있다. 이 경우 트랜잭션의 시작과 끝까지만 영속성 컨텍스트를 유지하며 
트랜잭션이 끝날 때 영속성 컨텍스트가 데이터베이스 커넥션을 반환하며 닫힌다. 따라서 커넥션 리소스를 낭비하지 않는 장점이 있다.

하지만 이 경우도 치명적인 단점이 있다. 바로 모든 지연 로딩을 트랜잭션 안에서 처리해야한다는 점이다. 따라서 view template에서는 지연로딩이 동작하지 않기 때문에
트랜잭션이 끝나기 전에 지연 로딩을 강제로 호출해야한다.