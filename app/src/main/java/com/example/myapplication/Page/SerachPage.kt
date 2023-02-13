package com.example.myapplication.Page

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.lostarkapp.retrofitAPI
import com.example.myapplication.dataModel.charterProfile
import com.example.myapplication.dbHelper.DBSearchData
import com.example.myapplication.sharedHelper.sharedHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Composable
fun SerachPageHome(navController: NavController, prefs: sharedHelper, context: Context) {
    Column(modifier = Modifier.fillMaxSize()) {
        SerachHeader(navController)
        listView(context, navController)
    }
}

@Composable
fun SerachHeader(navController: NavController) {
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
        inputName(navController = navController)
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
fun inputName(navController: NavController) {
    var text by remember { mutableStateOf("") }
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
                IconButton(onClick = { navController.navigate("profileDetail/${text}") }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                }
            }
        }
    }
}

