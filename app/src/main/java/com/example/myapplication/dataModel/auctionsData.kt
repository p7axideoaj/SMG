package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class auctionsData(
    @SerializedName("MaxItemLevel") val MaxItemLevel: Int,
    @SerializedName("ItemGradeQualities") val ItemGradeQualities: List<Int?>?,
    @SerializedName("SkillOptions") val SkillOptions: List<Map<String,*>>?,
    @SerializedName("Categories") val Categories: List<Map<String,*>>?,
    @SerializedName("ItemGrades") val ItemGrades: List<String?>?,
    @SerializedName("ItemTiers") val ItemTiers: List<Int?>?,
    @SerializedName("Classes") val Classes: List<String?>?,
)
