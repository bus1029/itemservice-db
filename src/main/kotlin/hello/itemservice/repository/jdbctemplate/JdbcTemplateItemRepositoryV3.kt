package hello.itemservice.repository.jdbctemplate

import hello.itemservice.domain.Item
import hello.itemservice.repository.ItemRepository
import hello.itemservice.repository.ItemSearchCondition
import hello.itemservice.repository.ItemUpdateDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils
import javax.sql.DataSource

/**
 * SimpleJdbcInsert 사용
 */
@Repository
class JdbcTemplateItemRepositoryV3() : ItemRepository {
  private val log: Logger = LoggerFactory.getLogger(JdbcTemplateItemRepositoryV3::class.java)
  private var template: NamedParameterJdbcTemplate? = null
  private var jdbcInsert: SimpleJdbcInsert? = null

  constructor(dataSource: DataSource) : this() {
    this.template = NamedParameterJdbcTemplate(dataSource)
    this.jdbcInsert = SimpleJdbcInsert(dataSource)
      .withTableName("item")
      .usingGeneratedKeyColumns("id")
//      .usingColumns("item_name", "price", "quantity") // 생략 가능
  }

  override fun save(item: Item): Item {
    val param = BeanPropertySqlParameterSource(item)
    val key = jdbcInsert!!.executeAndReturnKey(param)
    item.id = key.toLong()
    return item
  }

  override fun update(itemId: Long, updateParam: ItemUpdateDto) {
    val sql = "update item set item_name=:itemName, price=:price, quantity=:quantity " +
            "where id=:id"
    val param = MapSqlParameterSource()
      .addValue("itemName", updateParam.itemName)
      .addValue("price", updateParam.price)
      .addValue("quantity", updateParam.quantity)
      .addValue("id", itemId)

    template!!.update(sql, param)
  }

  override fun findById(id: Long): Item? {
    val sql = "select id, item_name, price, quantity from item where id = :id"
    return try {
      val param = mapOf<String, Any>(Pair("id", id))
      template!!.queryForObject(sql, param, itemRowMapper())
    } catch (e: EmptyResultDataAccessException) {
      null
    }
  }

  private fun itemRowMapper(): RowMapper<Item> {
    return BeanPropertyRowMapper.newInstance(Item::class.java) // Camel 변환 지원
  }

  override fun findAll(cond: ItemSearchCondition): List<Item> {
    var sql = "select id, item_name, price, quantity from item"
    val param = BeanPropertySqlParameterSource(cond)
    sql = appendCondition(sql, cond)
    return template!!.query(sql, param, itemRowMapper())
  }

  private fun appendCondition(sql: String, cond: ItemSearchCondition): String {
    val itemName = cond.itemName
    val maxPrice = cond.maxPrice
    var sqlWithCondition = sql

    if (StringUtils.hasText(itemName) || maxPrice != null) {
      sqlWithCondition = sqlWithCondition.plus(" where")
    }

    var andFlag = false
    if (StringUtils.hasText(itemName)) {
      sqlWithCondition = sqlWithCondition.plus(" item_name like concat('%',:itemName,'%')")
      andFlag = true
    }

    if (maxPrice != null) {
      if (andFlag) {
        sqlWithCondition = sqlWithCondition.plus(" and")
      }
      sqlWithCondition = sqlWithCondition.plus(" price <= :maxPrice")
    }

    log.info("sql={}", sqlWithCondition)
    return sqlWithCondition
  }
}