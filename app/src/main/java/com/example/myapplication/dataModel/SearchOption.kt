package com.example.myapplication.dataModel

import com.google.gson.annotations.SerializedName
public class SearchOption {
    @SerializedName("ItemLevelMin")
    private val itemLevelMin:Int? = null
    @SerializedName("ItemLevelMax")
    private val itemLevelMax:Int? = null
    @SerializedName("ItemGradeQuality")
    private val itemGradeQuality:Int? = null
    @SerializedName("SkillOptions")
    private val skillOptions:List<findOption>? = null
    @SerializedName("EtcOptions")
    private val etcOptions:List<findOption>? = null
    @SerializedName("Sort")
    private val sort:String = "BIDSTART_PRICE"
    @SerializedName("CategoryCode")
    private val categoryCode:Int = 0
    @SerializedName("CharacterClass")
    private val characterClass:String? = null
    @SerializedName("itemTier")
    private val itemTier:Int? = null
    @SerializedName("ItemGrade")
    private val itemGrade:String? = null
    @SerializedName("ItemName")
    private val itemName:String? = null
    @SerializedName("PageNo")
    private val pageNo:Int = 0
    @SerializedName("SortCondition")
    private val sortCondition:String = "ASC"
}