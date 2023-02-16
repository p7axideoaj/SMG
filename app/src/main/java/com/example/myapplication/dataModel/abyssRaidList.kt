package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class abyssRaidList(
    @SerializedName("Name") val name: String?,
    @SerializedName("Description") val description: String?,
    @SerializedName("MinCharacterLevel") val minCharacterLevel: Int?,
    @SerializedName("MinItemLevel") val minItemLevel: Int?,
    @SerializedName("AreaName") val areaName: String?,
    @SerializedName("StartTime") val startTime: String?,
    @SerializedName("EndTime") val endTime: String?,
    @SerializedName("Image") val image: String?,
    @SerializedName("rewardItems") val rewardItems: List<RewardList>,
)
data class RewardList(
    @SerializedName("Name") val name: String?,
    @SerializedName("Icon") val icon: String?,
    @SerializedName("Grade") val grade: String?,
)