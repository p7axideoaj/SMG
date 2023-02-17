package com.example.myapplication.Page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.dataModel.NewsNotiData
import com.example.myapplication.dataModel.guildData
import com.example.myapplication.getJSONGuildRankList
import com.example.myapplication.getJSONNewsNoti
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GuildRankPage(navController: NavHostController) {
    val serverName = listOf("루페온", "실리안", "아만","카마인","카제로스","아브렐슈드", "카단", "니나브")
    var selectSeverName by remember {
        mutableStateOf("서버 선택")
    }
    val scope = MainScope()
    val guildDataList = remember {
        mutableStateListOf<guildData?>()
    }
    DisposableEffect(0){
        scope.launch {
            while (true) {
                if(selectSeverName != "서버 선택") {
                    getJSONGuildRankList(guildDataList, selectSeverName)
                }
                delay(3000)
            }
        }
        onDispose {
            scope.cancel()
        }
    }
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = "길드 랭킹")
            },
            navigationIcon = {
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(Icons.Default.ArrowBack, "backIcon")
                }
            },
            backgroundColor = Color.White,
            elevation = 0.dp
        )
    }, modifier = Modifier.padding(16.dp)) {
        LazyColumn() {
            item {
                var expandableState by remember {
                    mutableStateOf(false)
                }
                val rotate by animateFloatAsState(targetValue = if(expandableState) 180f else 0f)
                Card(
                    Modifier
                        .fillMaxWidth()
                        .animateContentSize(animationSpec = tween(durationMillis = 100,))
                        .padding(10.dp)) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp)) {
                        Row(
                            Modifier
                            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Box() {
                                Row() {
                                    Text(text = selectSeverName, Modifier.padding(top = 12.dp))
                                    IconButton(onClick = {
                                        selectSeverName = "서버 선택"
                                        guildDataList.clear()
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "검색기록 삭제")
                                    }
                                }
                            }
                            IconButton(onClick = { expandableState = !expandableState},
                                Modifier
                                    .rotate(rotate)) {
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "열기")
                            }
                        }
                        AnimatedVisibility(visible = expandableState) {
                            Column() {
                                serverName.forEach {
                                    Text(it, Modifier.clickable { selectSeverName = it })
                                }
                            }
                        }
                    }
                }
            }
            if(selectSeverName == "서버 선택") {
                item { Text("서버를 선택해주세요") }
            } else {
                if(guildDataList.isNullOrEmpty()) {
                    item { Text("길드 데이터를 불러오는중입니다.") }
                } else {
                    items(guildDataList.toList()) { it ->
                        if(it == null) {
                            Text("길드 데이터를 불러오지 못했습니다.")
                        } else {
                            Column() {
                                Text(text = "${it.rank}위")
                                Text(text = "길드명 : ${it.guildName}")
                                Text(text = "길드마스터 : ${it.MasterName}")
                                Text(text = "길드인원 : ${it.memberCount} / ${it.maxMemberCount}")
                                Text(text = "점령전점수: ${it.rating}")
                                Text(text = "${it.guildMessage}")
                            }
                        }
                    }
                }
            }
        }
    }
}