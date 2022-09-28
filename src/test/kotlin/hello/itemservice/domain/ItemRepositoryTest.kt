package hello.itemservice.domain

import hello.itemservice.repository.ItemRepository
import hello.itemservice.repository.ItemSearchCondition
import hello.itemservice.repository.ItemUpdateDto
import hello.itemservice.repository.memory.MemoryItemRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.DefaultTransactionDefinition

@SpringBootTest
@Transactional
class ItemRepositoryTest {
  @Autowired
  private lateinit var itemRepository: ItemRepository

  @AfterEach
  fun destroy() {
    if (itemRepository is MemoryItemRepository) {
      (itemRepository as MemoryItemRepository).clearStore()
    }
  }

  @Test
  fun save() {
    val item = Item("itemA", 10000, 10)
    val savedItem = itemRepository.save(item)
    val findItem = itemRepository.findById(item.id!!)
    assertThat(findItem).isNotNull
    assertThat(findItem).isEqualTo(savedItem)
  }

  @Test
  fun updateItem() {
    val item = Item("item1", 10000, 10)
    val savedItem = itemRepository.save(item)
    val itemId = savedItem.id

    val updateParam = ItemUpdateDto("item2", 20000, 30)
    itemRepository.update(itemId!!, updateParam)

    val findItem = itemRepository.findById(itemId)
    assertThat(findItem).isNotNull
    assertThat(findItem!!.itemName).isEqualTo(updateParam.itemName)
    assertThat(findItem.price).isEqualTo(updateParam.price)
    assertThat(findItem.quantity).isEqualTo(updateParam.quantity)
  }

  @Test
  fun findItems() {

    //given
    val item1 = Item("itemA-1", 10000, 10)
    val item2 = Item("itemA-2", 20000, 20)
    val item3 = Item("itemB-1", 30000, 30)

    itemRepository.save(item1)
    itemRepository.save(item2)
    itemRepository.save(item3)

    test(null, null, item1, item2, item3)
    test("itemA", null, item1, item2)
    test("temA", null, item1, item2)
    test("itemB", null, item3)
    test(null, 10000, item1)
    test("itemA", 10000, item1)
  }

  private fun test(itemName: String?, maxPrice: Int?, vararg items: Item) {
    val result = itemRepository.findAll(ItemSearchCondition(itemName, maxPrice))
    assertThat(result).containsExactly(*items)
  }
}