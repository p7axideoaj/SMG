package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class marketsOption(
    @SerializedName("PageNo") val pageNo: Int?,
    @SerializedName("PageSize") val pageSize: Int?,
    @SerializedName("TotalCount") val totalCount: Int?,
    @SerializedName("Items") val items: List<OptionData?>,
)
data class OptionData(
    @SerializedName("Id") val id: Int?,
    @SerializedName("Name") val name: String?,
    @SerializedName("Grade") val grade: String?,
    @SerializedName("Icon") val icon: String?,
    @SerializedName("BundleCount") val bundleCount: Int?,
    @SerializedName("TradeRemainCount") val tradeRemainCount: Int?,
    @SerializedName("YDayAvgPrice") val yDayAvgPrice: Double?,
    @SerializedName("RecentPrice") val recentPrice: Int?,
    @SerializedName("CurrentMinPrice") val currentMinPrice: Int?,
)
