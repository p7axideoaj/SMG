package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName
import org.json.JSONArray

data class charterProfile(
    @SerializedName("CharacterImage") val CharacterImage: String?,
    @SerializedName("ExpeditionLevel") val ExpeditionLevel: Int?,
    @SerializedName("PvpGradeName") val PvpGradeName: String?,
    @SerializedName("TownLevel") val TownLevel: Int?,
    @SerializedName("TownName") val TownName: String?,
    @SerializedName("Title") val Title: String?,
    @SerializedName("GuildMemberGrade") val GuildMemberGrade: String?,
    @SerializedName("GuildName") val GuildName: String?,
    @SerializedName("UsingSkillPoint") val UsingSkillPoint: Int,
    @SerializedName("TotalSkillPoint") val TotalSkillPoint: Int,
    @SerializedName("Stats") val Stats: List<StatModel>,
    @SerializedName("Tendencies") val Tendencies: List<TendenciesModel>,
    @SerializedName("ServerName") val ServerName: String?,
    @SerializedName("CharacterName") val CharacterName: String,
    @SerializedName("CharacterLevel") val CharacterLevel: Int,
    @SerializedName("CharacterClassName") val CharacterClassName: String,
    @SerializedName("ItemAvgLevel") val ItemAvgLevel: String,
    @SerializedName("ItemMaxLevel") val ItemMaxLevel: String,
)
data class StatModel(
    @SerializedName("Type") val type: String,
    @SerializedName("Value") val value: String,
    @SerializedName("Tooltip") val tooltip: List<String>
)
data class TendenciesModel(
    @SerializedName("Type") val type: String,
    @SerializedName("Point") val Point: Int,
    @SerializedName("MaxPoint") val MaxPoint: Int
)