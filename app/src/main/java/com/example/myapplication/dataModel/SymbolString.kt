package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class SymbolString(
    @SerializedName("contentStr") val content: String,
    @SerializedName("titleStr") val title: String,
)
