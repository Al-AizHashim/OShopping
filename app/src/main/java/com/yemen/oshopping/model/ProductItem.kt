package com.yemen.oshopping.model

import java.util.*


data class ProductItem(
    var product_id: Int,
    var product_name: String,
    var yrial_price: Double,
    var dollar_price: Double,
    var vendor_id:Int,
    var cat_id:Int,
    var product_details: String,
    var product_img: String,
    var product_date: String,
    var product_quantity: Int,
    var product_discount: Int,
    var rating_average:Float,
    var number_of_ratings:Int,
    var color:String ="black",
    var hide:Int =0,
    var number_of_reports:Int
)