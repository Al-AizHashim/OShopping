package com.yemen.oshopping.api

import com.google.gson.annotations.SerializedName

import com.yemen.oshopping.model.User

data class SingleUserResponse (
    @SerializedName("user")
    var user: User
)