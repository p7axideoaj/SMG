package com.example.myapplication.dataModel

import retrofit2.http.Field

data class findOption(
    @Field("FirstOption") var firstOption: Int?,
    @Field("SecondOption") var secondOption: Int?,
    @Field("MinValue") var minValue: Int?,
    @Field("MaxValue") var maxValue: Int?,
)
