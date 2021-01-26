package com.yemen.oshopping.model

import com.google.gson.annotations.SerializedName

data class ActivityItem(
    @SerializedName("fk_product_id")
    var productId: Int?,
    @SerializedName("product_name")
    var productName: String?,
    @SerializedName("yrial_price")
    var yrial_price: Double?,
    @SerializedName("dollar_price")
    var dollar_price: Double?,

    @SerializedName("quantity")
    var quantity: Int,
    @SerializedName("total_price")
    var totalPrice: Double,
    @SerializedName("activity_type")
    var activityType: String

)
