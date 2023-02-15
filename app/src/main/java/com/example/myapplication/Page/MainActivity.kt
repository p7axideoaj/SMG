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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.myapplication.dataModel.newsData
import com.example.myapplication.dataModel.charterProfile
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
                    composable("home") { Homepage(navController, context, prefs) }
                    composable("search") { SerachPageHome(navController, prefs, context) }
                    composable("noti") { NotiPage(navController, context)}
                    composable("profileDetail/{username}") { it ->
                        it.arguments?.getString("username")
                            ?.let { it1 -> ProfileDetailHome(navController, it1, context, lifecycleScope)}
                    }
                    composable("addUser") { addUser(navController, prefs) }
                }

        }
    }
}

@ExperimentalPagerApi
@Composable
fun Homepage(navController: NavController, context: Context, prefs: sharedHelper) {
    Log.d("아아", "아")
    val list = remember {
        mutableStateListOf<newsData>()
    }
    val userlist = remember {
        mutableStateListOf<charterProfile?>()
    }
    val scope = MainScope()
    DisposableEffect(0){
        scope.launch {
            while (true) {
                getJSONNewsDate(list, context)
                prefs.getString("name")?.let { getJSONProfile(userlist, context, it) }
                delay(3000)
            }
        }
        onDispose {
            scope.cancel()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            , horizontalAlignment = Alignment.CenterHorizontally) {
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
        HorizontalPagerIndicator(pagerState = pagerState, Modifier.padding(5.dp))
        if (name.isNullOrEmpty()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.CenterHorizontally)) {
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
                    .height(100.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                LazyRow() {
                    items(userlist) {
                        if(it == null) {
                            Text(text = "등록되지 않은 모험가입니다.")
                        } else {
                            AsyncImage(model = "${it.CharacterImage}", contentDescription = "image")
                            Column() {
                                Text(text = "${it.CharacterName}")
                                Text(text = "${if(it.Title.isNullOrEmpty()) "" else it.Title}")
                            }
                            IconButton(onClick = {
                                prefs.remove("name")
                                name = prefs.getString("name")
                            }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
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


