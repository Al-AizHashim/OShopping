package com.yemen.oshopping.api

import com.google.gson.annotations.SerializedName
import com.yemen.oshopping.model.ReportDetails

import com.yemen.oshopping.model.ReportsDetails

class ReportsDetailsResponce (
    @SerializedName("ListOfReportsDetails")
    var reportDetailsItem: List<ReportsDetails>
)