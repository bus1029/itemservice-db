package hello.itemservice.service

import hello.itemservice.domain.Item
import hello.itemservice.repository.ItemSearchCondition
import hello.itemservice.repository.ItemUpdateDto

interface ItemService {
  fun save(item: Item): Item
  fun update(itemId: Long, updateParam: ItemUpdateDto)
  fun findById(id: Long): Item?
  fun findItems(itemSearch: ItemSearchCondition): List<Item>
}