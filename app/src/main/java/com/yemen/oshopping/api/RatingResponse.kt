package com.yemen.oshopping.api

import com.google.gson.annotations.SerializedName
import com.yemen.oshopping.model.Rating



data class RatingResponse (
    @SerializedName("listOfReports")
    var rating: List<Rating>
)