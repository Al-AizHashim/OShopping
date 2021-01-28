package com.yemen.oshopping.api

import com.google.gson.annotations.SerializedName

import com.yemen.oshopping.model.ProductReportsDetailsF

data class ProductReportsDetailsResponse (
    @SerializedName("ListOfReportsDetails")
    var productReportsItem: List<ProductReportsDetailsF>

)