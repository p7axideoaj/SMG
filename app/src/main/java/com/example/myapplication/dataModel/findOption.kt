package com.example.myapplication.dataModel

import retrofit2.http.Field

data class findOption(
     var firstOption: Int?,
     var secondOption: Int?,
     var minValue: Int?,
     var maxValue: Int?,
) {
     companion object {
          fun toMap(data: findOption): Map<String, Int?> {
               return mapOf(
                    "FirstOption" to data.firstOption,
                    "SecondOption" to data.secondOption,
                    "MinValue" to data.minValue,
                    "MaxValue" to data.maxValue
               )
          }
     }
}
