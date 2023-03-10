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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
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
    Log.d("??????", "???")
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
                    Icon(Icons.Default.Notifications, contentDescription = "??????")
                    Text(text = "???????????? ????????????")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    Modifier
                        .clickable { navController.navigate("search") }
                        .padding(start = 10.dp, end = 10.dp)
                        .fillMaxWidth(0.8f)
                        .height(30.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                        .border(
                            width = 1.dp,
                            color = Color.DarkGray,
                            shape = RoundedCornerShape(12.dp)
                        )) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "??????", fontSize = 16.sp, color = Color.DarkGray, modifier = Modifier.padding(start = 10.dp, top = 3.dp))
                        Icon(imageVector = Icons.Default.Search, contentDescription = "???????????????", modifier = Modifier.padding(top = 3.dp, end = 10.dp))
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(0.6f),horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(Modifier.clickable { navController.navigate("markets") }) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "?????????")
                            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "?????????")
                        }
                    }
                    Box(Modifier.clickable { navController.navigate("action") }) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "?????????")
                            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "?????????")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                var name by remember {
                    mutableStateOf(prefs.getString("name"))
                }
                val pagerState = rememberPagerState()
                if(list.isNullOrEmpty()) {
                    Text(text = "???????????? ???????????? ????????????.")
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
                            Text("??? ????????? ????????????")
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
                                    Text(text = "???????????? ?????? ??????????????????.")
                                } else {
                                    serverName = "${it.ServerName?: "??????"}" // ?????? ?????? ????????????
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
                    Icon(Icons.Default.Add, contentDescription = "?????? ?????????")
                    Text(text = "????????? ??????????????????")
                }
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item {Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Text("?????? ??????", Modifier.padding(top = 8.dp))
                TextButton(onClick = { navController.navigate("guildRank") }) {
                    Text("?????????+")
                }
            }}
            if(serverName.isNullOrEmpty()) {
                item { Text(text = "????????? ?????? ??? ?????? ????????????") }
            } else {
                if(guildRankList.isNullOrEmpty()) {
                    item {
                        Text("?????? ????????? ????????????????????????.")
                    }
                } else {
                    item { Text(text = "?????? : ${serverName}")}
                    item {Spacer(modifier = Modifier.height(10.dp))}
                    item { Box(Modifier.fillMaxWidth()) {
                        LazyRow() {
                            items(guildRankList.toList().filter { it!!.rank!! <= 5 }) {
                                if (it == null) {
                                    Text("?????? ????????? ???????????????????????????")
                                } else {
                                        Box(
                                            Modifier
                                                .fillMaxWidth(0.2f)
                                                .clickable {
                                                    lifecycleScope.launch {
                                                        scaffoldState.snackbarHostState.showSnackbar(
                                                            message = "?????? ?????? : ${it.rating}\n?????? : ${it.guildMessage}"
                                                        )
                                                    }
                                                }) {
                                            Column() {
                                                Text(text = "${it.rank}???")
                                                Text(text = "?????? : ${it.guildName}", overflow = TextOverflow.Ellipsis)
                                                Text(text = "????????? : ${it.MasterName}", overflow = TextOverflow.Ellipsis)
                                                Text(text = "?????? : ${it.memberCount} / ${it.maxMemberCount}")
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
                Text("????????? ??????")
            }}
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item {
                abyssRaid(abyssRaidDataList)
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item {Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Text("????????? ??????")
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
            Text(text = "?????? ????????? ????????? ???????????? ????????????")
        } else {
            val imageLoader = LocalContext.current.imageLoader.newBuilder()
                .logger(DebugLogger())
                .build()
            var index by remember { mutableStateOf(-1) }
            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                itemsIndexed(abyssRaidDataList.toList()) { idx, it ->
                    if (it == null) {
                        Text(text = "?????? ????????? ????????? ???????????? ???????????????.")
                    } else {
                        Column(Modifier.fillMaxWidth(0.5f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(Modifier.clickable { index = idx }) {
                                AsyncImage(
                                    model = "${it.image}",
                                    contentDescription = "???????????????",
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
                                                    Text(text = "?????? ????????? ????????? ???????????? ???????????????.")
                                                } else {
                                                    AsyncImage(
                                                        model = "${abyssRaidDataList[index]!!.image}",
                                                        contentDescription = "?????? ?????????",
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(120.dp),
                                                        contentScale = ContentScale.Crop
                                                    )
                                                    Text(text = "?????? : ${abyssRaidDataList[index]!!.name}")
                                                    Text(text = "?????? : ${abyssRaidDataList[index]!!.description}")
                                                    Text(text = "?????? ?????? ?????? : ${abyssRaidDataList[index]!!.minCharacterLevel}")
                                                    Text(text = "?????? ?????? ????????? ?????? : ${abyssRaidDataList[index]!!.minItemLevel}")
                                                    Text(text = "?????? : ${abyssRaidDataList[index]!!.areaName}")
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
                                                                    Text("?????? : ${it.name}")
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
                    Text(text = "????????? ?????? ????????? ???????????? ????????????")
                } else {
                    val imageLoader = LocalContext.current.imageLoader.newBuilder()
                        .logger(DebugLogger())
                        .build()
                    LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        if(guardianRaidDataList[0] == null) {
                            item { Text(text = "????????? ?????? ????????? ???????????? ???????????????") }
                        } else {
                            if(guardianRaidDataList[0]!!.raids.isNullOrEmpty()) {
                                item { Text(text = "????????? ????????? ????????? ???????????? ???????????????") }
                            } else {
                                itemsIndexed(guardianRaidDataList[0]!!.raids!!.toList()) { idx, it ->
                                    if (it == null) {
                                        Text(text = "????????? ?????? ????????? ???????????? ???????????????.")
                                    } else {
                                        Column(Modifier.fillMaxWidth(0.5f), horizontalAlignment = Alignment.CenterHorizontally) {
                                            Box() {
                                                AsyncImage(
                                                    model = "${it.image}",
                                                    contentDescription = "???????????????",
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
                        Text(text = "?????? ??????", Modifier.padding(top = 12.dp))
                    }
                    IconButton(onClick = { expandableState = !expandableState},
                        Modifier
                            .rotate(rotate)) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "??????")
                    }
                }
                AnimatedVisibility(visible = expandableState, Modifier.height(250.dp)) {
                    LazyColumn(Modifier.padding(16.dp)) {
                        item {
                            LazyRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                itemsIndexed(strList) { index, num ->
                                    TextButton(onClick = { selectIndex = index }, modifier = Modifier.size(40.dp), ) {
                                        Text(text = "${num}??????", fontSize = 8.sp, color = Color.Black)
                                    }
                                    Spacer(modifier = Modifier.width(2.dp))
                                }
                            }
                        }
                        if(guardianRaidDataList.isNullOrEmpty()) {
                            item { Text(text = "????????? ?????? ????????? ???????????? ????????????") }
                        } else {
                            items(guardianRaidDataList.toList()) {
                                if(it == null) {
                                    Text(text = "????????? ?????? ????????? ???????????? ???????????????")
                                } else {
                                    if(it.rewardItems != null) {
                                        when(selectIndex) {
                                            0 -> {
                                                val index = 0
                                                Column() {
                                                    Text(text = "???????????? : ${it.rewardItems[index].expeditionItemLevel}")
                                                    it.rewardItems[index].items.mapIndexed { index, reward ->
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            AsyncImage(
                                                                model = reward.icon,
                                                                contentDescription = "?????? ?????????"
                                                            )
                                                            Text("?????? ?????? : ${reward.name}")
                                                        }
                                                    }
                                                }
                                            }
                                            1 -> {
                                                val indexList = listOf(1,2)
                                                LazyColumn(Modifier.height(250.dp)) {
                                                    items(indexList) { idx ->
                                                        Text(text = "???????????? : ${it.rewardItems[idx].expeditionItemLevel}")
                                                        it.rewardItems[idx].items.mapIndexed { index, reward ->
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                AsyncImage(
                                                                    model = reward.icon,
                                                                    contentDescription = "?????? ?????????"
                                                                )
                                                                Text("?????? ?????? : ${reward.name}")
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            2 -> {
                                                val indexList = listOf(3)
                                                LazyColumn(Modifier.height(250.dp)) {
                                                    items(indexList) { idx ->
                                                        Text(text = "???????????? : ${it.rewardItems[idx].expeditionItemLevel}")
                                                        it.rewardItems[idx].items.mapIndexed { index, reward ->
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                AsyncImage(
                                                                    model = reward.icon,
                                                                    contentDescription = "?????? ?????????"
                                                                )
                                                                Text("?????? ?????? : ${reward.name}")
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            3 -> {
                                                val indexList = listOf(4,5,6,7)
                                                LazyColumn(Modifier.height(250.dp)) {
                                                    items(indexList) { idx ->
                                                        Text(text = "???????????? : ${it.rewardItems[idx].expeditionItemLevel}")
                                                        it.rewardItems[idx].items.mapIndexed { index, reward ->
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                AsyncImage(
                                                                    model = reward.icon,
                                                                    contentDescription = "?????? ?????????"
                                                                )
                                                                Text("?????? ?????? : ${reward.name}")
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            4 -> {
                                                val indexList = listOf(8,9,10,11,12)
                                                LazyColumn(Modifier.height(250.dp)) {
                                                    items(indexList) { idx ->
                                                        Text(text = "???????????? : ${it.rewardItems[idx].expeditionItemLevel}")
                                                        it.rewardItems[idx].items.mapIndexed { index, reward ->
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                AsyncImage(
                                                                    model = reward.icon,
                                                                    contentDescription = "?????? ?????????"
                                                                )
                                                                Text("?????? ?????? : ${reward.name}")
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            5 -> {
                                                val indexList = listOf(13)
                                                LazyColumn(Modifier.height(250.dp)) {
                                                    items(indexList) { idx ->
                                                        Text(text = "???????????? : ${it.rewardItems[idx].expeditionItemLevel}")
                                                        it.rewardItems[idx].items.mapIndexed { index, reward ->
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                AsyncImage(
                                                                    model = reward.icon,
                                                                    contentDescription = "?????? ?????????"
                                                                )
                                                                Text("?????? ?????? : ${reward.name}")
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
        Text(text = "???????????? ????????????????????????.")
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


