package com.example.myapplication.Page

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.dataModel.NewsNotiData
import com.example.myapplication.getJSONNewsNoti
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NotiPage(navController: NavController, context: Context) {
    val title = listOf("공지", "점검","상점", "이벤트")
    var index by remember { mutableStateOf(0) }
    val scope = MainScope()
    val notiList = remember {
        mutableStateListOf<NewsNotiData>()
    }
    DisposableEffect(0){
        scope.launch {
            while (true) {
                getJSONNewsNoti(notiList)
                delay(3000)
            }
        }
        onDispose {
            scope.cancel()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "공지")
                },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.Default.ArrowBack, "backIcon")
                    }
                },
                backgroundColor = Color.White,
                elevation = 0.dp
            )
        }
    ) { _ ->
        LazyColumn() {
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    title.mapIndexed { i, s ->
                        TextButton(onClick = { index = i}) {
                            Text(text = s)
                        }
                    }
                }
            }
            items(notiList.toList()) {
                if(it.Type == title[index]) {
                    Card(Modifier.clickable { val urlIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(it.link))
                        context.startActivity(urlIntent) }) {
                        Column() {
                            Text(text = it.title)
                            Text(text = it.Date)
                        }
                    }
                }
            }
        }
    }
}
