package com.yemen.oshopping.api

import com.google.gson.annotations.SerializedName
import com.yemen.oshopping.model.ReportDetails


class ReportDetailsResponce (
    @SerializedName("reports_details")
    var reportDetailsItem: List<ReportDetails>
)