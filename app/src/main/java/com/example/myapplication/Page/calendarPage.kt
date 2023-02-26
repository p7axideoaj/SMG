package com.example.myapplication.Page

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.*
import com.example.myapplication.dataModel.CalendarData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.timer

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
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)) {
                    Text(text = "로웬")
                    Column() {
                        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            items(calendarList) {
                                if(it == null) {
                                    Text("이벤트 일정을 불러오는 중입니다.")
                                } else {
                                    if(it.categoryName == "로웬") {
                                        var now = LocalDateTime.now()
                                        var a = it.startTimes.filter { t ->
                                            var time = LocalDateTime.parse(t, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                            now.dayOfYear == time.dayOfYear && now.isBefore(time)
                                        }
                                        Column(Modifier.fillMaxWidth(0.5f)) {
                                            Box() {
                                                AsyncImage(model = "${it.contentsIcon}", contentDescription = "${it.contentsName}")
                                            }
                                            Text(text = "${it.contentsName}")
                                            Text(it.location?: "")
                                            Text(if(a.isEmpty()) "" else LocalDateTime.parse(a[0]!!, DateTimeFormatter.ISO_LOCAL_DATE_TIME).format(DateTimeFormatter.ofPattern("MM-dd HH:mm")))
                                            Text(
                                                if(a.isEmpty()) {
                                                    ""
                                                } else {
                                                    var duration by remember {
                                                        mutableStateOf(0)
                                                    }
                                                    timer(period = 1000, initialDelay = 1000) {
                                                        var now = LocalDateTime.now()
                                                        var time = LocalDateTime.parse(a[0]!!, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                                        duration = Duration.between(now, time).seconds.toInt()
                                                        Log.d("듀레듀레1", "${duration}")
                                                    }
                                                    "${if(duration == 0) "불러오는중입니다" else formatRemain(duration)}"
                                                }
                                            )
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)) {
                    Text(text = "모험 섬")
                    Column() {
                        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            items(calendarList) {
                                if(it == null) {
                                    Text("이벤트 일정을 불러오는 중입니다.")
                                } else {
                                    if(it.categoryName == "모험 섬") {
                                        var now = LocalDateTime.now()
                                        var a = it.startTimes.filter { t ->
                                            var time = LocalDateTime.parse(t, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                            now.dayOfYear == time.dayOfYear && now.isBefore(time)
                                        }
                                        Column(Modifier.fillMaxWidth(0.5f)) {
                                            Box() {
                                                AsyncImage(model = "${it.contentsIcon}", contentDescription = "${it.contentsName}")
                                            }
                                            Text(text = "${it.contentsName}")
                                            Text(it.location?: "")
                                            Text(if(a.isEmpty()) "" else LocalDateTime.parse(a[0]!!, DateTimeFormatter.ISO_LOCAL_DATE_TIME).format(DateTimeFormatter.ofPattern("MM-dd HH:mm")))
                                            Text(
                                                if(a.isEmpty()) {
                                                    ""
                                                } else {
                                                    var duration by remember {
                                                        mutableStateOf(0)
                                                    }
                                                    timer(period = 1000, initialDelay = 1000) {
                                                        var now = LocalDateTime.now()
                                                        var time = LocalDateTime.parse(a[0]!!, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                                        duration = Duration.between(now, time).seconds.toInt()
                                                        Log.d("듀레듀레1", "${duration}")
                                                    }
                                                    "${if(duration == 0) "불러오는중입니다" else formatRemain(duration)}"
                                                }
                                            )
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)) {
                    Text(text = "오늘의 캘린더섬")
                    Column() {
                        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            items(calendarList) {
                                if(it == null) {
                                    Text("이벤트 일정을 불러오는 중입니다.")
                                } else {
                                    if(it.categoryName == "섬") {
                                        var now = LocalDateTime.now()
                                        var a = it.startTimes.filter { t ->
                                            var time = LocalDateTime.parse(t, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                            now.dayOfYear == time.dayOfYear && now.isBefore(time)
                                        }
                                        Column(Modifier.fillMaxWidth(0.5f)) {
                                            Box() {
                                                AsyncImage(model = "${it.contentsIcon}", contentDescription = "${it.contentsName}")
                                            }
                                            Text(text = "${it.contentsName}")
                                            Text(it.location?: "")
                                            Text(if(a.isEmpty()) "" else LocalDateTime.parse(a[0]!!, DateTimeFormatter.ISO_LOCAL_DATE_TIME).format(DateTimeFormatter.ofPattern("MM-dd HH:mm")))
                                            Text(
                                                if(a.isEmpty()) {
                                                    ""
                                                } else {
                                                    var duration by remember {
                                                        mutableStateOf(0)
                                                    }
                                                    timer(period = 1000, initialDelay = 1000) {
                                                        var now = LocalDateTime.now()
                                                        var time = LocalDateTime.parse(a[0]!!, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                                        duration = Duration.between(now, time).seconds.toInt()
                                                        Log.d("듀레듀레1", "${duration}")
                                                    }
                                                    "${if(duration == 0) "불러오는중입니다" else formatRemain(duration)}"
                                                }
                                            )
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)) {
                    Text(text = "유령선")
                    Column() {
                        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            items(calendarList) {
                                if(it == null) {
                                    Text("이벤트 일정을 불러오는 중입니다.")
                                } else {
                                    if(it.categoryName == "유령선") {
                                        var now = LocalDateTime.now()
                                        var a = it.startTimes.filter { t ->
                                            var time = LocalDateTime.parse(t, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                            now.dayOfYear == time.dayOfYear && now.isBefore(time)
                                        }
                                        Column(Modifier.fillMaxWidth(0.5f)) {
                                            Box() {
                                                AsyncImage(model = "${it.contentsIcon}", contentDescription = "${it.contentsName}")
                                            }
                                            Text(text = "${it.contentsName}")
                                            Text(it.location?: "")
                                            Text(if(a.isEmpty()) "" else LocalDateTime.parse(a[0]!!, DateTimeFormatter.ISO_LOCAL_DATE_TIME).format(DateTimeFormatter.ofPattern("MM-dd HH:mm")))
                                            Text(
                                                if(a.isEmpty()) {
                                                    ""
                                                } else {
                                                    var duration by remember {
                                                        mutableStateOf(0)
                                                    }
                                                    timer(period = 1000, initialDelay = 1000) {
                                                        var now = LocalDateTime.now()
                                                        var time = LocalDateTime.parse(a[0]!!, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                                        duration = Duration.between(now, time).seconds.toInt()
                                                        Log.d("듀레듀레1", "${duration}")
                                                    }
                                                    "${if(duration == 0) "불러오는중입니다" else formatRemain(duration)}"
                                                }
                                            )
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)) {
                    Text(text = "카오스게이트")
                    Column() {
                        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            items(calendarList) {
                                if(it == null) {
                                    Text("이벤트 일정을 불러오는 중입니다.")
                                } else {
                                    if(it.categoryName == "카오스게이트") {
                                        var now = LocalDateTime.now()
                                        var a = it.startTimes.filter { t ->
                                            var time = LocalDateTime.parse(t, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                            now.dayOfYear == time.dayOfYear && now.isBefore(time)
                                        }
                                        Column(Modifier.fillMaxWidth(0.5f)) {
                                            Box() {
                                                AsyncImage(model = "${it.contentsIcon}", contentDescription = "${it.contentsName}")
                                            }
                                            Text(text = "${it.contentsName}")
                                            Text(it.location?: "")
                                            Text(if(a.isEmpty()) "" else LocalDateTime.parse(a[0]!!, DateTimeFormatter.ISO_LOCAL_DATE_TIME).format(DateTimeFormatter.ofPattern("MM-dd HH:mm")))
                                            Text(
                                                if(a.isEmpty()) {
                                                    ""
                                                } else {
                                                    var duration by remember {
                                                        mutableStateOf(0)
                                                    }
                                                    timer(period = 1000, initialDelay = 1000) {
                                                        var now = LocalDateTime.now()
                                                        var time = LocalDateTime.parse(a[0]!!, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                                        duration = Duration.between(now, time).seconds.toInt()
                                                        Log.d("듀레듀레1", "${duration}")
                                                    }
                                                    "${if(duration == 0) "불러오는중입니다" else formatRemain(duration)}"
                                                }
                                            )
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)) {
                    Text(text = "필드보스")
                    Column() {
                        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            items(calendarList) {
                                if(it == null) {
                                    Text("이벤트 일정을 불러오는 중입니다.")
                                } else {
                                    if(it.categoryName == "필드보스") {
                                        var now = LocalDateTime.now()
                                        var a = it.startTimes.filter { t ->
                                            var time = LocalDateTime.parse(t, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                            now.dayOfYear == time.dayOfYear && now.isBefore(time)
                                        }
                                        Column(Modifier.fillMaxWidth(0.5f)) {
                                            Box() {
                                                AsyncImage(model = "${it.contentsIcon}", contentDescription = "${it.contentsName}")
                                            }
                                            Text(text = "${it.contentsName}")
                                            Text(it.location?: "")
                                            Text(if(a.isEmpty()) "" else LocalDateTime.parse(a[0]!!, DateTimeFormatter.ISO_LOCAL_DATE_TIME).format(DateTimeFormatter.ofPattern("MM-dd HH:mm")))
                                            Text(
                                                if(a.isEmpty()) {
                                                    ""
                                                } else {
                                                    var duration by remember {
                                                        mutableStateOf(0)
                                                    }
                                                    timer(period = 1000, initialDelay = 1000) {
                                                        var now = LocalDateTime.now()
                                                        var time = LocalDateTime.parse(a[0]!!, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                                        duration = Duration.between(now, time).seconds.toInt()
                                                        Log.d("듀레듀레1", "${duration}")
                                                    }
                                                    "${if(duration == 0) "불러오는중입니다" else formatRemain(duration)}"
                                                }
                                            )
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)) {
                    Text(text = "항해")
                    Column() {
                        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            items(calendarList) {
                                if(it == null) {
                                    Text("이벤트 일정을 불러오는 중입니다.")
                                } else {
                                    if(it.categoryName == "항해") {
                                        var now = LocalDateTime.now()
                                        var a = it.startTimes.filter { t ->
                                            var time = LocalDateTime.parse(t, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                            now.dayOfYear == time.dayOfYear && now.isBefore(time)
                                        }
                                        Column(Modifier.fillMaxWidth(0.5f)) {
                                            Box() {
                                                AsyncImage(model = "${it.contentsIcon}", contentDescription = "${it.contentsName}")
                                            }
                                            Text(text = "${it.contentsName}")
                                            Text(it.location?: "")
                                            Text(if(a.isEmpty()) "" else LocalDateTime.parse(a[0]!!, DateTimeFormatter.ISO_LOCAL_DATE_TIME).format(DateTimeFormatter.ofPattern("MM-dd HH:mm")))
                                            Text(
                                                if(a.isEmpty()) {
                                                    ""
                                                } else {
                                                    var duration by remember {
                                                        mutableStateOf(0)
                                                    }
                                                    timer(period = 1000, initialDelay = 1000) {
                                                        var now = LocalDateTime.now()
                                                        var time = LocalDateTime.parse(a[0]!!, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                                        duration = Duration.between(now, time).seconds.toInt()
                                                        Log.d("듀레듀레1", "${duration}")
                                                    }
                                                    "${if(duration == 0) "불러오는중입니다" else formatRemain(duration)}"
                                                }
                                            )
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
fun formatRemain(seconds: Int):String {
    if(seconds > 3600) {
        return "${seconds / 3600}시간 전"
    }
    if(seconds > 60) {
        return "${seconds / 60}분 전"
    }
    return "${seconds}초 전"
}