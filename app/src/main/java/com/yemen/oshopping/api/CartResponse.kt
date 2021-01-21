package com.yemen.oshopping.api

import com.google.gson.annotations.SerializedName
import com.yemen.oshopping.model.Cart

data class CartResponse(
    @SerializedName("Cart")
    var cartItem: List<Cart>) {
}