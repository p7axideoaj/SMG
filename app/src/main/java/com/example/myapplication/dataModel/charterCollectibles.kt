package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName

data class charterCollectibles(
    @SerializedName("Type") val Type: String?,
    @SerializedName("Icon") val Icon: String?,
    @SerializedName("Point") val Point: Int?,
    @SerializedName("MaxPoint") val MaxPoint: Int?,
    @SerializedName("CollectiblePoints") val CollectiblePoints: List<Map<String,*>>,

)
