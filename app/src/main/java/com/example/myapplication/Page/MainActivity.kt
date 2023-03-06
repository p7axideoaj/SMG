package com.example.myapplication.Page

import android.content.Context
import android.content.Intent
import android.net.Uri

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.imageLoader
import coil.util.DebugLogger
import com.example.myapplication.*
import com.example.myapplication.dataModel.*
import com.example.myapplication.sharedHelper.sharedHelper
import com.google.accompanist.pager.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//import okhttp3.logging.HttpLoggingInterceptor

class MainActivity : ComponentActivity() {
    lateinit var prefs: sharedHelper
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            val context = LocalContext.current
            val navController = rememberNavController()
            prefs = sharedHelper(context)
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { Homepage(navController, context, prefs, lifecycleScope) }
                    composable("search") { SerachPageHome(navController, prefs, context) }
                    composable("noti") { NotiPage(navController, context)}
                    composable("profileDetail/{username}") { it ->
                        it.arguments?.getString("username")
                            ?.let { it1 -> ProfileDetailHome(navController, it1, context, lifecycleScope)}
                    }
                    composable("action") { actionPage(navController)}
                    composable("calendar") {CalendarPage(navController, context)}
                    composable("guildRank") { GuildRankPage(navController) }
                    composable("addUser") { addUser(navController, prefs) }
                    composable("markets") { MarketPage(navController)}
                }
        }
    }
}

@ExperimentalPagerApi
@Composable
fun Homepage(
    navController: NavController,
    context: Context,
    prefs: sharedHelper,
    lifecycleScope: LifecycleCoroutineScope
) {
    Log.d("아아", "아")
    val list = remember {
        mutableStateListOf<newsData>()
    }
    val userlist = remember {
        mutableStateListOf<charterProfile?>()
    }
    var serverName by remember {
        mutableStateOf("")
    }
    var guildRankList = remember {
        mutableStateListOf<guildData?>()
    }
    var abyssRaidDataList = remember {
        mutableStateListOf<abyssRaidList?>()
    }
    var guardianRaidDataList = remember {
        mutableStateListOf<GuardianRaidsData?>()
    }
    val scope = MainScope()
    val scaffoldState = rememberScaffoldState()
    DisposableEffect(0){
        scope.launch {
            while (true) {
                getJSONNewsDate(list, context)
                prefs.getString("name")?.let { getJSONProfile(userlist, context, it) }
                if(!serverName.isNullOrEmpty()) {
                    getJSONGuildRankList(guildRankList, serverName)
                }
                getJSONAbyssRaidList(abyssRaidDataList)
                getJSONGuardianRaids(guardianRaidDataList)
                delay(3000)
            }
        }
        onDispose {
            scope.cancel()
        }
    }

    Scaffold(scaffoldState = scaffoldState) { _ ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
            , horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Row(Modifier.clickable { navController.navigate("noti") }) {
                    Icon(Icons.Default.Notifications, contentDescription = "공지")
                    Text(text = "공지사항 보러가기")
                }
                Box(Modifier.clickable { navController.navigate("search") }) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "검색")
                        Icon(imageVector = Icons.Default.Search, contentDescription = "검색아이콘")
                    }
                }
                Box(Modifier.clickable { navController.navigate("markets") }) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "거래소 바로가기")
                        Icon(imageVector = Icons.Default.Done, contentDescription = "거래소")
                    }
                }
                Box(Modifier.clickable { navController.navigate("action") }) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "경매장 바로가기")
                        Icon(imageVector = Icons.Default.Done, contentDescription = "경매장")
                    }
                }
                var name by remember {
                    mutableStateOf(prefs.getString("name"))
                }
                val pagerState = rememberPagerState()
                if(list.isNullOrEmpty()) {
                    Text(text = "이벤트를 불러오는 중입니다.")
                } else {
                    HorizontalPager(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth(), count = list.size, verticalAlignment = Alignment.Top, state = pagerState) { page ->
                        PageImage(list, list[page], page, context)
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    HorizontalPagerIndicator(pagerState = pagerState, Modifier.padding(5.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                if (name.isNullOrEmpty()) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center) {
                        TextButton(onClick = {
                            navController.navigate("addUser")
                        }) {
                            Text("내 모험가 등록하기")
                        }
                    }
                } else {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        LazyRow() {
                            items(userlist) {
                                if(it == null) {
                                    Text(text = "등록되지 않은 모험가입니다.")
                                } else {
                                    serverName = "${it.ServerName?: "없음"}" // 서버 이름 넘겨주기
                                    AsyncImage(model = "${it.CharacterImage}", contentDescription = "image")
                                    Column() {
                                        Text(text = "${it.CharacterName}")
                                        Text(text = "${if(it.Title.isNullOrEmpty()) "" else it.Title}")
                                    }
                                    IconButton(onClick = {
                                        prefs.remove("name")
                                        name = prefs.getString("name")
                                        serverName = ""
                                        guildRankList.clear()
                                    }) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item {
                Row(Modifier.clickable{ navController.navigate("calendar") }) {
                    Icon(Icons.Default.Add, contentDescription = "일정 더보기")
                    Text(text = "이벤트 일정보러가기")
                }
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item {Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Text("길드 랭킹", Modifier.padding(top = 8.dp))
                TextButton(onClick = { navController.navigate("guildRank") }) {
                    Text("더보기+")
                }
            }}
            if(serverName.isNullOrEmpty()) {
                item { Text(text = "사용자 등록 후 길드 랭킹보기") }
            } else {
                if(guildRankList.isNullOrEmpty()) {
                    item {
                        Text("길드 정보를 불러오는중입니다.")
                    }
                } else {
                    item { Text(text = "서버 : ${serverName}")}
                    item {Spacer(modifier = Modifier.height(10.dp))}
                    item { Box(Modifier.fillMaxWidth()) {
                        LazyRow() {
                            items(guildRankList.toList().filter { it!!.rank!! <= 5 }) {
                                if (it == null) {
                                    Text("길드 정보를 불러오지못했습니다")
                                } else {
                                        Box(
                                            Modifier
                                                .fillMaxWidth(0.2f)
                                                .clickable {
                                                    lifecycleScope.launch {
                                                        scaffoldState.snackbarHostState.showSnackbar(
                                                            message = "길드 점수 : ${it.rating}\n문의 : ${it.guildMessage}"
                                                        )
                                                    }
                                                }) {
                                            Column() {
                                                Text(text = "${it.rank}위")
                                                Text(text = "길드 : ${it.guildName}", overflow = TextOverflow.Ellipsis)
                                                Text(text = "마스터 : ${it.MasterName}", overflow = TextOverflow.Ellipsis)
                                                Text(text = "인원 : ${it.memberCount} / ${it.maxMemberCount}")
                                            }
                                        }
                                    Spacer(modifier = Modifier.width(5.dp))
                                }
                            }
                        }
                    } }
                }
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item {Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Text("심연의 던전")
            }}
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item {
                abyssRaid(abyssRaidDataList)
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item {Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Text("가디언 토벌")
            }}
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item { guardianRaid(guardianRaidDataList) }
        }
    }
}
@Composable
fun abyssRaid(abyssRaidDataList: SnapshotStateList<abyssRaidList?>) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(280.dp)) {
        if(abyssRaidDataList.isNullOrEmpty()) {
            Text(text = "도전 어비스 정보를 불러오고 있습니다")
        } else {
            val imageLoader = LocalContext.current.imageLoader.newBuilder()
                .logger(DebugLogger())
                .build()
            var index by remember { mutableStateOf(-1) }
            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                itemsIndexed(abyssRaidDataList.toList()) { idx, it ->
                    if (it == null) {
                        Text(text = "도전 어비스 정보를 불러오지 못헀습니다.")
                    } else {
                        Column(Modifier.fillMaxWidth(0.5f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(Modifier.clickable { index = idx }) {
                                AsyncImage(
                                    model = "${it.image}",
                                    contentDescription = "보스이미지",
                                    imageLoader = imageLoader,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Box(Modifier.align(Alignment.BottomEnd)) {
                                    Text(text = "${it.name}", color = Color.White, modifier = Modifier.padding(end = 4.dp), fontSize = 12.sp)
                                }
                            }
                            Box() {
                                Text(text = "${it.startTime!!.substring(5)} ~ ${it.endTime!!.substring(5)}", overflow = TextOverflow.Ellipsis, fontSize = 12.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        if (index != -1) {
                            Dialog(
                                onDismissRequest = {
                                    index = -1
                                }
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .width(270.dp)
                                        .padding(10.dp),
                                    color = Color.White
                                ) {
                                    Box(Modifier.fillMaxSize()) {
                                        LazyColumn() {
                                            item {
                                                if(abyssRaidDataList[index] == null) {
                                                    Text(text = "도전 어비스 정보를 불러오지 못헀습니다.")
                                                } else {
                                                    AsyncImage(
                                                        model = "${abyssRaidDataList[index]!!.image}",
                                                        contentDescription = "심연 이미지",
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(120.dp),
                                                        contentScale = ContentScale.Crop
                                                    )
                                                    Text(text = "이름 : ${abyssRaidDataList[index]!!.name}")
                                                    Text(text = "설명 : ${abyssRaidDataList[index]!!.description}")
                                                    Text(text = "최소 권장 레벨 : ${abyssRaidDataList[index]!!.minCharacterLevel}")
                                                    Text(text = "최소 권장 아이템 레벨 : ${abyssRaidDataList[index]!!.minItemLevel}")
                                                    Text(text = "지역 : ${abyssRaidDataList[index]!!.areaName}")
                                                    Text(text = "${abyssRaidDataList[index]!!.startTime} ~ ${abyssRaidDataList[index]!!.endTime}")
                                                    Box(
                                                        Modifier
                                                            .fillMaxWidth()
                                                            .height(100.dp)) {
                                                        LazyVerticalGrid(
                                                            columns = GridCells.Fixed(2),
                                                        ) {
                                                            items(abyssRaidDataList[index]!!.rewardItems) {
                                                                Column {
                                                                    AsyncImage(
                                                                        model = "${it.icon}",
                                                                        contentDescription = "${it.name}"
                                                                    )
                                                                    Text("이름 : ${it.name}")
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
}
@Composable
fun guardianRaid(guardianRaidDataList: SnapshotStateList<GuardianRaidsData?>) {
    Column() {
        Box(
            Modifier
                .fillMaxWidth()
                .height(200.dp)) {
                if(guardianRaidDataList.isNullOrEmpty()) {
                    Text(text = "가디언 토벌 정보를 불러오고 있습니다")
                } else {
                    val imageLoader = LocalContext.current.imageLoader.newBuilder()
                        .logger(DebugLogger())
                        .build()
                    LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        if(guardianRaidDataList[0] == null) {
                            item { Text(text = "가디언 토벌 정보를 불러오지 못했습니다") }
                        } else {
                            if(guardianRaidDataList[0]!!.raids.isNullOrEmpty()) {
                                item { Text(text = "가디언 레이드 정보를 불러오지 못했습니다") }
                            } else {
                                itemsIndexed(guardianRaidDataList[0]!!.raids!!.toList()) { idx, it ->
                                    if (it == null) {
                                        Text(text = "가디언 토벌 정보를 불러오지 못헀습니다.")
                                    } else {
                                        Column(Modifier.fillMaxWidth(0.5f), horizontalAlignment = Alignment.CenterHorizontally) {
                                            Box() {
                                                AsyncImage(
                                                    model = "${it.image}",
                                                    contentDescription = "보스이미지",
                                                    imageLoader = imageLoader,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(70.dp),
                                                    contentScale = ContentScale.Crop
                                                )
                                                Box(Modifier.align(Alignment.BottomEnd)) {
                                                    Text(text = "${it.name}", color = Color.White, modifier = Modifier.padding(end = 4.dp), fontSize = 12.sp)
                                                }
                                            }
                                            Box() {
                                                Text(text = "${it.startTime!!.substring(5)} ~ ${it.endTime!!.substring(5)}", overflow = TextOverflow.Ellipsis, fontSize = 12.sp)
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(5.dp))
                                    }
                                }
                            }
                        }
                }
            }
        }
        Card(
            Modifier
                .fillMaxWidth()
                .animateContentSize(animationSpec = tween(durationMillis = 5,))
                .padding(10.dp)) {
            var expandableState by remember {
                mutableStateOf(false)
            }
            val rotate by animateFloatAsState(targetValue = if(expandableState) 180f else 0f)
            var selectIndex by remember {
                mutableStateOf(0)
            }
            var strList = listOf(2,3,4,5,6,7)
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Box() {
                        Text(text = "보상 정보", Modifier.padding(top = 12.dp))
                    }
                    IconButton(onClick = { expandableState = !expandableState},
                        Modifier
                            .rotate(rotate)) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "열기")
                    }
                }
                AnimatedVisibility(visible = expandableState, Modifier.height(250.dp)) {
                    LazyColumn(Modifier.padding(16.dp)) {
                        item {
                            LazyRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                itemsIndexed(strList) { index, num ->
                                    TextButton(onClick = { selectIndex = index }, modifier = Modifier.size(40.dp), ) {
                                        Text(text = "${num}단계", fontSize = 8.sp, color = Color.Black)
                                    }
                                    Spacer(modifier = Modifier.width(2.dp))
                                }
                            }
                        }
                        if(guardianRaidDataList.isNullOrEmpty()) {
                            item { Text(text = "가디언 토벌 정보를 불러오고 있습니다") }
                        } else {
                            items(guardianRaidDataList.toList()) {
                                if(it == null) {
                                    Text(text = "가디언 보상 정보를 불러오지 못했습니다")
                                } else {
                                    if(it.rewardItems != null) {
                                        when(selectIndex) {
                                            0 -> {
                                                val index = 0
                                                Column() {
                                                    Text(text = "적정레벨 : ${it.rewardItems[index].expeditionItemLevel}")
                                                    it.rewardItems[index].items.mapIndexed { index, reward ->
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            AsyncImage(
                                                                model = reward.icon,
                                                                contentDescription = "보상 아이콘"
                                                            )
                                                            Text("보상 이름 : ${reward.name}")
                                                        }
                                                    }
                                                }
                                            }
                                            1 -> {
                                                val indexList = listOf(1,2)
                                                LazyColumn(Modifier.height(250.dp)) {
                                                    items(indexList) { idx ->
                                                        Text(text = "적정레벨 : ${it.rewardItems[idx].expeditionItemLevel}")
                                                        it.rewardItems[idx].items.mapIndexed { index, reward ->
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                AsyncImage(
                                                                    model = reward.icon,
                                                                    contentDescription = "보상 아이콘"
                                                                )
                                                                Text("보상 이름 : ${reward.name}")
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            2 -> {
                                                val indexList = listOf(3)
                                                LazyColumn(Modifier.height(250.dp)) {
                                                    items(indexList) { idx ->
                                                        Text(text = "적정레벨 : ${it.rewardItems[idx].expeditionItemLevel}")
                                                        it.rewardItems[idx].items.mapIndexed { index, reward ->
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                AsyncImage(
                                                                    model = reward.icon,
                                                                    contentDescription = "보상 아이콘"
                                                                )
                                                                Text("보상 이름 : ${reward.name}")
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            3 -> {
                                                val indexList = listOf(4,5,6,7)
                                                LazyColumn(Modifier.height(250.dp)) {
                                                    items(indexList) { idx ->
                                                        Text(text = "적정레벨 : ${it.rewardItems[idx].expeditionItemLevel}")
                                                        it.rewardItems[idx].items.mapIndexed { index, reward ->
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                AsyncImage(
                                                                    model = reward.icon,
                                                                    contentDescription = "보상 아이콘"
                                                                )
                                                                Text("보상 이름 : ${reward.name}")
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            4 -> {
                                                val indexList = listOf(8,9,10,11,12)
                                                LazyColumn(Modifier.height(250.dp)) {
                                                    items(indexList) { idx ->
                                                        Text(text = "적정레벨 : ${it.rewardItems[idx].expeditionItemLevel}")
                                                        it.rewardItems[idx].items.mapIndexed { index, reward ->
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                AsyncImage(
                                                                    model = reward.icon,
                                                                    contentDescription = "보상 아이콘"
                                                                )
                                                                Text("보상 이름 : ${reward.name}")
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            5 -> {
                                                val indexList = listOf(13)
                                                LazyColumn(Modifier.height(250.dp)) {
                                                    items(indexList) { idx ->
                                                        Text(text = "적정레벨 : ${it.rewardItems[idx].expeditionItemLevel}")
                                                        it.rewardItems[idx].items.mapIndexed { index, reward ->
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                AsyncImage(
                                                                    model = reward.icon,
                                                                    contentDescription = "보상 아이콘"
                                                                )
                                                                Text("보상 이름 : ${reward.name}")
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
@ExperimentalPagerApi
@Composable
fun PageImage(list: SnapshotStateList<newsData>, news: newsData, page: Int, context: Context) {
    if(list.isNullOrEmpty()) {
        Text(text = "이벤트를 불러오는중입니다.")
    } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.clickable(onClick = {
                val urlIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(list[page].link))
                context.startActivity(urlIntent)})) {
                AsyncImage(
                    model = "${news.thumbnail}",
                    contentDescription = "eventImage"
                )
            }

        }   
    }
}


