package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class charterCards(
    @SerializedName("Cards") val Cards: List<Map<String, *>>,
    @SerializedName("Effects") val Effects: List<Map<String, *>>,
)
