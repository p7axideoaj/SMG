package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class ItemPartBox(
    @SerializedName("Element_000") val name: String,
    @SerializedName("Element_001") val option: String,
)
