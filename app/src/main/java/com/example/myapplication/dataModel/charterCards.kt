package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class charterCards(
    @SerializedName("Cards") val Cards: List<Cards>?,
    @SerializedName("Effects") val Effects: List<CardsEffects>?,
)
data class Cards(
    @SerializedName("Slot") val slot: Int,
    @SerializedName("Name") val name: String,
    @SerializedName("Icon") val icon: String,
    @SerializedName("AwakeCount") val awakeCount: Int,
    @SerializedName("AwakeTotal") val awakeTotal: Int,
    @SerializedName("Grade") val grade: String,
    @SerializedName("Tooltip") val Tooltip: String,
)
data class CardsEffects(
    @SerializedName("Index") val index: Int,
    @SerializedName("CardSlots") val cardSlots: List<Int>,
    @SerializedName("Items") val items: List<TeamEffects>
)
data class TeamEffects(
    @SerializedName("Name") val name: String,
    @SerializedName("Description") val description: String,
)