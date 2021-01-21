package com.yemen.oshopping.model

 data class Cart(
     var cart_id:Int?=null,
     var fk_product_id:Int,
     var fk_user_id:Int,
     var cart_statuse:Int) {
}


