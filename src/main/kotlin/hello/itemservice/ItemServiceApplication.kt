package hello.itemservice

import hello.itemservice.config.JdbcTemplateV1Config
import hello.itemservice.config.MemoryConfig
import hello.itemservice.repository.ItemRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile

//@Import(MemoryConfig::class)
@Import(JdbcTemplateV1Config::class)
@SpringBootApplication(scanBasePackages = ["hello.itemservice.web"])
class ItemServiceApplication {
  @Bean
  @Profile("local")
  fun testDataInit(itemRepository: ItemRepository): TestDataInit {
    return TestDataInit(itemRepository)
  }
}

fun main(args: Array<String>) {
  runApplication<ItemServiceApplication>(*args)
}