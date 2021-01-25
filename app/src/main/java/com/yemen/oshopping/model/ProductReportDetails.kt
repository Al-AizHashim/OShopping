package com.yemen.oshopping.model

data class ProductReportDetails(
    var product_r_d_id :Int? = null,
    var product_id: Int,
    var product_r_id: Int,
    var sender_id : Int,
    var created_at: String?=null
)