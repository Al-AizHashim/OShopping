package com.yemen.oshopping.api

import com.google.gson.annotations.SerializedName
import com.yemen.oshopping.model.User


data class UserResponse (
    @SerializedName("ListOfUsers")
    var userItem: List<User>
)