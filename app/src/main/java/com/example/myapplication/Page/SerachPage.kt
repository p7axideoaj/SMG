package com.example.myapplication.Page

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.*
import com.example.myapplication.dataModel.charterProfile
import com.example.myapplication.dbHelper.DBSearchData
import com.example.myapplication.sharedHelper.sharedHelper
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SerachPageHome(navController: NavController, prefs: sharedHelper, context: Context) {
    Column(modifier = Modifier.fillMaxSize()) {
        SerachHeader(navController, context)
        listView(context, navController)
    }
}

@Composable
fun SerachHeader(navController: NavController, context: Context) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(70.dp)) {
        IconButton(onClick = { navController.navigate("home") { popUpTo("search"){
            inclusive = true
            saveState = true
        } } }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
        }
        inputName(navController = navController, context)
    }
}
@Composable
fun listView(context: Context, navController: NavController) {
    val db: DBSearchData = DBSearchData(context);

    var list by remember {
        mutableStateOf(db.selectData())
    }
    Log.d("LongLong", "${db.selectTest()}")
    Log.d("list", "${list}")

    if(list.isNullOrEmpty() || list.size <= 0) {
        Text(text = "검색 결과가 없습니다.", color = Color.Black)
    } else {
        LazyColumn(verticalArrangement = Arrangement.Bottom){
            itemsIndexed(list) { _, item ->
                Box(Modifier.clickable { navController.navigate("profileDetail/${item.name}") }) {
                    Row {
                        Box(
                            Modifier
                                .width(50.dp)
                                .height(60.dp)
                        ) {
                            AsyncImage(model = "${item.image}", contentDescription = null)
                        }
                        Box(Modifier.width(10.dp))
                        Box(
                            Modifier
                                .height(60.dp)
                                .width(200.dp)) {
                            Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
                                Text(text = item.name, color = Color.Black, fontSize = 14.sp)
                                Text(text = item.title, color = Color.Black, fontSize = 10.sp)
                            }
                        }
                        IconButton(onClick = {
                            db.deleteData(item.name)
                            list = db.selectData()
                        }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
                        }
                    }

                }
                Box(Modifier.height(1.dp))
            }

        }
    }
}
@Composable
fun inputName(navController: NavController, context: Context) {
    var userlist = remember {
        mutableStateListOf<charterProfile?>()
    }
    val scope = MainScope()
    var text by remember { mutableStateOf("") }
    DisposableEffect(0){
        scope.launch {
            while (true) {
                getJSONProfile(userlist, context, text)
                delay(3000)
            }
        }
        onDispose {
            scope.cancel()
        }
    }
    var index by remember {
        mutableStateOf(-1)
    }
    var flag by remember { mutableStateOf(true) }
    Box() {
        Row() {
            Box(
                Modifier
                    .width(if (flag) 300.dp else 230.dp)
                    .height(50.dp)) {
                TextField(value = text, onValueChange = {
                    text = it
                    flag = it == ""
                }, label = { Text(text = "유저 검색")})
            }
            if (flag) {
            } else {
                IconButton(onClick = {
                    if(userlist.isNullOrEmpty()) {
                        index = 0
                    } else {
                        navController.navigate("profileDetail/${text}")
                    }
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                }
            }
        }
    }
}

