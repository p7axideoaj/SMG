package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class GuardianRaidsData(
    @SerializedName("Raids") val raids: List<RaidsData>?,
    @SerializedName("RewardItems") val rewardItems: List<RewardData>?,
)
data class RaidsData(
    @SerializedName("Name") val name: String?,
    @SerializedName("Description") val description: String?,
    @SerializedName("MinCharacterLevel") val minCharacterLevel: Int?,
    @SerializedName("MinItemLevel") val minItemLevel: Int?,
    @SerializedName("RequiredClearRaid") val requiredClearRaid: String?,
    @SerializedName("StartTime") val startTime: String?,
    @SerializedName("EndTime") val endTime: String?,
    @SerializedName("Image") val image: String?,
)
data class RewardData(
    @SerializedName("ExpeditionItemLevel") val expeditionItemLevel: Int?,
    @SerializedName("Items") val items: List<RewardList>,
)