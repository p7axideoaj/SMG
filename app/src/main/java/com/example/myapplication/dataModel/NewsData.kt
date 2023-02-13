package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName
import java.util.*

data class newsData(
    @SerializedName("Title") val title: String,
    @SerializedName("Thumbnail") val thumbnail: String,
    @SerializedName("Link") val link: String,
    @SerializedName("StartDate") val startDate: String,
    @SerializedName("EndDate") val endDate: String,
    @SerializedName("RewardDate") val rewardDate: String?,
)
