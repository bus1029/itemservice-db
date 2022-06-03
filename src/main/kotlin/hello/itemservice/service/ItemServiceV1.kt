package hello.itemservice.service

import hello.itemservice.domain.Item
import hello.itemservice.repository.ItemRepository
import hello.itemservice.repository.ItemSearchCondition
import hello.itemservice.repository.ItemUpdateDto
import org.springframework.stereotype.Service

@Service
class ItemServiceV1(
  private val itemRepository: ItemRepository
) : ItemService {
  override fun save(item: Item): Item {
    return itemRepository.save(item)
  }

  override fun update(itemId: Long, updateParam: ItemUpdateDto) {
    itemRepository.update(itemId, updateParam)
  }

  override fun findById(id: Long): Item? {
    return itemRepository.findById(id)
  }

  override fun findItems(itemSearch: ItemSearchCondition): List<Item> {
    return itemRepository.findAll(itemSearch)
  }
}