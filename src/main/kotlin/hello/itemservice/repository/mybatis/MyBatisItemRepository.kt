package hello.itemservice.repository.mybatis

import hello.itemservice.domain.Item
import hello.itemservice.repository.ItemRepository
import hello.itemservice.repository.ItemSearchCondition
import hello.itemservice.repository.ItemUpdateDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class MyBatisItemRepository(
  private val itemMapper: ItemMapper
) : ItemRepository {
  private val log: Logger = LoggerFactory.getLogger(this::class.java)

  override fun save(item: Item): Item {
    log.info("itemMapper class={}", itemMapper.javaClass)
    itemMapper.save(item)
    return item
  }

  override fun update(itemId: Long, updateParam: ItemUpdateDto) {
    itemMapper.update(itemId, updateParam)
  }

  override fun findById(id: Long): Item? {
    return itemMapper.findById(id)
  }

  override fun findAll(cond: ItemSearchCondition): List<Item> {
    return itemMapper.findAll(cond)
  }
}