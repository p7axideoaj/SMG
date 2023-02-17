package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class CalendarData(
    @SerializedName("CategoryName") val categoryName: String?,
    @SerializedName("ContentsName") val contentsName: String?,
    @SerializedName("ContentsIcon") val contentsIcon: String?,
    @SerializedName("MinItemLevel") val minItemLevel: Int?,
    @SerializedName("StartTimes") val startTimes: List<String?>,
    @SerializedName("Location") val location: String?,
    @SerializedName("RewardItems") val rewardItems: List<RewardList>,
)
