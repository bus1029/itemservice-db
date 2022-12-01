package hello.itemservice.config

import hello.itemservice.repository.ItemRepository
import hello.itemservice.repository.mybatis.ItemMapper
import hello.itemservice.repository.mybatis.MyBatisItemRepository
import hello.itemservice.service.ItemService
import hello.itemservice.service.ItemServiceV1
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MyBatisConfig(
  private val itemMapper: ItemMapper
) {
  @Bean
  fun itemService(): ItemService {
    return ItemServiceV1(itemRepository())
  }

  @Bean
  fun itemRepository(): ItemRepository {
    return MyBatisItemRepository(itemMapper)
  }
}