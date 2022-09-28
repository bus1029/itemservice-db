package hello.itemservice

import hello.itemservice.config.JdbcTemplateV3Config
import hello.itemservice.repository.ItemRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

//@Import(MemoryConfig::class)
//@Import(JdbcTemplateV1Config::class)
//@Import(JdbcTemplateV2Config::class)
@Import(JdbcTemplateV3Config::class)
@SpringBootApplication(scanBasePackages = ["hello.itemservice.web"])
class ItemServiceApplication {
  private val log: Logger = LoggerFactory.getLogger(this::class.java)
  @Bean
  @Profile("local")
  fun testDataInit(itemRepository: ItemRepository): TestDataInit {
    return TestDataInit(itemRepository)
  }

  @Bean
  @Profile("test")
  fun dataSource(): DataSource {
    log.info("메모리 데이터베이스 초기화")
    val dataSource = DriverManagerDataSource()
    dataSource.setDriverClassName("org.h2.Driver")
    dataSource.url = "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1"
    dataSource.username = "sa"
    dataSource.password = ""
    return dataSource
  }
}

fun main(args: Array<String>) {
  runApplication<ItemServiceApplication>(*args)
}