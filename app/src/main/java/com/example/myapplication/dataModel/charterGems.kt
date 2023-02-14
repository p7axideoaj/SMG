package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class charterGems(
    @SerializedName("Gems") val Gems: List<Gems?>,
    @SerializedName("Effects") val Effects: List<GemsEffects?>,
)
data class Gems(
    @SerializedName("Slot") val slot: Int,
    @SerializedName("Name") val name: String,
    @SerializedName("Icon") val icon: String,
    @SerializedName("Level") val level: Int,
    @SerializedName("Grade") val grade: String,
    @SerializedName("Tooltip") val tooltip: String,
)
data class GemsEffects(
    @SerializedName("GemSlot") val gemSlot: Int,
    @SerializedName("Name") val name: String,
    @SerializedName("Description") val description: String,
    @SerializedName("Icon") val icon: String,
    @SerializedName("Tooltip") val tooltip: String,
)