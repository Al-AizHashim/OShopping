package com.yemen.oshopping.model

data class User(
    var user_id: Int?=null,
    val first_name: String?=null,
    val last_name: String?=null,
    val email: String?=null,
    val phone_number:String?=null,
    val details: String?=null,
    val address: String?=null,
    val vendor: Int=0, // ( 0 -> user ) ( 1 -> vendor)
    val block: Int =0, // ( 0 -> active ) ( 1 -> blocked)
    val admin: Int=0,
    val image: String?=null,
    val created_at: String?=null
)