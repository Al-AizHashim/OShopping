package com.yemen.oshopping.model

data class Rating(
    var rating_id: Int?=null,
    var product_id: Int,
    var user_id: Int,
    var rating:Int
)