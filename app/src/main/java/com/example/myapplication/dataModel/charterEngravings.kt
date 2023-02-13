package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class charterEngravings(
    @SerializedName("Engravings") val Engravings: List<Map<String, *>>,
    @SerializedName("Effects") val Effects: List<Map<String, *>>,
)
