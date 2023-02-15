package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data  class NewsNotiData(
    @SerializedName("Title") val title: String,
    @SerializedName("Date") val Date: String,
    @SerializedName("Link") val link: String,
    @SerializedName("Type") val Type: String,
)