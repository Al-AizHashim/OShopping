package com.yemen.oshopping.model

import com.google.gson.annotations.SerializedName

data class ActivityItem(
    @SerializedName("activity_id")
    var activityId: Int,
    @SerializedName("fk_user_id")
    var userId: Int,
    @SerializedName("fk_product_id")
    var productId: Int,
    @SerializedName("quantity")
    var quantity: Int,
    @SerializedName("total_price")
    var totalPrice: Double,
    @SerializedName("activity_type")
    var activityType: String
)
