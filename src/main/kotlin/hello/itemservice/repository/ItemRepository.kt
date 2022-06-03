package hello.itemservice.repository

import hello.itemservice.domain.Item

interface ItemRepository {
  fun save(item: Item): Item
  fun update(itemId: Long, updateParam: ItemUpdateDto)
  fun findById(id: Long): Item?
  fun findAll(cond: ItemSearchCondition): List<Item>
}