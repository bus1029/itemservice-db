package hello.itemservice.domain

class Item() {
  var id: Long? = null
  var itemName: String = ""
  var price: Int = 0
  var quantity: Int = 0

  constructor(itemName: String, price: Int, quantity: Int): this() {
    this.itemName = itemName
    this.price = price
    this.quantity = quantity
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Item

    if (id != other.id) return false
    if (itemName != other.itemName) return false
    if (price != other.price) return false
    if (quantity != other.quantity) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id?.hashCode() ?: 0
    result = 31 * result + itemName.hashCode()
    result = 31 * result + price
    result = 31 * result + quantity
    return result
  }
}