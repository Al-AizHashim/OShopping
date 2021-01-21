package com.yemen.oshopping.model

data class ActivityItem(
    var activityId: Int,
    var userId: Int,
    var productId: Int,
    var quantity: Int,
    var totalPrice: Double,
    var activityType: String
)