package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class ItemTitle(
    @SerializedName("leftStr0") val name: String,
    @SerializedName("leftStr2") val option: String,
    @SerializedName("qualityValue") val Value: Int,
    @SerializedName("slotData") val slot: String,
)
