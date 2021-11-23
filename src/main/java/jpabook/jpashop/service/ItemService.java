package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	@Transactional
	public void saveItem(Item item){
		itemRepository.save(item);
	}

	// 변경 감지 기능을 활용한 업데이트 : 영속성 컨텍스트에서 엔티티를 다시 조회하고, 트랜잭션 커밋 시점에 변경 감지(Dirty checking)이 동작하는 것을 이용
	@Transactional
	public void updateItem(Long itemId, String name, int price, int stockQuantity){
		Item findItem = itemRepository.findOne(itemId);
		findItem.setName(name);
		findItem.setPrice(price);
		findItem.setStockQuantity(stockQuantity);

		// itemRepository.save(findItem); : 호출할 필요가 없음, findItem은 영속 상태다.
		// updateItem이 끝나면 @Transactional 때문에 커밋이 되고 JPA가 flush를 통해 set한 내용들이 update 쿼리로 날아간다.
	}

	public List<Item> findItems(){
		return itemRepository.findAll();
	}

	public Item findOne(Long itemId){
		return itemRepository.findOne(itemId);
	}
}
