package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class charterAvatars(
    @SerializedName("Type") val Type: String?,
    @SerializedName("Name") val Name: String?,
    @SerializedName("Icon") val Icon: String?,
    @SerializedName("Grade") val Grade: String?,
    @SerializedName("IsSet") val IsSet: Boolean?,
    @SerializedName("IsInner") val IsInner: Boolean?,
    @SerializedName("Tooltip") val Tooltip: String?,
)

