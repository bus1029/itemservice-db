package hello.itemservice.repository.mybatis

import hello.itemservice.domain.Item
import hello.itemservice.repository.ItemSearchCondition
import hello.itemservice.repository.ItemUpdateDto
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface ItemMapper {
  fun save(item: Item)
  fun update(@Param("id") id: Long, @Param("updateParam") updateParam: ItemUpdateDto)
  fun findById(id: Long): Item?
  fun findAll(itemSearch: ItemSearchCondition): List<Item>
}