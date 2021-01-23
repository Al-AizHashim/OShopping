package com.yemen.oshopping.model

data class ReportDetails(
    var report_type: String? = null,
    var sender_name: String,
    var against: String,
    var created_at: String
)