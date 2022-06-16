package hello.itemservice.repository

class ItemUpdateDto(
  var itemName: String,
  var price: Int,
  var quantity: Int
) {
}