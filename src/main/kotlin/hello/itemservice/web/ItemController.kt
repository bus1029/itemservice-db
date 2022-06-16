package hello.itemservice.web

import hello.itemservice.domain.Item
import hello.itemservice.repository.ItemSearchCondition
import hello.itemservice.repository.ItemUpdateDto
import hello.itemservice.service.ItemService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/items")
class ItemController(
  private val itemService: ItemService
) {
  private val log: Logger = LoggerFactory.getLogger(ItemController::class.java)

  @GetMapping
  fun items(@ModelAttribute("itemSearch") itemSearch: ItemSearchCondition, model: Model): String {
    val items = itemService.findItems(itemSearch)
    model.addAttribute("items", items)
    return "items"
  }

  @GetMapping("/{itemId}")
  fun item(@PathVariable itemId: Long, model: Model): String {
    val item = itemService.findById(itemId)
    item?.let {
      model.addAttribute("item", item)
    } ?: run {
      log.error("Cannot find item by id[{}], set empty Item", itemId)
      model.addAttribute("item", Item())
    }
    return "item"
  }

  @GetMapping("/add")
  fun addForm(): String {
    return "addForm"
  }

  @PostMapping("/add")
  fun addItem(@ModelAttribute item: Item, redirectAttribute: RedirectAttributes): String {
    val savedItem = itemService.save(item)
    redirectAttribute.addAttribute("itemId", savedItem.id)
    redirectAttribute.addAttribute("status", true)
    return "redirect:/items/{itemId}"
  }

  @GetMapping("/{itemId}/edit")
  fun editForm(@PathVariable itemId: Long, model: Model): String {
    val item = itemService.findById(itemId)
    item?.let {
      model.addAttribute("item", item)
    } ?: run {
      log.error("Cannot find item by id[{}], set empty Item", itemId)
      model.addAttribute("item", Item())
    }
    return "editForm"
  }

  @PostMapping("/{itemId}/edit")
  fun edit(@PathVariable itemId: Long, @ModelAttribute updateParam: ItemUpdateDto): String {
    itemService.update(itemId, updateParam)
    return "redirect:/items/{itemId}"
  }
}