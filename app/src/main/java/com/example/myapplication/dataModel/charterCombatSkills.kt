package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class charterCombatSkills(
    @SerializedName("Name") val Name: String?,
    @SerializedName("Icon") val Icon: String?,
    @SerializedName("Level") val Level: Int?,
    @SerializedName("Type") val Type: String?,
    @SerializedName("IsAwakening") val IsAwakening: Boolean?,
    @SerializedName("Tripods") val Tripods: List<Map<String, *>>?,
    @SerializedName("Rune") val Rune: Map<String, *>?,
)
