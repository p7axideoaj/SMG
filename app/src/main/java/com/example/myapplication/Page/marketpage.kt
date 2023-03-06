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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.dataModel.marketsData
import com.example.myapplication.dataModel.marketsItemData
import com.example.myapplication.dataModel.marketsOption
import com.example.myapplication.getJSONMarkets
import com.example.myapplication.getJSONMarketsItemData
import com.example.myapplication.getJSONMarketsOption
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject


@Composable
fun MarketPage(navController: NavHostController) {
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
    var optionList = remember {
        mutableStateListOf<marketsData?>()
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
    val marketItem = remember {
         mutableStateListOf<marketsOption?>()
    }
    var itemId by remember {
        mutableStateOf(0)
    }
    DisposableEffect(0){
        scope.launch {
            while (true) {
                getJSONMarketsOption(marketItem, itemName, if(itemGrade == "선택 사항 없음") null else itemGrade,
                    if(itemTier == 0) null else itemTier , if(characterClass == "선택 사항 없음") null else characterClass, categoryCode)
//                if(itemId != 0) {
//                    getJSONMarketsItemData(itemDetailData, itemId)
//                    delay(1000)
//                }
                getJSONMarkets(optionList)
                delay(2500)
            }
        }
        onDispose {
            scope.cancel()
        }
    }
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "거래소")},
            navigationIcon = {
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(Icons.Default.ArrowBack, "backIcon")
                }
            },
            backgroundColor = Color.White,
            elevation = 0.dp)
    }) {_ ->
        LazyColumn(Modifier.padding(16.dp)) {
            item {
                LazyRow() {
                    optionList.forEach {
                        itemsIndexed(it!!.Categories) { idx, Categorie ->
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
                                    Text("${if(!optionList.isNullOrEmpty()) optionList.map { it!!.Categories[pageIndex].codeName }.first() else ""} : $categoryCodeText")
                                }
                                DropdownMenu(expanded = categoryCodeExpanded, onDismissRequest = { categoryCodeExpanded = false }) {
                                    optionList.forEach { option ->
                                        if(option == null) {
                                            DropdownMenuItem(onClick = { categoryCodeText = "선택 사항 없음"}) {
                                                Text(text = "정보를 불러오는 중입니다.")
                                            }
                                        } else {
                                            if(!option.Categories[pageIndex].subs.isNullOrEmpty()) {
                                                option.Categories[pageIndex].subs.forEach { sub ->
                                                    DropdownMenuItem(onClick = {
                                                        categoryCode = sub.code
                                                        categoryCodeText = sub.codeName
                                                    } ) {
                                                        Text(text = sub.codeName)
                                                    }
                                                }
                                            }
                                            DropdownMenuItem(onClick = {
                                                categoryCode = option.Categories[pageIndex].code
                                                categoryCodeText = option.Categories[pageIndex].codeName
                                            }) {
                                                Text(text = option.Categories[pageIndex].codeName)
                                            }
                                        }
                                    }
                                }
                            }
                    }
                OutlinedTextField(value = itemName, onValueChange = {itemName = it}, label = { Text(
                    text = "아이템이름"
                ) })
                Column() {
                    Box() {
                        TextButton(onClick = {
                            itemGradeExpanded = true
                        }) {
                            Text("아이템 등급 : $itemGradeText")
                        }
                        DropdownMenu(expanded = itemGradeExpanded, onDismissRequest = { itemGradeExpanded = false }) {
                            optionList.forEach { option ->
                                if(option == null) {
                                    DropdownMenuItem(onClick = { itemGradeText = "선택 사항 없음"}) {
                                        Text(text = "정보를 불러오는 중입니다.")
                                    }
                                } else {
                                    if(option.ItemGrades.isNullOrEmpty()) {
                                        DropdownMenuItem(onClick = { itemGradeText = "선택 사항 없음"}) {
                                            Text(text = "정보를 불러오는 중입니다.")
                                        }
                                    } else {
                                        option.ItemGrades.forEach { garde ->
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
                            optionList.forEach { option ->
                                if(option == null) {
                                    DropdownMenuItem(onClick = { itemTierText = 0}) {
                                        Text(text = "정보를 불러오는 중입니다.")
                                    }
                                } else {
                                    if(option.ItemTiers.isNullOrEmpty()) {
                                        DropdownMenuItem(onClick = { itemTierText = 0}) {
                                            Text(text = "정보를 불러오는 중입니다.")
                                        }
                                    } else {
                                        option.ItemTiers.forEach { tier ->
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
                            optionList.forEach { option ->
                                if(option == null) {
                                    DropdownMenuItem(onClick = { characterClassText = ""}) {
                                        Text(text = "정보를 불러오는 중입니다.")
                                    }
                                } else {
                                    if(option.Classes.isNullOrEmpty()) {
                                        DropdownMenuItem(onClick = { characterClassText = ""}) {
                                            Text(text = "정보를 불러오는 중입니다.")
                                        }
                                    } else {
                                        option.Classes.forEach { clas ->
                                            if (clas != null) {
                                                DropdownMenuItem(onClick = {
                                                    characterClassText = clas
                                                    characterClass = clas
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
            item { Spacer(modifier = Modifier.height(10.dp)) }
            if(marketItem.isNullOrEmpty()) {
                item { Text(text = "검색 옵션을 입력해주세요") }
            } else {
                items(marketItem.toList()) { item ->
                    if(item == null) {
                        Text(text = "아이템을 불러오는 중입니다")
                    } else {
                        item.items.forEach { it ->
                            if(it == null) {
                                Text(text = "데이터를 불러오지 못했습니다.")
                            } else {
                                var expandableState by remember {
                                    mutableStateOf(false)
                                }
                                val rotate by animateFloatAsState(targetValue = if(expandableState) 180f else 0f)
                                val itemDetailData = remember {
                                    mutableStateListOf<marketsItemData?>()
                                }
                                Card(
                                    Modifier
                                        .fillMaxWidth()
                                        .animateContentSize(animationSpec = tween(durationMillis = 100,))
                                        .padding(10.dp)) {
                                    Column(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)) {
                                        Row(Modifier
                                            .fillMaxWidth()) {
                                            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                                AsyncImage(model = "${it.icon}", contentDescription = "${it.name}")
                                                Column {
                                                    Text(text = "${it.name}")
                                                    Text(text = "어제 최고가 : ${it.yDayAvgPrice}")
                                                }
                                                IconButton(onClick = {
                                                    expandableState = !expandableState
                                                    itemId = it.id!!
                                                    Log.d("도도토리묵", "$itemId")
                                                    getJSONMarketsItemData(itemDetailData, itemId)
                                                    },
                                                    Modifier
                                                        .weight(1f)
                                                        .rotate(rotate)) {
                                                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "열기")
                                                }
                                            }
                                        }
                                        AnimatedVisibility(visible = expandableState) {
                                            Box(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .height(300.dp)) {
                                                LazyColumn() {
                                                    if(itemDetailData.isNullOrEmpty()) {
                                                        item { Text("아이템을 불러오는 중입니다...") }
                                                    } else {
                                                        items(itemDetailData.toList()) {
                                                            if(it == null) {
                                                                Text(text = "아이템 정보를 불러오는 중입니다.")
                                                            } else {
                                                                val json = JSONObject(it.toolTip)
                                                                for(i in 0 until json.length()) {
                                                                    val value = json.getJSONObject("Element_%03d".format(i))
                                                                    when(value["type"]) {
                                                                        "NameTagBox", "SingleTextBox", "MultiTextBox", "ShowMeTheMoney" ->
                                                                            Text(
                                                                                parse(value.getString("value"))
                                                                            )
                                                                        "ItemTitle" -> {
                                                                            Column() {
                                                                                Text(
                                                                                    text = parse(
                                                                                        value.getJSONObject("value")
                                                                                            .getString("leftStr0")
                                                                                    )
                                                                                )
                                                                                Text(
                                                                                    text = parse(
                                                                                        value.getJSONObject("value")
                                                                                            .getString("leftStr2")
                                                                                    )
                                                                                )
                                                                            }
                                                                        }
                                                                    }
                                                                }
//                                                                var price = 0.0;
//                                                                for (i in it.stats.indices) {
//                                                                    if(i+1 < it.stats.size) {
//                                                                        price = if(it.stats[i]!!.avgPrice!! >= it.stats[i+1]!!.avgPrice!!) {
//                                                                            it.stats!![i]!!.avgPrice!!
//                                                                        } else {
//                                                                            it.stats!![i+1]!!.avgPrice!!
//                                                                        }
//                                                                    }
//                                                                }
                                                                Text(text = "최근 거래가 : ${it.stats[0]!!.avgPrice!!}")
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
            }
        }
    }
}