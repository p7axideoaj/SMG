package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class guildData(
    @SerializedName("Rank") val rank: Int?,
    @SerializedName("GuildName") val guildName: String?,
    @SerializedName("GuildMessage") val guildMessage: String?,
    @SerializedName("MasterName") val MasterName: String?,
    @SerializedName("Rating") val rating: Int?,
    @SerializedName("MemberCount") val memberCount: Int?,
    @SerializedName("MaxMemberCount") val maxMemberCount: Int?,
    @SerializedName("UpdatedDate") val updatedDate: String?,
)
