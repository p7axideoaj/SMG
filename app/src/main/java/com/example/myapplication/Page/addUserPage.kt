package com.example.myapplication.Page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.myapplication.sharedHelper.sharedHelper

@Composable
fun addUser(navController: NavController, prefs: sharedHelper) {
    val textState = remember {
        mutableStateOf("");
    }
    Column(Modifier.fillMaxSize()) {
        Text(text = "모험가 등록하기")
        Row() {
            TextField(value = textState.value, onValueChange = { textValue -> textState.value = textValue})
            TextButton(onClick = {
                prefs.setString("name", textState.value)
                navController.navigate("home")
            }) {
                Text(text = "등록하기")
            }
        }

    }
}