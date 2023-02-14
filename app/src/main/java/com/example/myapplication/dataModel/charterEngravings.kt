package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class charterEngravings(
    @SerializedName("Engravings") val Engravings: List<engravings>?,
    @SerializedName("Effects") val Effects: List<EngravingsEffects>?,
)
data class engravings(
    @SerializedName("Slot") val slot: Int,
    @SerializedName("Name") val name: String,
    @SerializedName("Icon") val icon: String,
    @SerializedName("Tooltip") val Tooltip: String?,
)
data class EngravingsEffects(
    @SerializedName("Name") val name: String,
    @SerializedName("Description") val description: String,
)