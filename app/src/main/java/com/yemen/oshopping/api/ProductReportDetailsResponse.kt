package com.yemen.oshopping.api

import com.google.gson.annotations.SerializedName

import com.yemen.oshopping.model.ProductReportDetailsF

data class ProductReportDetailsResponse (
    @SerializedName("reports_details")
    var productReportItem: List<ProductReportDetailsF>

)