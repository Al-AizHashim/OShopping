package com.yemen.oshopping.api

import com.google.gson.annotations.SerializedName
import com.yemen.oshopping.model.Report

data class ReportResponse  (
    @SerializedName("ListOfReports")
    var reportItem: List<Report>
)