package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class marketsData(
    @SerializedName("Categories") val Categories: List<Map<String, *>>,
    @SerializedName("ItemGrades") val ItemGrades: List<String?>?,
    @SerializedName("ItemTiers") val ItemTiers: List<Int?>?,
    @SerializedName("Classes") val Classes: List<String?>?,
)
