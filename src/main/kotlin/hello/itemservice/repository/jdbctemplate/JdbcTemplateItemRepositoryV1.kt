package hello.itemservice.repository.jdbctemplate

import hello.itemservice.domain.Item
import hello.itemservice.repository.ItemRepository
import hello.itemservice.repository.ItemSearchCondition
import hello.itemservice.repository.ItemUpdateDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils
import java.sql.Connection
import javax.sql.DataSource

@Repository
class JdbcTemplateItemRepositoryV1() : ItemRepository {
  private val log: Logger = LoggerFactory.getLogger(JdbcTemplateItemRepositoryV1::class.java)
  private var template: JdbcTemplate? = null

  constructor(dataSource: DataSource) : this() {
    this.template = JdbcTemplate(dataSource)
  }

  override fun save(item: Item): Item {
    val sql = "insert into item(item_name, price, quantity) values (?,?,?)"
    val keyHolder = GeneratedKeyHolder()
    template!!.update({ conn: Connection ->
      // 자동 증가 키
      val ps = conn.prepareStatement(sql, arrayOf("id"))
      ps.setString(1, item.itemName)
      ps.setInt(2, item.price)
      ps.setInt(3, item.quantity)
      ps
    }, keyHolder)

    val key = keyHolder.key!!.toLong()
    item.id = key
    return item
  }

  override fun update(itemId: Long, updateParam: ItemUpdateDto) {
    val sql = "update item set item_name=?, price=?, quantity=? where id=?"
    template!!.update(sql,
      updateParam.itemName,
      updateParam.price,
      updateParam.quantity,
      itemId)
  }

  override fun findById(id: Long): Item? {
    val sql = "select id, item_name, price, quantity from item where id = ?"
    return try {
      template!!.queryForObject(sql, itemRowMapper(), id)
    } catch (e: EmptyResultDataAccessException) {
      null
    }
  }

  private fun itemRowMapper(): RowMapper<Item> {
    return RowMapper() { rs, _ ->
      val item = Item()
      item.id = rs.getLong("id")
      item.itemName = rs.getString("item_name")
      item.price = rs.getInt("price")
      item.quantity = rs.getInt("quantity")
      item
    }
  }

  override fun findAll(cond: ItemSearchCondition): List<Item> {
    var sql = "select id, item_name, price, quantity from item"
    val param = ArrayList<Any?>()
    sql = appendCondition(sql, cond, param)
    return template!!.query(sql, itemRowMapper(), *param.toTypedArray())
  }

  private fun appendCondition(sql: String, cond: ItemSearchCondition, param: ArrayList<Any?>): String {
    val itemName = cond.itemName
    val maxPrice = cond.maxPrice
    var sqlWithCondition = sql

    if (StringUtils.hasText(itemName) || maxPrice != null) {
      sqlWithCondition = sqlWithCondition.plus(" where")
    }

    var andFlag = false
    if (StringUtils.hasText(itemName)) {
      sqlWithCondition = sqlWithCondition.plus(" item_name like concat('%',?,'%')")
      param.add(itemName!!)
      andFlag = true
    }

    if (maxPrice != null) {
      if (andFlag) {
        sqlWithCondition = sqlWithCondition.plus(" and")
      }
      sqlWithCondition = sqlWithCondition.plus(" price <= ?")
      param.add(maxPrice)
    }

    log.info("sql={}", sqlWithCondition)
    return sqlWithCondition
  }
}