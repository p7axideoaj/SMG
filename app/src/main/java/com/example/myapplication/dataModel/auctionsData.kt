package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class auctionsData(
    @SerializedName("MaxItemLevel") val maxItemLevel: Int,
    @SerializedName("ItemGradeQualities") val itemGradeQualities: List<Int>?,
    @SerializedName("SkillOptions") val skillOptions: List<auctionSkillsOptions>?,
    @SerializedName("EtcOptions") val etcOptions: List<auctionEtcOptions>?,
    @SerializedName("Categories") val categories: List<marketsOptions>?,
    @SerializedName("ItemGrades") val itemGrades: List<String?>?,
    @SerializedName("ItemTiers") val itemTiers: List<Int?>?,
    @SerializedName("Classes") val classes: List<String?>?,
)

data class auctionSkillsOptions(
    @SerializedName("Value") val value: Int?,
    @SerializedName("Class") val className: String?,
    @SerializedName("Text") val text: String?,
    @SerializedName("IsSkillGroup") val isSkillGroup: Boolean?,
    @SerializedName("Tripods") val tripods: List<tripodsOptions>,
)
data class tripodsOptions(
    @SerializedName("Value") val value: Int?,
    @SerializedName("Text") val text: String?,
    @SerializedName("IsGem") val IsGem: Boolean?,
)
data class auctionEtcOptions(
    @SerializedName("Value") val value: Int?,
    @SerializedName("Text") val text: String?,
    @SerializedName("EtcSubs") val etcSubs: List<etcSubsOptions>,
)
data class etcSubsOptions(
    @SerializedName("Value") val value: Int?,
    @SerializedName("Text") val text: String?,
    @SerializedName("Class") val className: String?,
)