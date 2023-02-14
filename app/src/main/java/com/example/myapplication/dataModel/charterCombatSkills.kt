package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class charterCombatSkills(
    @SerializedName("Name") val name: String?,
    @SerializedName("Icon") val icon: String?,
    @SerializedName("Level") val level: Int?,
    @SerializedName("Type") val type: String?,
    @SerializedName("IsAwakening") val isAwakening: Boolean?,
    @SerializedName("Tripods") val tripods: List<Tripods>,
    @SerializedName("Rune") val rune: Rune?,
    @SerializedName("Tooltip") val tooltip: String?,
)

data class Tripods(
    @SerializedName("Tier") val tier: Int?,
    @SerializedName("Slot") val slot: Int?,
    @SerializedName("Name") val name: String?,
    @SerializedName("Icon") val icon: String?,
    @SerializedName("Level") val level: Int?,
    @SerializedName("IsSelected") val isSelected: Boolean?,
    @SerializedName("Tooltip") val tooltip: String?,
)
data class Rune (
    @SerializedName("Name") val name: String?,
    @SerializedName("Icon") val icon: String?,
    @SerializedName("Grade") val grade: String?,
    @SerializedName("Tooltip") val tooltip: String?,
)