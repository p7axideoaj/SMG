package com.example.myapplication.Page

import android.content.Context
import android.content.Intent
import android.net.Uri

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.myapplication.dataModel.newsData
import com.example.myapplication.dataModel.charterProfile
import com.example.myapplication.dataModel.guildData
import com.example.myapplication.getJSONGuildRankList
import com.example.myapplication.getJSONNewsDate
import com.example.myapplication.getJSONProfile
import com.example.myapplication.sharedHelper.sharedHelper
import com.google.accompanist.pager.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import java.time.LocalDateTime

//import okhttp3.logging.HttpLoggingInterceptor

val timer = LocalDateTime.now()
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
                    composable("guildRank") { GuildRankPage(navController) }
                    composable("addUser") { addUser(navController, prefs) }
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
                delay(3000)
            }
        }
        onDispose {
            scope.cancel()
        }
    }

    Scaffold(scaffoldState = scaffoldState) {
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
                var name by remember {
                    mutableStateOf(prefs.getString("name"))
                }
                val pagerState = rememberPagerState()
                HorizontalPager(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth(), count = list.size, verticalAlignment = Alignment.Top, state = pagerState) { page ->
                    PageImage(list, list[page], page, context)
                }
                Spacer(modifier = Modifier.height(3.dp))
                HorizontalPagerIndicator(pagerState = pagerState, Modifier.padding(5.dp))
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
        }
    }
}

@ExperimentalPagerApi
@Composable
fun PageImage(list: SnapshotStateList<newsData>, news: newsData, page: Int, context: Context) {
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


