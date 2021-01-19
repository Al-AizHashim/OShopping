package com.yemen.oshopping.model

data class User(
    var user_id: Int?=null,
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone_number:String,
    val details: String,
    val address: String,
    val vendor: Int=0, // ( 0 -> user ) ( 1 -> vendor)
    val block: Int =0, // ( 0 -> active ) ( 1 -> blocked)
    val admin: Int=0,
    val image: String?=null,
    val created_at: String?=null
)