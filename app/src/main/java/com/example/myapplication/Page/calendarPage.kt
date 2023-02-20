package com.example.myapplication.Page

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.*
import com.example.myapplication.dataModel.CalendarData
import com.google.accompanist.pager.VerticalPager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CalendarPage(navController: NavController, context: Context) {
    var calendarList = remember {
        mutableStateListOf<CalendarData?>()
    }
    val scope = MainScope()
    val scaffoldState = rememberScaffoldState()
    DisposableEffect(0){
        scope.launch {
            while (true) {
                getJSONCalendar(calendarList)
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
                Text(text = "이벤트 일정")
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
                Column(Modifier.fillMaxWidth().height(200.dp)) {
                    Text(text = "로웬")
                    Column() {
                        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            items(calendarList) {
                                if(it == null) {
                                    Text("이벤트 일정을 불러오는 중입니다.")
                                } else {
                                    if(it.categoryName == "로웬") {
                                        Column(Modifier.fillMaxWidth(0.5f)) {
                                            Box() {
                                                AsyncImage(model = "${it.contentsIcon}", contentDescription = "${it.contentsName}")
                                                Text(text = "${it.contentsName}")
                                            }
                                            Text(it.location?: "")
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
            item {
                Column(Modifier.fillMaxWidth().height(200.dp)) {
                    Text(text = "모험 섬")
                    Column() {
                        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            items(calendarList) {
                                if(it == null) {
                                    Text("이벤트 일정을 불러오는 중입니다.")
                                } else {
                                    if(it.categoryName == "모험 섬") {
                                        Column(Modifier.fillMaxWidth(0.5f)) {
                                            Box() {
                                                AsyncImage(model = "${it.contentsIcon}", contentDescription = "${it.contentsName}")
                                                Text(text = "${it.contentsName}")
                                            }
                                            Text(it.location?: "")
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
            item {
                Column(Modifier.fillMaxWidth().height(200.dp)) {
                    Text(text = "오늘의 캘린더섬")
                    Column() {
                        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            items(calendarList) {
                                if(it == null) {
                                    Text("이벤트 일정을 불러오는 중입니다.")
                                } else {
                                    if(it.categoryName == "섬") {
                                        Column(Modifier.fillMaxWidth(0.5f)) {
                                            Box() {
                                                AsyncImage(model = "${it.contentsIcon}", contentDescription = "${it.contentsName}")
                                                Text(text = "${it.contentsName}")
                                            }
                                            Text(it.location?: "")
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
            item {
                Column(Modifier.fillMaxWidth().height(200.dp)) {
                    Text(text = "유령선")
                    Column() {
                        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            items(calendarList) {
                                if(it == null) {
                                    Text("이벤트 일정을 불러오는 중입니다.")
                                } else {
                                    if(it.categoryName == "유령선") {
                                        Column(Modifier.fillMaxWidth(0.5f)) {
                                            Box() {
                                                AsyncImage(model = "${it.contentsIcon}", contentDescription = "${it.contentsName}")
                                                Text(text = "${it.contentsName}")
                                            }
                                            Text(it.location?: "")
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
            item {
                Column(Modifier.fillMaxWidth().height(200.dp)) {
                    Text(text = "카오스게이트")
                    Column() {
                        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            items(calendarList) {
                                if(it == null) {
                                    Text("이벤트 일정을 불러오는 중입니다.")
                                } else {
                                    if(it.categoryName == "카오스게이트") {
                                        Column(Modifier.fillMaxWidth(0.5f)) {
                                            Box() {
                                                AsyncImage(model = "${it.contentsIcon}", contentDescription = "${it.contentsName}")
                                                Text(text = "${it.contentsName}")
                                            }
                                            Text(it.location?: "")
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
            item {
                Column(Modifier.fillMaxWidth().height(200.dp)) {
                    Text(text = "필드보스")
                    Column() {
                        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            items(calendarList) {
                                if(it == null) {
                                    Text("이벤트 일정을 불러오는 중입니다.")
                                } else {
                                    if(it.categoryName == "필드보스") {
                                        Column(Modifier.fillMaxWidth(0.5f)) {
                                            Box() {
                                                AsyncImage(model = "${it.contentsIcon}", contentDescription = "${it.contentsName}")
                                                Text(text = "${it.contentsName}")
                                            }
                                            Text(it.location?: "")
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
            item {
                Column(Modifier.fillMaxWidth().height(200.dp)) {
                    Text(text = "항해")
                    Column() {
                        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            items(calendarList) {
                                if(it == null) {
                                    Text("이벤트 일정을 불러오는 중입니다.")
                                } else {
                                    if(it.categoryName == "항해") {
                                        Column(Modifier.fillMaxWidth(0.5f)) {
                                            Box() {
                                                AsyncImage(model = "${it.contentsIcon}", contentDescription = "${it.contentsName}")
                                                Text(text = "${it.contentsName}")
                                            }
                                            Text(it.location?: "")
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
