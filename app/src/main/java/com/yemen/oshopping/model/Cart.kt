package com.yemen.oshopping.model

 data class Cart(
     var cart_id:Int?=null,
     var fk_product_id:Int,
     var fk_user_id:Int,
     var cart_statuse:Int,
     var product_name: String,
     var yrial_price: Double,
     var dollar_price: Double,
     var vendor_id:Int,
     var cat_id:Int,
     var product_details: String?,
     var product_img: String?,
     var product_date: String?=null,
     var product_quantity: Int,
     var product_discount: Int,
     var color:String ="black"

 )


