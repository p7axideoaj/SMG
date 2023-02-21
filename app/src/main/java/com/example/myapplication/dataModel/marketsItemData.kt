package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class marketsItemData(
    @SerializedName("Name") val name: String?,
    @SerializedName("TradeRemainCount") val tradeRemainCount: Int?,
    @SerializedName("BundleCount") val bundleCount: Int?,
    @SerializedName("Stats") val stats: List<ItemStats?>,
    @SerializedName("ToolTip") val toolTip: String?,
    )
data class ItemStats(
    @SerializedName("Date") val date: String?,
    @SerializedName("AvgPrice") val avgPrice: Int?,
    @SerializedName("TradeCount") val tradeCount: Int?,
)
