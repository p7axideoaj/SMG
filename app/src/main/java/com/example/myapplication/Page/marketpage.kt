package com.example.myapplication.Page

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.getJSONGuildRankList
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun MarketPage(navController: NavHostController) {
    var itemName by remember {
        mutableStateOf("")
    }
    var ItemTier by remember {
        mutableStateOf(null)
    }
    var ItemGrade by remember {
        mutableStateOf(null)
    }
    var characterClass by remember {
        mutableStateOf(null)
    }
    var categoryCode by remember {
        mutableStateOf(0)
    }
    val sort = "GRADE"
    val pageNo = "1"
    val sortCondition = "ASC"
    val scope = MainScope()
    DisposableEffect(0){
        scope.launch {
            while (true) {
                delay(3000)
            }
        }
        onDispose {
            scope.cancel()
        }
    }
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "경매장")},
            navigationIcon = {
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(Icons.Default.ArrowBack, "backIcon")
                }
            },
            backgroundColor = Color.White,
            elevation = 0.dp)
    }) {
        LazyColumn() {

        }
    }
}
@Composable
fun MarketHeader() {

}