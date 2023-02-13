package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class charterCombatSkills(
    @SerializedName("Name") val name: String?,
    @SerializedName("Icon") val icon: String?,
    @SerializedName("Level") val level: Int?,
    @SerializedName("Type") val type: String?,
    @SerializedName("IsAwakening") val isAwakening: Boolean?,
    @SerializedName("Tripods") val tripods: List<Map<String, *>>?,
    @SerializedName("Rune") val rune: Map<String, *>?,
    @SerializedName("Tooltip") val tooltip: String?,
)

data class Tripods(
    @SerializedName("Tier") val tier: Int?,
    @SerializedName("Slot") val slot: Int?,
    @SerializedName("Name") val name: String?,
    @SerializedName("Icon") val icon: String?,
    @SerializedName("Level") val level: Int?,
    @SerializedName("IsSelected") val isSelected: Int?,
    @SerializedName("Tooltip") val tooltip: String?,
)