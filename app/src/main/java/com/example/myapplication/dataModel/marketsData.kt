package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class marketsData(
    @SerializedName("Categories") val Categories: List<marketsOptions>,
    @SerializedName("ItemGrades") val ItemGrades: List<String?>?,
    @SerializedName("ItemTiers") val ItemTiers: List<Int?>?,
    @SerializedName("Classes") val Classes: List<String?>?,
)
data class marketsOptions(
    @SerializedName("Subs") val subs: List<marketsOptionSubs>,
    @SerializedName("Code") val code: Int,
    @SerializedName("CodeName") val codeName: String,
)
data class marketsOptionSubs(
    @SerializedName("Code") val code: Int,
    @SerializedName("CodeName") val codeName: String,
)