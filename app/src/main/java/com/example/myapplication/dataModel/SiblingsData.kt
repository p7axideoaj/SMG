package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class SiblingsData(
    @SerializedName("ServerName") val serverName: String,
    @SerializedName("CharacterName") val characterName: String,
    @SerializedName("CharacterLevel") val characterLevel: Int,
    @SerializedName("CharacterClassName") val characterClassName: String,
    @SerializedName("ItemAvgLevel") val itemAvgLevel: String,
    @SerializedName("ItemMaxLevel") val ItemMaxLevel: String,
)
