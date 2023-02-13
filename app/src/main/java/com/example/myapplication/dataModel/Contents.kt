package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class Contents(
    @SerializedName("contentStr") val contentStr: String?,
)
