package com.yemen.oshopping.api

import com.google.gson.annotations.SerializedName
import com.yemen.oshopping.model.ActivityItem

data class ActivityResponse(
    @SerializedName("ListOfActivities")
    var activItem: List<ActivityItem>
)