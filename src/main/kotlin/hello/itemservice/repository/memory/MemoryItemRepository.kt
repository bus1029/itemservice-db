package hello.itemservice.repository.memory

import hello.itemservice.domain.Item
import hello.itemservice.repository.ItemRepository
import hello.itemservice.repository.ItemSearchCondition
import hello.itemservice.repository.ItemUpdateDto
import org.springframework.stereotype.Repository
import org.springframework.util.ObjectUtils
import java.util.concurrent.atomic.AtomicLong

@Repository
class MemoryItemRepository : ItemRepository {
  companion object {
    private val store = mutableMapOf<Long, Item>()
    private val sequence = AtomicLong()
  }

  override fun save(item: Item): Item {
    item.id = sequence.addAndGet(1)
    store[item.id!!] = item
    return item
  }

  override fun update(itemId: Long, updateParam: ItemUpdateDto) {
    val findItem = findById(itemId)
    findItem?.let {
      findItem.itemName = updateParam.itemName
      findItem.price = updateParam.price
      findItem.quantity = updateParam.quantity
    } ?: throw NoSuchElementException("Cannot find item by $itemId")
  }

  override fun findById(id: Long): Item? {
    return store[id]
  }

  override fun findAll(cond: ItemSearchCondition): List<Item> {
    val itemName = cond.itemName
    val maxPrice = cond.maxPrice ?: Int.MAX_VALUE
    return filterStore(itemName, maxPrice)
  }

  private fun filterStore(
    itemName: String?,
    maxPrice: Int
  ): List<Item> {
    if (ObjectUtils.isEmpty(itemName)) {
      return filterStoreToMaxPrice(maxPrice)
    }

    return filterStoreToMaxPrice(maxPrice)
      .filter {
        it.itemName.contains(itemName!!)
      }
  }

  private fun filterStoreToMaxPrice(maxPrice: Int): List<Item> {
    return store.values.filter {
      it.price <= maxPrice
    }
  }

  fun clearStore() {
    store.clear()
  }
}