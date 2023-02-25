package com.example.myapplication.Page

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.*
import com.example.myapplication.dataModel.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun actionPage(navController: NavHostController) {
    var itemName by remember {
        mutableStateOf("")
    }
    var itemTier by remember {
        mutableStateOf(0)
    }
    var itemGrade by remember {
        mutableStateOf("")
    }
    var characterClass by remember {
        mutableStateOf("")
    }
    var categoryCode by remember {
        mutableStateOf(0)
    }
    val scope = MainScope()
    var pageIndex by remember {
        mutableStateOf(0)
    }
    var categoryCodeText by remember {
        mutableStateOf("선택 사항 없음")
    }
    var itemTierText by remember {
        mutableStateOf(0)
    }
    var itemGradeText by remember {
        mutableStateOf("선택 사항 없음")
    }
    var characterClassText by remember {
        mutableStateOf("선택 사항 없음")
    }
    var categoryCodeExpanded by remember {
        mutableStateOf(false)
    }
    var itemTierExpanded by remember {
        mutableStateOf(false)
    }
    var itemGradeExpanded by remember {
        mutableStateOf(false)
    }
    var characterClassexpanded by remember {
        mutableStateOf(false)
    }
    val auctionsDataList = remember {
        mutableStateListOf<auctionsData?>()
    }
    //skills
    var auctionsDataExpanded by remember {
        mutableStateOf(false)
    }
    var dataList = findOption(0,0,0,0)
    var auctionsDataText by remember {
        mutableStateOf("선택 사항 없음")
    }
    var actionDataIndex by remember {
        mutableStateOf(0)
    }
    var auctionsDataDetailExpanded by remember {
        mutableStateOf(false)
    }
    var auctionsDetailDataText by remember {
        mutableStateOf("선택 사항 없음")
    }
    var skillMinLevel by remember {
        mutableStateOf(0)
    }
    var skillMinLevelText by remember {
        mutableStateOf("")
    }
    var skillsMaxLevel by remember {
        mutableStateOf(0)
    }
    var skillsMaxLevelText by remember {
        mutableStateOf("")
    }
    //etc
    var auctionsEtcDataExpanded by remember {
        mutableStateOf(false)
    }
    var etcDataList = findOption(0,0,0,0)
    var auctionsDataEtcText by remember {
        mutableStateOf("선택 사항 없음")
    }
    var actionDataEtcIndex by remember {
        mutableStateOf(0)
    }
    var auctionsDataEtcDetailExpanded by remember {
        mutableStateOf(false)
    }
    var auctionsDetailEtcDataText by remember {
        mutableStateOf("선택 사항 없음")
    }
    var etcMinLevel by remember {
        mutableStateOf(0)
    }
    var etcMinLevelText by remember {
        mutableStateOf("")
    }
    var etcMaxLevel by remember {
        mutableStateOf(0)
    }
    var etcMaxLevelText by remember {
        mutableStateOf("")
    }
    var itemGradeQualities by remember {
        mutableStateOf(0)
    }
    var itemGradeQualitiesExpanded by remember {
        mutableStateOf(false)
    }
    var itemMaxLevel by remember {
        mutableStateOf(0)
    }
    var itemMaxLevelText by remember {
        mutableStateOf("")
    }
    var itemMinLevel by remember {
        mutableStateOf(0)
    }
    var itemMinLevelText by remember {
        mutableStateOf("")
    }
    val itemDataList = remember {
        mutableStateListOf<auctionItemsData?>()
    }
    DisposableEffect(0){
        scope.launch {
            while (true) {
                getJSONAuctions(auctionsDataList)
                getJSONAuctionsData(itemDataList, itemLevelMin = if(itemTier == 0) null else itemMinLevel,
                    itemLevelMax = if(itemTier == 0) null else itemMaxLevel, itemGradeQuality = if(itemTier == 0) null else itemGradeQualities,
                    skillOptions = listOf(dataList), etcOptions = listOf(etcDataList),
                    categoryCode = categoryCode, characterClass = if(characterClass == "선택 사항 없음") null else characterClass,
                    itemTier = if(itemTier == 0) null else itemTier, itemGrade = if(itemGrade == "선택 사항 없음") null else itemGrade, itemName = if(itemName == "") null else itemName
                    )
                delay(2500)
            }
        }
        onDispose {
            scope.cancel()
        }
    }
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "경매장") },
            navigationIcon = {
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(Icons.Default.ArrowBack, "backIcon")
                }
            },
            backgroundColor = Color.White,
            elevation = 0.dp)
    }) {
        LazyColumn(Modifier.padding(16.dp)) {
            item {
                LazyRow() {
                    auctionsDataList.forEach {
                        itemsIndexed(it!!.categories!!) { idx, Categorie ->
                            TextButton(onClick = { pageIndex = idx}) {
                                Text(text = Categorie.codeName)
                            }
                        }
                    }
                }
            }
            item {
                Column() {
                    Box() {
                        TextButton(onClick = {
                            categoryCodeExpanded = true
                        }) {
                            Text("${if(!auctionsDataList.isNullOrEmpty()) auctionsDataList.map { it!!.categories!![pageIndex].codeName }.first() else ""} : $categoryCodeText")
                        }
                        DropdownMenu(expanded = categoryCodeExpanded, onDismissRequest = { categoryCodeExpanded = false }) {
                            auctionsDataList.forEach { option ->
                                if(option == null) {
                                    DropdownMenuItem(onClick = { categoryCodeText = "선택 사항 없음"}) {
                                        Text(text = "정보를 불러오는 중입니다.")
                                    }
                                } else {
                                    if(!option.categories!![pageIndex].subs.isNullOrEmpty()) {
                                        option.categories!![pageIndex].subs.forEach { sub ->
                                            DropdownMenuItem(onClick = {
                                                categoryCode = sub.code
                                                categoryCodeText = sub.codeName
                                            } ) {
                                                Text(text = sub.codeName)
                                            }
                                        }
                                    }
                                    DropdownMenuItem(onClick = {
                                        categoryCode = option.categories!![pageIndex].code
                                        categoryCodeText = option.categories!![pageIndex].codeName
                                    }) {
                                        Text(text = option.categories!![pageIndex].codeName)
                                    }
                                }
                            }
                        }
                    }
                }
                Column() {
                    Box() {
                        TextButton(onClick = { itemGradeQualitiesExpanded = true }) {
                            Text(text = "아이템 등급 : ${if(itemGradeQualities == 0) "선택 사항 없음" else itemGradeQualities}")
                        }
                        DropdownMenu(expanded = itemGradeQualitiesExpanded, onDismissRequest = { itemGradeQualitiesExpanded = false }) {
                            auctionsDataList.forEach { auctionsData ->
                                if(auctionsData == null) {
                                    DropdownMenuItem(onClick = {
                                        itemGradeQualitiesExpanded = false
                                        itemGradeQualities = 0
                                    }) {
                                        Text(text = "데이터를 불러오는 중입니다")
                                    }
                                } else {
                                    auctionsData.itemGradeQualities!!.forEach { grade ->
                                        DropdownMenuItem(onClick = {
                                            itemGradeQualitiesExpanded = false
                                            itemGradeQualities = grade
                                        }) {
                                            Text(text = "$grade")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                    auctionsDataList.forEach { auctionsData ->
                        if(auctionsData == null) {
                            Text(text = "데이터를 불러오는 중입니다")
                        } else {
                            TextField(value = "$itemMinLevel", onValueChange = {
                                itemMinLevel = if(it == "") {
                                    0
                                } else if(auctionsData.maxItemLevel < it.toInt()) {
                                    1700
                                }  else {
                                    it.toInt()
                                }
                            }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                            TextField(value = "$itemMaxLevel", onValueChange = {
                                itemMaxLevel = if(auctionsData.maxItemLevel < it.toInt() ) {
                                    1700
                                } else if(it != "") {
                                    0
                                } else {
                                    it.toInt()
                                }
                            })
                        }
                }
                TextField(value = itemName, onValueChange = {itemName = it})
                Column() {
                    Box() {
                        TextButton(onClick = {
                            itemGradeExpanded = true
                        }) {
                            Text("아이템 등급 : $itemGradeText")
                        }
                        DropdownMenu(expanded = itemGradeExpanded, onDismissRequest = { itemGradeExpanded = false }) {
                            auctionsDataList.forEach { option ->
                                if(option == null) {
                                    DropdownMenuItem(onClick = { itemGradeText = "선택 사항 없음"}) {
                                        Text(text = "정보를 불러오는 중입니다.")
                                    }
                                } else {
                                    if(option.itemGrades.isNullOrEmpty()) {
                                        DropdownMenuItem(onClick = { itemGradeText = "선택 사항 없음"}) {
                                            Text(text = "정보를 불러오는 중입니다.")
                                        }
                                    } else {
                                        option.itemGrades.forEach { garde ->
                                            if (garde != null) {
                                                DropdownMenuItem(onClick = {
                                                    itemGradeText = garde
                                                    itemGrade = garde
                                                }) {
                                                    Text(garde)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Column() {
                    Box() {
                        TextButton(onClick = {
                            itemTierExpanded = true
                        }) {
                            Text("아이템 티어 : ${if(itemTierText == 0) "선택 사항 없음" else itemTierText}")
                        }
                        DropdownMenu(expanded = itemTierExpanded, onDismissRequest = { itemTierExpanded = false }) {
                            auctionsDataList.forEach { option ->
                                if(option == null) {
                                    DropdownMenuItem(onClick = { itemTierText = 0}) {
                                        Text(text = "정보를 불러오는 중입니다.")
                                    }
                                } else {
                                    if(option.itemTiers.isNullOrEmpty()) {
                                        DropdownMenuItem(onClick = { itemTierText = 0}) {
                                            Text(text = "정보를 불러오는 중입니다.")
                                        }
                                    } else {
                                        option.itemTiers.forEach { tier ->
                                            if (tier != null) {
                                                DropdownMenuItem(onClick = {
                                                    itemTierText = tier
                                                    itemTier = tier
                                                }) {
                                                    Text("$tier")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Column() {
                    Box() {
                        TextButton(onClick = {
                            characterClassexpanded = true
                        }) {
                            Text("직업 : $characterClassText")
                        }
                        DropdownMenu(expanded = characterClassexpanded, onDismissRequest = { characterClassexpanded = false }) {
                            auctionsDataList.forEach { option ->
                                if(option == null) {
                                    DropdownMenuItem(onClick = { characterClassText = ""}) {
                                        Text(text = "정보를 불러오는 중입니다.")
                                    }
                                } else {
                                    if(option.classes.isNullOrEmpty()) {
                                        DropdownMenuItem(onClick = { characterClassText = ""}) {
                                            Text(text = "정보를 불러오는 중입니다.")
                                        }
                                    } else {
                                        option.classes.forEach { clas ->
                                            if (clas != null) {
                                                DropdownMenuItem(onClick = {
                                                    characterClassText = clas
                                                    characterClass = clas
                                                    characterClassexpanded = false
                                                    auctionsDataText = "선택 사항 없음"
                                                    auctionsDetailDataText = "선택 사항 없음"
                                                }) {
                                                    Text("$clas")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            itemsIndexed(auctionsDataList) { idx, it ->
                Column() {
                        Box() {
                            TextButton(onClick = {
                                auctionsDataExpanded = true
                            }) {
                                Text("스킬 : $auctionsDataText")
                            }
                            DropdownMenu(expanded = auctionsDataExpanded, onDismissRequest = { auctionsDataExpanded = false }) {
                                auctionsDataList.forEach { auctionData ->
                                    if(auctionData == null) {
                                        DropdownMenuItem(onClick = { auctionsDataText = "선택 사항 없음"}) {
                                            Text(text = "정보를 불러오는 중입니다.")
                                        }
                                    } else {
                                        if(auctionData.skillOptions.isNullOrEmpty()) {
                                            DropdownMenuItem(onClick = { auctionsDataText = "선택 사항 없음"}) {
                                                Text(text = "정보를 불러오는 중입니다.")
                                            }
                                        } else {
                                            auctionData.skillOptions.forEach { skillOption ->
                                                if(characterClass == skillOption.className) {
                                                    if (skillOption != null) {
                                                        DropdownMenuItem(onClick = {
                                                            auctionsDataText = skillOption.text!!
                                                            dataList.firstOption = skillOption.value
                                                            actionDataIndex = idx
                                                            auctionsDataExpanded = false
                                                            auctionsDetailDataText = "선택 사항 없음"
                                                        }) {
                                                            Text(skillOption.text!!)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                if(auctionsDataText != "선택 사항 없음") {
                    Column() {
                        TextButton(onClick = {
                            auctionsDataDetailExpanded = true
                        }) {
                            Text("스킬 옵션 : $auctionsDetailDataText")
                        }
                        DropdownMenu(expanded = auctionsDataDetailExpanded, onDismissRequest = { auctionsDataDetailExpanded = false }) {
                            if(auctionsDataList[actionDataIndex] == null) {
                                DropdownMenuItem(onClick = { auctionsDetailDataText = "선택 사항 없음"}) {
                                    Text(text = "정보를 불러오는 중입니다.")
                                }
                            } else {
                                if(auctionsDataList[actionDataIndex]!!.skillOptions.isNullOrEmpty()) {
                                    DropdownMenuItem(onClick = { auctionsDetailDataText = "선택 사항 없음"}) {
                                        Text(text = "정보를 불러오는 중입니다.")
                                    }
                                } else {
                                    auctionsDataList.forEach { auctionsData ->
                                        auctionsData!!.skillOptions!!.filter { it.className == characterClass }[actionDataIndex].tripods.forEach { tripod ->
                                            DropdownMenuItem(onClick = {
                                                dataList.secondOption = tripod.value
                                                auctionsDetailDataText = tripod.text!!
                                                auctionsDataDetailExpanded = false
                                            }) {
                                                Text(text = "${tripod.text}")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Column() {
                    TextField(value = "${skillMinLevel}", onValueChange = {
                        if(skillMinLevel is Int|| it != "") {
                            skillMinLevel = it.toInt()
                            dataList.minValue = skillMinLevel
                        } else {
                            skillMinLevel = 0
                        }

                    })
                    TextField(value = "${skillsMaxLevel}", onValueChange = {
                        if(skillsMaxLevel is Int || it != "") {
                            skillsMaxLevel = it.toInt()
                            dataList.maxValue = skillsMaxLevel
                        } else {
                            skillsMaxLevel = 0
                        }
                    })
                }
                // etc
                Column() {
                    Box() {
                        TextButton(onClick = {
                            auctionsEtcDataExpanded = true
                        }) {
                            Text("기타 : $auctionsDataEtcText")
                        }
                        DropdownMenu(expanded = auctionsEtcDataExpanded, onDismissRequest = { auctionsEtcDataExpanded = false }) {
                            auctionsDataList.forEach { auctionData ->
                                if(auctionData == null) {
                                    DropdownMenuItem(onClick = { auctionsDataEtcText = "선택 사항 없음"}) {
                                        Text(text = "정보를 불러오는 중입니다.2")
                                    }
                                } else {
                                    if(auctionData.etcOptions.isNullOrEmpty()) {
                                        DropdownMenuItem(onClick = { auctionsDataEtcText = "선택 사항 없음"}) {
                                            Text(text = "정보를 불러오는 중입니다.1")
                                        }
                                    } else {
                                        auctionData.etcOptions.forEachIndexed { idx, etcOption ->
                                            DropdownMenuItem(onClick = {
                                                auctionsDataEtcText = etcOption.text!!
                                                etcDataList.firstOption = etcOption.value
                                                actionDataEtcIndex = idx
                                                Log.d("치킨맛", "$actionDataEtcIndex")
                                                auctionsEtcDataExpanded = false
                                                auctionsDetailEtcDataText = "선택 사항 없음"
                                            }) {
                                                Text(text = etcOption.text!!)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Column() {
                    TextButton(onClick = {
                        auctionsDataEtcDetailExpanded = true
                    }) {
                        Text("스킬 옵션 : $auctionsDetailEtcDataText")
                    }
                    DropdownMenu(expanded = auctionsDataEtcDetailExpanded, onDismissRequest = { auctionsDataEtcDetailExpanded = false }) {
                        if(it == null) {
                            DropdownMenuItem(onClick = { auctionsDetailEtcDataText = "선택 사항 없음"}) {
                                Text(text = "정보를 불러오는 중입니다.")
                            }
                        } else {
                            if(it.etcOptions!![actionDataEtcIndex] == null) {
                                DropdownMenuItem(onClick = { auctionsDetailEtcDataText = "선택 사항 없음"}) {
                                    Text(text = "정보를 불러오는 중입니다.")
                                }
                            } else {
                                auctionsDataList.forEach { auctionsData ->
                                    auctionsData!!.etcOptions!![actionDataEtcIndex].etcSubs.filter { it.className == characterClass || it.className == "" }.forEach { etcSub ->
                                        DropdownMenuItem(onClick = {
                                            etcDataList.secondOption = etcSub.value
                                            auctionsDetailEtcDataText = etcSub.text!!
                                            auctionsDataEtcDetailExpanded = false
                                        }) {
                                            Text(text = "${etcSub.text}")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Column() {
                    TextField(value = "$etcMinLevel", onValueChange = {
                        if(etcMinLevel is Int) {
                            etcMinLevel = it.toInt()
                            etcDataList.minValue = etcMinLevel
                        } else {
                            etcMinLevel = 0
                        }

                    })
                    TextField(value = "$etcMaxLevel", onValueChange = {
                        if(etcMaxLevel is Int) {
                            etcMaxLevel = it.toInt()
                            etcDataList.maxValue = etcMaxLevel
                        } else {
                            etcMaxLevel = 0
                        }
                    })
                }
            }
            item { Spacer(modifier = Modifier.height(5.dp)) }
            if(itemDataList.isNullOrEmpty()) {
                item { Text(text = "검색 옵션을 입력해주세요") }
            } else {
                items(itemDataList.toList()) { itemData ->
                    if(itemData == null || itemData.items.isNullOrEmpty()) {
                        Text(text = "아이템을 불러오는 중입니다")
                    } else {
                        itemData.items.forEach { itemData ->
                            if(itemData == null) {
                                Text(text = "아이템을 불러오는 중입니다")
                            } else {
                                Row() {
                                    AsyncImage(model = "${itemData.icon}", contentDescription = "${itemData.name}")
                                    Column() {
                                        Row {
                                           Text(text = "${itemData.name}")
                                           Text(text = "${itemData.Level}")
                                        }
                                        Row {
                                            Text(text = "${itemData.gradeQuality}")
                                            itemData.options.forEach { option ->
                                                Text(text = "${option.optionName}")
                                                // 이미지로 교체 예정(tripods)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

