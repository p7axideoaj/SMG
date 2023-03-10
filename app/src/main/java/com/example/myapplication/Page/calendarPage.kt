package com.example.myapplication.Page

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.*
import com.example.myapplication.dataModel.CalendarData
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
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
    val navScope = rememberCoroutineScope()
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
    var selectText by remember {
        mutableStateOf("")
    }
    Scaffold(
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            Column() {
                TextButton(onClick = {
                    selectText = ""
                    navScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }) {
                    Text(text = "?????? ????????????")
                }
                TextButton(onClick = {
                    selectText = "??????"
                    navScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }) {
                    Text(text = "??????")
                }
                TextButton(onClick = {
                    selectText = "?????? ???"
                    navScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }) {
                    Text(text = "?????? ???")
                }
                TextButton(onClick = {
                    selectText = "???"
                    navScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }) {
                    Text(text = "???")
                }
                TextButton(onClick = {
                    selectText = "?????????"
                    navScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }) {
                    Text(text = "?????????")
                }
                TextButton(onClick = {
                    selectText = "??????????????????"
                    navScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }) {
                    Text(text = "??????????????????")
                }
                TextButton(onClick = {
                    selectText = "????????????"
                    navScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }) {
                    Text(text = "????????????")
                }
                TextButton(onClick = {
                    selectText = "??????"
                    navScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }) {
                    Text(text = "??????")
                }
            }
        },
        topBar = {
        TopAppBar(
            title = {
                Text(text = "????????? ??????")
            },
            navigationIcon = {
                Row() {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.Default.ArrowBack, "backIcon")
                    }
                }
            },
            actions = {
                IconButton(onClick = {
                    navScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {
                    Icon(Icons.Filled.Menu, "backIcon")
                }
            },
            backgroundColor = Color.White,
            elevation = 0.dp
        )
    }) {_ ->
        var show by remember {
            mutableStateOf(false)
        }
        var selectName by remember {
            mutableStateOf("")
        }
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            when(selectText) {
                "??????" -> { item {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .height(400.dp)) {
                        Text(text = "??????")
                        Column() {
                            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                items(calendarList) {
                                    if(it == null) {
                                        Text("????????? ????????? ???????????? ????????????.")
                                    } else {
                                        if(it.categoryName == "??????") {
                                            var now = LocalDateTime.now()
                                            var a = it.startTimes.filter { t ->
                                                var time = LocalDateTime.parse(t, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                                now.dayOfYear == time.dayOfYear && now.isBefore(time)
                                            }
                                            Column(
                                                Modifier
                                                    .fillMaxWidth(0.5f)
                                                    .clickable {
                                                        show = true
                                                        selectName = it.contentsName!!
                                                    }) {
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
                                                            Log.d("????????????1", "${duration}")
                                                        }
                                                        "${if(duration == 0) "????????????????????????" else formatRemain(duration)}"
                                                    }
                                                )
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                } }
                "?????? ???" -> { item {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .height(400.dp)) {
                        Text(text = "?????? ???")
                        Column() {
                            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                items(calendarList) {
                                    if(it == null) {
                                        Text("????????? ????????? ???????????? ????????????.")
                                    } else {
                                        if(it.categoryName == "?????? ???") {
                                            var now = LocalDateTime.now()
                                            var a = it.startTimes.filter { t ->
                                                var time = LocalDateTime.parse(t, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                                now.dayOfYear == time.dayOfYear && now.isBefore(time)
                                            }
                                            Column(Modifier.fillMaxWidth(0.5f).clickable {
                                                show = true
                                                selectName = it.contentsName!!
                                            }) {
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
                                                            Log.d("????????????1", "${duration}")
                                                        }
                                                        "${if(duration == 0) "????????????????????????" else formatRemain(duration)}"
                                                    }
                                                )
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                } }
                "???" -> { item {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .height(400.dp)) {
                        Text(text = "????????? ????????????")
                        Column() {
                            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                items(calendarList) {
                                    if(it == null) {
                                        Text("????????? ????????? ???????????? ????????????.")
                                    } else {
                                        if(it.categoryName == "???") {
                                            var now = LocalDateTime.now()
                                            var a = it.startTimes.filter { t ->
                                                var time = LocalDateTime.parse(t, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                                now.dayOfYear == time.dayOfYear && now.isBefore(time)
                                            }
                                            Column(Modifier.fillMaxWidth(0.5f).clickable {
                                                show = true
                                                selectName = it.contentsName!!
                                            }) {
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
                                                            Log.d("????????????1", "${duration}")
                                                        }
                                                        "${if(duration == 0) "????????????????????????" else formatRemain(duration)}"
                                                    }
                                                )
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                } }
                "?????????" -> { item {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .height(400.dp)) {
                        AsyncImage(model = "https://cdn-lostark.game.onstove.com/EFUI_IconAtlas/achieve/achieve_09_19.png", contentDescription = "?????????")
                        Text(text = "?????????")
                        Column() {
                            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                items(calendarList) {
                                    if(it == null) {
                                        Text("????????? ????????? ???????????? ????????????.")
                                    } else {
                                        if(it.categoryName == "?????????") {
                                            var now = LocalDateTime.now()
                                            var a = it.startTimes.filter { t ->
                                                var time = LocalDateTime.parse(t, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                                now.dayOfYear == time.dayOfYear && now.isBefore(time)
                                            }
                                            Column(Modifier.fillMaxWidth(0.5f).clickable {
                                                show = true
                                                selectName = it.contentsName!!
                                            }) {
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
                                                            Log.d("????????????1", "${duration}")
                                                        }
                                                        "${if(duration == 0) "????????????????????????" else formatRemain(duration)}"
                                                    }
                                                )
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                } }
                "??????????????????" -> { item {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .height(400.dp)) {
                        AsyncImage(model = "https://cdn-lostark.game.onstove.com/EFUI_IconAtlas/achieve/achieve_13_11.png", contentDescription = "??????????????????")
                        Text(text = "??????????????????")
                        Column() {
                            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                items(calendarList) {
                                    if(it == null) {
                                        Text("????????? ????????? ???????????? ????????????.")
                                    } else {
                                        if(it.categoryName == "??????????????????") {
                                            var now = LocalDateTime.now()
                                            var a = it.startTimes.filter { t ->
                                                var time = LocalDateTime.parse(t, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                                now.dayOfYear == time.dayOfYear && now.isBefore(time)
                                            }
                                            Column(Modifier.fillMaxWidth(0.5f).clickable {
                                                show = true
                                                selectName = it.contentsName!!
                                            }) {
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
                                                            Log.d("????????????1", "${duration}")
                                                        }
                                                        "${if(duration == 0) "????????????????????????" else formatRemain(duration)}"
                                                    }
                                                )
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                } }
                "????????????" -> { item {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .height(400.dp)) {
                        AsyncImage(model = "https://cdn-lostark.game.onstove.com/EFUI_IconAtlas/achieve/achieve_14_142.png", contentDescription = "????????????")
                        Text(text = "????????????")
                        Column() {
                            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                items(calendarList) {
                                    if(it == null) {
                                        Text("????????? ????????? ???????????? ????????????.")
                                    } else {
                                        if(it.categoryName == "????????????") {
                                            var now = LocalDateTime.now()
                                            var a = it.startTimes.filter { t ->
                                                var time = LocalDateTime.parse(t, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                                now.dayOfYear == time.dayOfYear && now.isBefore(time)
                                            }
                                            Column(Modifier.fillMaxWidth(0.5f).clickable {
                                                show = true
                                                selectName = it.contentsName!!
                                            }) {
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
                                                            Log.d("????????????1", "${duration}")
                                                        }
                                                        "${if(duration == 0) "????????????????????????" else formatRemain(duration)}"
                                                    }
                                                )
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                } }
                "??????" -> { item {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .height(400.dp)) {
                        AsyncImage(model = "https://cdn-lostark.game.onstove.com/EFUI_IconAtlas/achieve/achieve_06_55.png", contentDescription = "??????")
                        Text(text = "??????")
                        Column() {
                            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                items(calendarList) {
                                    if(it == null) {
                                        Text("????????? ????????? ???????????? ????????????.")
                                    } else {
                                        if(it.categoryName == "??????") {
                                            var now = LocalDateTime.now()
                                            var a = it.startTimes.filter { t ->
                                                var time = LocalDateTime.parse(t, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                                now.dayOfYear == time.dayOfYear && now.isBefore(time)
                                            }
                                            Column(Modifier.fillMaxWidth(0.5f).clickable {
                                                show = true
                                                selectName = it.contentsName!!
                                            }) {
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
                                                            Log.d("????????????1", "${duration}")
                                                        }
                                                        "${if(duration == 0) "????????????????????????" else formatRemain(duration)}"
                                                    }
                                                )
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                } }
                else -> {
                    item {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp)) {
                            Text(text = "??????")
                            Column() {
                                LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                    items(calendarList) {
                                        if(it == null) {
                                            Text("????????? ????????? ???????????? ????????????.")
                                        } else {
                                            if(it.categoryName == "??????") {
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
                                                                Log.d("????????????1", "${duration}")
                                                            }
                                                            "${if(duration == 0) "????????????????????????" else formatRemain(duration)}"
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
                            Text(text = "?????? ???")
                            Column() {
                                LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                    items(calendarList) {
                                        if(it == null) {
                                            Text("????????? ????????? ???????????? ????????????.")
                                        } else {
                                            if(it.categoryName == "?????? ???") {
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
                                                                Log.d("????????????1", "${duration}")
                                                            }
                                                            "${if(duration == 0) "????????????????????????" else formatRemain(duration)}"
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
                            Text(text = "????????? ????????????")
                            Column() {
                                LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                    items(calendarList) {
                                        if(it == null) {
                                            Text("????????? ????????? ???????????? ????????????.")
                                        } else {
                                            if(it.categoryName == "???") {
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
                                                                Log.d("????????????1", "${duration}")
                                                            }
                                                            "${if(duration == 0) "????????????????????????" else formatRemain(duration)}"
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
                            Text(text = "?????????")
                            Column() {
                                LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                    items(calendarList) {
                                        if(it == null) {
                                            Text("????????? ????????? ???????????? ????????????.")
                                        } else {
                                            if(it.categoryName == "?????????") {
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
                                                                Log.d("????????????1", "${duration}")
                                                            }
                                                            "${if(duration == 0) "????????????????????????" else formatRemain(duration)}"
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
                            Text(text = "??????????????????")
                            Column() {
                                LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                    items(calendarList) {
                                        if(it == null) {
                                            Text("????????? ????????? ???????????? ????????????.")
                                        } else {
                                            if(it.categoryName == "??????????????????") {
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
                                                                Log.d("????????????1", "${duration}")
                                                            }
                                                            "${if(duration == 0) "????????????????????????" else formatRemain(duration)}"
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
                            Text(text = "????????????")
                            Column() {
                                LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                    items(calendarList) {
                                        if(it == null) {
                                            Text("????????? ????????? ???????????? ????????????.")
                                        } else {
                                            if(it.categoryName == "????????????") {
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
                                                                Log.d("????????????1", "${duration}")
                                                            }
                                                            "${if(duration == 0) "????????????????????????" else formatRemain(duration)}"
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
                            Text(text = "??????")
                            Column() {
                                LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                    items(calendarList) {
                                        if(it == null) {
                                            Text("????????? ????????? ???????????? ????????????.")
                                        } else {
                                            if(it.categoryName == "??????") {
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
                                                                Log.d("????????????1", "${duration}")
                                                            }
                                                            "${if(duration == 0) "????????????????????????" else formatRemain(duration)}"
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
        if(show) {
            BottomSheetDialog(onDismissRequest = {
                show = false
            }) {
                Surface(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .height(400.dp)) {
                    calendarList.filter { it!!.contentsName!! == selectName }.map {
                        if(it == null) {
                            Text(text = "???????????? ????????????????????????.")
                        } else {
                            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                items(it.rewardItems) { reward ->
                                    Column() {
                                        AsyncImage(model = reward.icon, contentDescription = reward.name)
                                        Text(text = reward.name!!)
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
        return "${seconds / 3600}?????? ???"
    }
    if(seconds > 60) {
        return "${seconds / 60}??? ???"
    }
    return "${seconds}??? ???"
}