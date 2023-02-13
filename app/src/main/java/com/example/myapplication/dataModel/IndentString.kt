package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class IndentString(
    @SerializedName("contentStr") val content: List<Contents>,
    @SerializedName("topStr") val topStr: String,
)
