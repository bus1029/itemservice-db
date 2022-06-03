package hello.itemservice

import hello.itemservice.domain.Item
import hello.itemservice.repository.ItemRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener

class TestDataInit(
  private val itemRepository: ItemRepository
) {
  private val log: Logger = LoggerFactory.getLogger(TestDataInit::class.java)

  @EventListener(ApplicationReadyEvent::class)
  fun initData() {
    log.info("Test data init.")
    itemRepository.save(Item("itemA", 10000, 10))
    itemRepository.save(Item("itemB", 20000, 20))
  }
}
