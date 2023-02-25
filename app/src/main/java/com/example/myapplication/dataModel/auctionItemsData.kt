package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class auctionItemsData(
    @SerializedName("PageNo") val pageNo: Int?,
    @SerializedName("PageSize") val pageSize: Int?,
    @SerializedName("TotalCount") val totalCount: Int?,
    @SerializedName("Items") val items: List<itemsList>,
)
data class itemsList(
    @SerializedName("Name") val name: String?,
    @SerializedName("Grade") val grade: String?,
    @SerializedName("Tier") val tier: Int?,
    @SerializedName("Level") val Level: Int?,
    @SerializedName("Icon") val icon: String?,
    @SerializedName("GradeQuality") val gradeQuality: Int?,
    @SerializedName("AuctionInfo") val auctionInfo: auctionInfoData?,
    @SerializedName("Options") val options: List<auctionInfoOptions>,
)
data class auctionInfoOptions(
    @SerializedName("Type") val type: String?,
    @SerializedName("OptionName") val optionName: String?,
    @SerializedName("OptionNameTripod") val optionNameTripod: String?,
    @SerializedName("Value") val value: Int?,
    @SerializedName("IsPenalty") val isPenalty: Boolean?,
    @SerializedName("ClassName") val className: String?,
)
data class auctionInfoData(
    @SerializedName("StartPrice") val startPrice: Int?,
    @SerializedName("BuyPrice") val buyPrice: Int?,
    @SerializedName("BidPrice") val bidPrice: Int?,
    @SerializedName("EndDate") val endDate: String?,
    @SerializedName("BidCount") val bidCount: Int?,
    @SerializedName("BidStartPrice") val bidStartPrice: Int?,
    @SerializedName("IsCompetitive") val isCompetitive: Boolean?,
    @SerializedName("TradeAllowCount") val tradeAllowCount: Int?,
    )
