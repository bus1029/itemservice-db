package hello.itemservice.config

import hello.itemservice.repository.ItemRepository
import hello.itemservice.repository.memory.MemoryItemRepository
import hello.itemservice.service.ItemService
import hello.itemservice.service.ItemServiceV1
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MemoryConfig {
  @Bean
  fun itemService(): ItemService {
    return ItemServiceV1(itemRepository())
  }

  @Bean
  fun itemRepository(): ItemRepository {
    return MemoryItemRepository()
  }
}