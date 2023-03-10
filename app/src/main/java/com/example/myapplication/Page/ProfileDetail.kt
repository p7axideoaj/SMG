package com.example.myapplication.Page

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.*
import com.example.myapplication.R
import com.example.myapplication.dataModel.*
import com.example.myapplication.dbHelper.DBSearchData
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneOffset
import androidx.compose.runtime.getValue
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@ExperimentalPagerApi
@Composable
fun ProfileDetailHome(
    navController: NavHostController,
    username: String,
    context: Context,
    lifecycleScope: LifecycleCoroutineScope
) {

    val list = remember {
        mutableStateListOf<charterProfile?>()
    }
    val siblingList = remember {
        mutableStateListOf<SiblingsData>()
    }
    val charArr = remember {
        mutableListOf<List<charterProfile?>>()
    }
//    var url:String
    val scope = MainScope()
    DisposableEffect(0){
        scope.launch {
            while (true) {
                getJSONChaterSiblings(siblingList, username)
                getJSONProfile(list, context, username)
                delay(2000)
                for(i in 0 until siblingList.size) {
                    if(username != siblingList[i].characterName) {
                        if(charArr.size != siblingList.size) {
                            charArr.add(getJSONProfileRetrunList(siblingList[i].characterName))
                        }
                        Log.d("????????????", "${charArr}")
                    }
                }
                delay(3000)
            }
        }
        onDispose {
            scope.cancel()
            charArr.clear()
        }
    }
    val db: DBSearchData = DBSearchData(context);
    val str = db.selectDataByName(username);
    db.deleteData(str?.name)
    list.map {
        db.addSearchData(searchData = SearchData(time = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),name = it!!.CharacterName, title = it!!.Title?: "", image = it!!.CharacterImage?: ""))
    }
    val scaffoldState = rememberScaffoldState()
    var show by remember {
        mutableStateOf(false)
    }
    Scaffold(
        scaffoldState = scaffoldState,
    ) {
        LazyColumn() {
            items(list) {
                if(it != null) {
                    Box(
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.DarkGray)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                            Column() {
                                Box(
                                    Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(Color.Black), contentAlignment = Alignment.Center) {
                                    if(it.CharacterClassName.isEmpty()) {
                                        null
                                    } else {
                                        val className = when (it.CharacterClassName) {
                                            "????????????" -> R.drawable.arcana
                                            "??????" -> R.drawable.bard
                                            "???????????????" ->R.drawable.battle_master
                                            "?????????" -> R.drawable.berserker
                                            "????????????" -> R.drawable.blade
                                            "????????????" -> R.drawable.blaster
                                            "?????????" -> R.drawable.demonic
                                            "??????????????????" -> R.drawable.destroyed
                                            "????????????" -> R.drawable.devil_hunter
                                            "?????????" -> R.drawable.drawing_artist
                                            "????????????" -> R.drawable.gunslinger
                                            "????????????" -> R.drawable.hawkeye
                                            "???????????????" -> R.drawable.holy_night
                                            "????????????" -> R.drawable.meteorologist
                                            "????????????" -> R.drawable.infight
                                            "??????" -> R.drawable.reaper
                                            "????????????" -> R.drawable.scouter
                                            "????????????" -> R.drawable.slayer
                                            "????????????" -> R.drawable.sorceress
                                            "?????????" -> R.drawable.spearman
                                            "???????????????" -> R.drawable.striker
                                            "?????????" -> R.drawable.summoner
                                            "?????????" -> R.drawable.technician
                                            "?????????" -> R.drawable.warlord1
                                            else -> null
                                        }
                                        className?.let { it1 ->
                                            Icon(
                                                painter = painterResource(id = it1),
                                                contentDescription = "?????? ?????????",
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(CircleShape)
                                                    .background(Color.White)
                                            )
                                        }

                                    }

                                }
                                Button(
                                    modifier = Modifier
                                        .width(50.dp)
                                        .height(30.dp),
                                    onClick = {
                                        charArr.forEach { charA ->
                                            if(!charA.isNullOrEmpty()) {
                                                show = true
                                            }
                                        }
                                    }
                                ) {
                                    Text(
                                        text = "????????? ??????", fontSize = 4.sp
                                    )
                                }
                            }
                            Column(Modifier.width(200.dp)) {
                                Text("????????? ?????? : ${it.CharacterName}")
                                Text("?????? : ${it.Title?: ""}")
                                Text("????????? ?????? : Lv.${it.CharacterLevel}")
                                Text("??????????????? ?????? : Lv.${it.ItemMaxLevel}")
                                Text("??????????????? ?????? : Lv.${it.ItemAvgLevel}")
                            }
                        }
                    }
                    profileContent(it, context, username, scaffoldState, lifecycleScope, navController)
                }
            }
        }
        if(show) {
            BottomSheetDialog(onDismissRequest = {
                show = false
            }) {
                Surface(Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .background(Color.White)
                    .padding(16.dp)
                    .clip(shape = RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp))) {
                    Box(Modifier
                        .fillMaxWidth()
                        .height(350.dp)) {
                        Spacer(modifier = Modifier.height(15.dp))
                        if(charArr.size == 0 || charArr.isNullOrEmpty()) {
                            Text("????????? ????????????????????????.")
                        } else {
                            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                                list.forEach {
                                    if (it != null) {
                                        Log.d("??????????????????????????????","${charArr.toList()}")
                                        itemsIndexed(
                                            siblingList.toList()
                                                .filter { s -> it.CharacterName != s.characterName }) { i, s ->
                                            Box(Modifier.clickable { navController.navigate("profileDetail/${s.characterName}") }) {
                                                Column {
                                                    if (charArr[i].first()!!.CharacterImage == null) {
                                                        Box() {
                                                            Text("????????? ??????")
                                                        }
                                                    } else {
                                                        AsyncImage(
                                                            model = "${charArr[i].first()!!.CharacterImage!!}",
                                                            contentDescription = "????????? ?????????"
                                                        )
                                                    }
                                                    Text(
                                                        "??????????????? : ${s.characterName}",
                                                        fontSize = 8.sp
                                                    )
                                                    Text("???????????? : ${s.serverName}", fontSize = 8.sp)
                                                    Text(
                                                        "${s.characterClassName} Lv.${s.characterLevel}",
                                                        fontSize = 8.sp
                                                    )
                                                    Text(
                                                        "??????????????? : ${s.itemAvgLevel}",
                                                        fontSize = 8.sp
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
    }
}
@OptIn(ExperimentalMaterialApi::class)
@ExperimentalPagerApi
@Composable
fun profileContent(
    it: charterProfile,
    context: Context,
    username: String,
    scaffoldState: ScaffoldState,
    lifecycleScope: LifecycleCoroutineScope,
    navController: NavHostController
) {
    // it : ????????? ??????
    // ?????? ??????
    val skillslist = remember {
        mutableStateListOf<charterCombatSkills?>()
    }
    // ????????? ??????
    val avatarslist = remember {
        mutableStateListOf<charterAvatars?>()
    }
    // ?????? ??????
    val equipmentlist = remember {
        mutableStateListOf<charterEquipment?>()
    }
    // ?????? ??????
    val cardslist = remember {
        mutableStateOf<charterCards?>(null)
    }
    // ????????? ??????
    val collectibleslist = remember {
        mutableStateListOf<charterCollectibles?>()
    }
    // ????????? ??????
    val colosseumslist = remember {
        mutableStateListOf<charterColosseums?>()
    }
    // ?????? ??????
    val engravingslist = remember {
        mutableStateListOf<charterEngravings?>()
    }
    // ?????? ??????
    val gemslist = remember {
        mutableStateListOf<charterGems?>()
    }

    val scope = MainScope()
    DisposableEffect(0){
        scope.launch {
            while (true) {

                getJSONProfileEquipment(equipmentlist,context,username)
                getJSONProfileAvatars(avatarslist, context, username)
                getJSONProfileCollectibles(collectibleslist,context,username)
                getJSONProfileColosseums(colosseumslist,context,username)
                getJSONProfileEngravings(engravingslist,context,username)
                getJSONProfileGems(gemslist,context,username)
                getJSONProfileCards(cardslist, context, username)
                getJSONProfileCombatSkills(skillslist,context,username)
                delay(6000)
            }
        }
        onDispose {
            scope.cancel()
        }
    }
    val pages = listOf("????????? ??????", "?????????", "??????", "?????????")
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    Column() {
        Box(
            Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(16.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(10.dp))) {
            Column() {
                TabRow(selectedTabIndex = pagerState.currentPage, indicator = {
                        tabPositions -> TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(pagerState, tabPositions))
                }) {
                    pages.forEachIndexed { index, s ->  Tab(selected = pagerState.currentPage == index, onClick = {
                        coroutineScope.launch {
                            pagerState.scrollToPage(index)
                        }
                    }){
                        Text(text = s)
                    }}
                }
                HorizontalPager(count = pages.size, state = pagerState) { page: Int ->
                    val charterItems = listOf<@Composable ()->Unit>( { equipment(equipmentlist, it) }, { avatar(avatarslist, it) }, { gems(gemslist) })
                    val skillItem = listOf<@Composable ()->Unit>( { combatSkill(skillslist) }, { engravings(engravingslist, scaffoldState, lifecycleScope) })
                    val collectionItem = listOf<@Composable ()->Unit>( { card(cardslist) }, { collections(collectibleslist) })
                    val pagelist = mutableListOf<@Composable ()->Unit>(
                        { profileDetails(colosseumslist, it) },
                        { itemslist(charterItems) },
                        { skills(skillItem) },
                        { collection(collectionItem) },
                    )
                    pagelist[page]()
                }
            }
        }
    }
}
@Composable
fun card(
    cardslist: MutableState<charterCards?>,
) {
    var idx by remember {
        mutableStateOf(0)
    }
    Column(Modifier.padding(16.dp)) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(400.dp)) {
            LazyVerticalGrid(GridCells.Fixed(3),
                Modifier
                    .fillMaxWidth()
                    .height(450.dp)) {
                if (cardslist.value?.Cards.isNullOrEmpty()) {
                    item { Text(text = "????????? ???????????? ???????????????") }
                } else {
                    itemsIndexed(cardslist.value?.Cards!!) { idx1, it ->
                        if(cardslist.value?.Effects.isNullOrEmpty()) {
                            Text(text = "???????????? ???????????? ????????????")
                        }else {
                            var index by remember { mutableStateOf(-1) }
                            if(cardslist.value?.Effects?.get(idx)?.cardSlots?.contains(it.slot)!!) {
                                Column(
                                    Modifier.clickable {
                                        index = idx1
                                    }
                                ) {
                                    AsyncImage(model = "${it.icon}", contentDescription = "${it.name}")
                                    Text(text = "${it.name}")
                                    Text(text = "${it.awakeCount} / ${it.awakeTotal}")
                                }
                                if (index != -1) {
                                    Dialog(
                                        onDismissRequest = {
                                            index = -1
                                        }
                                    ) {
                                        Surface(
                                            modifier = Modifier
                                                .width(250.dp)
                                                .height(500.dp)
                                                .padding(10.dp),
                                            color = Color.White
                                        ) {
                                            Box(Modifier.fillMaxSize()) {
                                                LazyColumn(Modifier.fillMaxSize()) {
                                                    val cardJson =
                                                        JSONObject(it.Tooltip)
                                                    for (i in 0 until cardJson.length()) {
                                                        val value =
                                                            cardJson.getJSONObject("Element_%03d".format(i))
                                                        when (value["type"]) {
                                                            "NameTagBox", "SingleTextBox" -> item {
                                                                Text(
                                                                    parse(value.getString("value"))
                                                                )
                                                            }
                                                            "Card" -> item {
                                                                Column() {
                                                                    AsyncImage(
                                                                        model = value.getJSONObject("value").getJSONObject("iconData").getString("iconPath"),
                                                                        contentDescription = "?????? ?????????"
                                                                    )
                                                                    Text(text = "${value.getJSONObject("value").getInt("awakeCount")} / ${value.getJSONObject("value").getInt("awakeTotal")}")
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
                    }
                }
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(100.dp)) {
                if (cardslist.value?.Effects == null) {
                } else {
                        Row() {
                            for (i in 0 until cardslist.value?.Effects?.size!!) {
                                Log.d("????????????", "${i}")
                                Button(onClick = {
                                    idx = i
                                }) {
                                    Text(text = "${i + 1}")
                                }
                            }
                        }
                }
        }
    }
}
@Composable
fun collections(collectionslist: SnapshotStateList<charterCollectibles?>) {
    LazyColumn() {
        if(collectionslist == null) {
            item {Text(text = "???????????? ??????")}
        } else {
            items(collectionslist.toList()) {
                var expandableState by remember {
                    mutableStateOf(false)
                }
                val rotate by animateFloatAsState(targetValue = if(expandableState) 180f else 0f)
                if(it == null) {
                    Text("????????? ??????")
                }else {
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .animateContentSize(animationSpec = tween(durationMillis = 100,))
                            .padding(10.dp)) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(10.dp)) {
                            Row(Modifier
                                .fillMaxWidth()) {
                                AsyncImage(model = "${it.Icon}", contentDescription = "????????? ?????????",  Modifier.weight(1f))
                                Text(text = "${it.Type}",  Modifier.weight(1f))
                                Text(text = "${it.Point} / ${it.MaxPoint}",  Modifier.weight(1f))
                                IconButton(onClick = { expandableState = !expandableState},
                                    Modifier
                                        .weight(1f)
                                        .rotate(rotate)) {
                                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "??????")
                                }
                            }
                            AnimatedVisibility(visible = expandableState) {
                                Column(Modifier.padding(16.dp)) {
                                    for (i in it.CollectiblePoints.indices) {
                                        Box(Modifier.fillMaxWidth()) {
                                            Row() {
                                                Text("${it.CollectiblePoints[i].pointName}")
                                                Spacer(modifier = Modifier.width(3.dp))
                                                Text("${it.CollectiblePoints[i].point} / ${it.CollectiblePoints[i].maxPoint}")
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
}

@Composable
fun itemslist(items: List<@Composable () -> Unit>) {
    val text = listOf("??????", "?????????", "???")
    var index = remember {
        mutableStateOf(0)
    }
    Box() {
        Column() {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(500.dp)) {
                if(items.isNullOrEmpty()) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(size = 64.dp),
                        color = Color.Black,
                        strokeWidth = 6.dp
                    )
                } else {
                    items[index.value]()
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp), contentAlignment = Alignment.BottomCenter) {
                LazyVerticalGrid(columns = GridCells.Adaptive(minSize = (200/2.7).dp)) {
                    text.forEachIndexed { i, s ->
                        item {
                            TextButton(onClick = {index.value = i}) {
                                Text(text = s)
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun skills(items: List<@Composable () -> Unit>) {
    val text = listOf("??????", "??????")
    var index = remember {
        mutableStateOf(0)
    }
    Box() {
        Column() {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(500.dp)) {
                items[index.value]()
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp), contentAlignment = Alignment.BottomCenter) {
                LazyVerticalGrid(columns = GridCells.Adaptive(minSize = (200/2).dp)) {
                    text.forEachIndexed { i, s ->
                        item {
                            TextButton(onClick = {index.value = i}) {
                                Text(text = s)
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun combatSkill(skillslist: SnapshotStateList<charterCombatSkills?>) {
    Log.d("????????????", "${skillslist}")
    if(skillslist.isNullOrEmpty()) {
        Text("???????????? ?????????????????????")
    } else {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)) {
            var index by remember { mutableStateOf(-1) }
            LazyVerticalGrid(columns = GridCells.Fixed(4),
                Modifier
                    .fillMaxSize()) {
                itemsIndexed(skillslist.toList()) { idx, it ->
                    if(it == null) {
                        Text("????????????")
                    } else {
                        Column(Modifier.clickable {
                            Log.d("??????", "${idx}")
                            index = idx
                        }) {
                            AsyncImage("${it.icon}", contentDescription = "?????? ?????????")
                            Text("${it.name}")
                            Box(Modifier.height(5.dp))
                            if (it.rune == null) {
                                Box(
                                    Modifier
                                        .width(20.dp)
                                        .height(40.dp)
                                ) {
                                }
                            } else {
                                Box(
                                    Modifier
                                        .width(40.dp)
                                        .height(60.dp)
                                ) {
                                    Column(Modifier.fillMaxSize()) {
                                        AsyncImage("${it.rune.icon}", contentDescription = "??? ?????????")
                                        Text("${it.rune.name}")
                                    }
                                }
                            }
                        }
                        if (index != -1) {
                            Dialog(
                                onDismissRequest = {
                                    index = -1
                                }
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .width(250.dp)
                                        .padding(10.dp),
                                    color = Color.White
                                ) {
                                    Box(Modifier.fillMaxSize()) {
                                        LazyColumn(Modifier.fillMaxSize()) {
                                            val skillJson =
                                                JSONObject(skillslist.toList()[if (index == -1) 0 else index]!!.tooltip)
                                            for (i in 0 until skillJson.length()) {
                                                val value =
                                                    skillJson.getJSONObject("Element_%03d".format(i))
                                                when (value["type"]) {
                                                    "NameTagBox", "SingleTextBox", "MultiTextBox", "ShowMeTheMoney" -> item {
                                                        Text(
                                                            parse(value.getString("value"))
                                                        )
                                                    }
                                                    "ItemPartBox" -> item {
                                                        Column() {
                                                            Text(
                                                                text = parse(
                                                                    value.getJSONObject("value")
                                                                        .getString("Element_000")
                                                                )
                                                            )
                                                            Row() {
                                                                Text(
                                                                    text = parse(
                                                                        value.getJSONObject("value")
                                                                            .getString("Element_001")
                                                                    )
                                                                )
                                                            }
                                                        }
                                                    }
                                                    "TripodSkillCustom" -> item {
                                                        Column() {
                                                            if (!value.getJSONObject("value")
                                                                    .getJSONObject("Element_000")
                                                                    .getBoolean("lock")
                                                            ) {
                                                                AsyncImage(
                                                                    model = value.getJSONObject(
                                                                        "value"
                                                                    )
                                                                        .getJSONObject("Element_000")
                                                                        .getJSONObject("slotData")
                                                                        .getString("iconPath"),
                                                                    contentDescription = "?????? ?????????"
                                                                )
                                                                Text(
                                                                    "${
                                                                        parse(
                                                                            value.getJSONObject(
                                                                                "value"
                                                                            )
                                                                                .getJSONObject(
                                                                                    "Element_000"
                                                                                )
                                                                                .getString(
                                                                                    "desc"
                                                                                )
                                                                        )
                                                                    } ${
                                                                        parse(
                                                                            value.getJSONObject(
                                                                                "value"
                                                                            )
                                                                                .getJSONObject(
                                                                                    "Element_000"
                                                                                )
                                                                                .getString(
                                                                                    "name"
                                                                                )
                                                                        )
                                                                    }"
                                                                )
                                                                Text(
                                                                    "${
                                                                        parse(
                                                                            value.getJSONObject(
                                                                                "value"
                                                                            )
                                                                                .getJSONObject(
                                                                                    "Element_000"
                                                                                )
                                                                                .getString(
                                                                                    "tier"
                                                                                )
                                                                        )
                                                                    }"
                                                                )
                                                            } else {
                                                                Text(
                                                                    "${
                                                                        parse(
                                                                            value.getJSONObject(
                                                                                "value"
                                                                            )
                                                                                .getJSONObject(
                                                                                    "Element_000"
                                                                                )
                                                                                .getString(
                                                                                    "desc"
                                                                                )
                                                                        )
                                                                    }"
                                                                )
                                                            }
                                                            if (!value.getJSONObject("value")
                                                                    .getJSONObject("Element_001")
                                                                    .getBoolean("lock")
                                                            ) {
                                                                AsyncImage(
                                                                    model = value.getJSONObject(
                                                                        "value"
                                                                    )
                                                                        .getJSONObject("Element_001")
                                                                        .getJSONObject("slotData")
                                                                        .getString("iconPath"),
                                                                    contentDescription = "?????? ?????????"
                                                                )
                                                                Text(
                                                                    "${
                                                                        parse(
                                                                            value.getJSONObject(
                                                                                "value"
                                                                            )
                                                                                .getJSONObject(
                                                                                    "Element_001"
                                                                                )
                                                                                .getString(
                                                                                    "desc"
                                                                                )
                                                                        )
                                                                    } ${
                                                                        parse(
                                                                            value.getJSONObject(
                                                                                "value"
                                                                            )
                                                                                .getJSONObject(
                                                                                    "Element_001"
                                                                                )
                                                                                .getString(
                                                                                    "name"
                                                                                )
                                                                        )
                                                                    }"
                                                                )
                                                                Text(
                                                                    "${
                                                                        parse(
                                                                            value.getJSONObject(
                                                                                "value"
                                                                            )
                                                                                .getJSONObject(
                                                                                    "Element_001"
                                                                                )
                                                                                .getString(
                                                                                    "tier"
                                                                                )
                                                                        )
                                                                    }"
                                                                )
                                                            } else {
                                                                Text(
                                                                    "${
                                                                        parse(
                                                                            value.getJSONObject(
                                                                                "value"
                                                                            )
                                                                                .getJSONObject(
                                                                                    "Element_001"
                                                                                )
                                                                                .getString(
                                                                                    "desc"
                                                                                )
                                                                        )
                                                                    }"
                                                                )
                                                            }
                                                            if (!value.getJSONObject("value")
                                                                    .getJSONObject("Element_002")
                                                                    .getBoolean("lock")
                                                            ) {
                                                                AsyncImage(
                                                                    model = value.getJSONObject(
                                                                        "value"
                                                                    )
                                                                        .getJSONObject("Element_002")
                                                                        .getJSONObject("slotData")
                                                                        .getString("iconPath"),
                                                                    contentDescription = "?????? ?????????"
                                                                )
                                                                Text(
                                                                    "${
                                                                        parse(
                                                                            value.getJSONObject(
                                                                                "value"
                                                                            )
                                                                                .getJSONObject(
                                                                                    "Element_002"
                                                                                )
                                                                                .getString(
                                                                                    "desc"
                                                                                )
                                                                        )
                                                                    } ${
                                                                        parse(
                                                                            value.getJSONObject(
                                                                                "value"
                                                                            )
                                                                                .getJSONObject(
                                                                                    "Element_002"
                                                                                )
                                                                                .getString(
                                                                                    "name"
                                                                                )
                                                                        )
                                                                    }"
                                                                )
                                                                Text(
                                                                    "${
                                                                        parse(
                                                                            value.getJSONObject(
                                                                                "value"
                                                                            )
                                                                                .getJSONObject(
                                                                                    "Element_002"
                                                                                )
                                                                                .getString(
                                                                                    "tier"
                                                                                )
                                                                        )
                                                                    }"
                                                                )
                                                            } else {
                                                                Text(
                                                                    "${
                                                                        parse(
                                                                            value.getJSONObject(
                                                                                "value"
                                                                            )
                                                                                .getJSONObject(
                                                                                    "Element_002"
                                                                                )
                                                                                .getString(
                                                                                    "desc"
                                                                                )
                                                                        )
                                                                    }"
                                                                )
                                                            }
                                                        }
                                                    }
                                                    "CommonSkillTitle" -> item {
                                                        Column() {
                                                            AsyncImage(
                                                                model = value.getJSONObject("value")
                                                                    .getJSONObject("slotData")
                                                                    .getString("iconPath"),
                                                                contentDescription = "?????? ?????????"
                                                            )
                                                            Text(
                                                                text = parse(
                                                                    value.getJSONObject("value")
                                                                        .getString("name")
                                                                )
                                                            )
                                                            Text(
                                                                text = parse(
                                                                    value.getJSONObject("value")
                                                                        .getString("level")
                                                                )
                                                            )
                                                            Text(
                                                                text = parse(
                                                                    value.getJSONObject("value")
                                                                        .getString("leftText")
                                                                )
                                                            )
                                                            Text(
                                                                text = parse(
                                                                    value.getJSONObject("value")
                                                                        .getString("middleText")
                                                                )
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                            if (skillslist.toList()[if (index == -1) 0 else index]!!.tripods.isNullOrEmpty()) {
                                                item { Text("????????? ?????????????????? ????????????") }
                                            } else {
                                                if (skillslist.toList()[if (index == -1) 0 else index]!!.tripods != null) {
                                                    items(skillslist.toList()[if (index == -1) 0 else index]!!.tripods) {
                                                        AsyncImage(
                                                            "${it.icon}",
                                                            contentDescription = "???????????????"
                                                        )
                                                        Text("${it.name}")
                                                        Text("${it.level}")
                                                        Text("${parse(it.tooltip ?: "")}")
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
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun engravings(
    engravingslist: SnapshotStateList<charterEngravings?>,
    scaffoldState: ScaffoldState,
    lifecycleScope: LifecycleCoroutineScope
) {
    if(engravingslist.isNullOrEmpty()) {
        Text("???????????? ?????????????????????")
    } else {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxSize()) {
            var index by remember { mutableStateOf(-1) }
            LazyColumn() {
                items(engravingslist.toList()) {
                    if (it == null) {
                        Text("????????? ?????????????????????.")
                    } else {
                        if(it.Engravings.isNullOrEmpty()) {
                        } else {
                            for(i in it.Engravings.indices) {
                                Row(Modifier.clickable { index = i }) {
                                    AsyncImage("${it.Engravings[i].icon}", contentDescription = "?????????")
                                    Text("${it.Engravings[i].name}")
                                }
                                Box(Modifier.height(5.dp))
                                if (index != -1) {
                                    Dialog(
                                        onDismissRequest = {
                                            index = -1
                                        }
                                    ) {
                                        Surface(
                                            modifier = Modifier
                                                .width(250.dp)
                                                .padding(10.dp),
                                            color = Color.White
                                        ) {
                                            Box(Modifier.fillMaxSize()) {
                                                LazyColumn(Modifier.fillMaxSize()) {
                                                    val engravingsJson =
                                                        JSONObject(it.Engravings[if (index == -1) 0 else index].Tooltip)
                                                    for (i in 0 until engravingsJson.length()) {
                                                        val value =
                                                            engravingsJson.getJSONObject("Element_%03d".format(i))
                                                        when (value["type"]) {
                                                            "NameTagBox", "SingleTextBox", "MultiTextBox", "ShowMeTheMoney" -> item {
                                                                Text(
                                                                    parse(value.getString("value"))
                                                                )
                                                            }
                                                            "ItemPartBox" -> item {
                                                                Column() {
                                                                    Text(
                                                                        text = parse(
                                                                            value.getJSONObject("value")
                                                                                .getString("Element_000")
                                                                        )
                                                                    )
                                                                    Row() {
                                                                        Text(
                                                                            text = parse(
                                                                                value.getJSONObject("value")
                                                                                    .getString("Element_001")
                                                                            )
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                            "EngraveSkillTitle" -> item {
                                                                Column() {
                                                                    AsyncImage(
                                                                        model = value.getJSONObject("value")
                                                                            .getJSONObject("slotData")
                                                                            .getString("iconPath"),
                                                                        contentDescription = "?????? ?????????"
                                                                    )
                                                                    Text(
                                                                        text = parse(
                                                                            value.getJSONObject("value")
                                                                                .getString("forceMiddleText")
                                                                        )
                                                                    )
                                                                    Row() {
                                                                        Text(
                                                                            text = parse(
                                                                                value.getJSONObject("value")
                                                                                    .getString("leftText")
                                                                            )
                                                                        )
                                                                        Text(
                                                                            text = parse(
                                                                                value.getJSONObject("value")
                                                                                    .getString("name")
                                                                            )
                                                                        )
                                                                    }
                                                                    Text(
                                                                        text = parse(
                                                                            value.getJSONObject("value")
                                                                                .getString("rightText")
                                                                        )
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
                    }
                }
                items(engravingslist.toList()) {
                    if (it == null) {
                        Text("????????? ?????????????????????.")
                    } else {
                        if(it.Effects.isNullOrEmpty()) {
                        } else {
                            for(i in it.Effects.indices) {
                                Row(Modifier.clickable {
                                    lifecycleScope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar("${it.Effects[i].description}")
                                    }
                                }) {
                                    Text("${it.Effects[i].name}")
                                }
                                Box(Modifier.height(5.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun gems(gemslist: SnapshotStateList<charterGems?>) {
    var index by remember { mutableStateOf(-1) }
    if(gemslist.isNullOrEmpty()) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(size = 64.dp)
                    .align(Alignment.Center),
                color = Color.Black,
                strokeWidth = 6.dp
            )
        }
    } else {
        Box(Modifier.fillMaxSize()) {
            for (i in 0 until gemslist.size) {
                LazyVerticalGrid(columns = GridCells.Fixed(3),
                    Modifier
                        .fillMaxSize()
                        .padding(top = 15.dp, start = 5.dp, end = 5.dp)) {
                    if(gemslist[i] == null) {
                        item { Text("????????? ?????? ????????????.")}
                    } else {
                        itemsIndexed(gemslist[i]!!.Gems) { idx, it1 ->
                            Box(
                                Modifier
                                    .size(60.dp)
                                    .clickable {
                                        Log.d("?????????", "${idx}")
                                        index = idx
                                    }) {
                                Column() {
                                    AsyncImage(
                                        "${it1?.icon}",
                                        contentDescription = "?????? ?????????",
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Text("${parse(it1?.name?: "")}", fontSize = 8.sp)
                                }
                                if (index != -1) {
                                    Dialog(
                                        onDismissRequest = {
                                            index = -1
                                        }
                                    ) {
                                        Surface(
                                            modifier = Modifier
                                                .width(250.dp)
                                                .height(700.dp)
                                                .padding(10.dp),
                                            color = Color.White
                                        ) {
                                            Box(Modifier.fillMaxSize()) {
                                                LazyColumn(Modifier.fillMaxSize()) {
                                                    val js = JSONObject(tootipParse(gemslist[0]!!.Gems[if(index == -1) 0 else index]?.tooltip?: ""))
                                                    val EffectJs = JSONObject(tootipParse(gemslist[0]!!.Effects[if (index == -1) 0 else index]?.tooltip?: ""))
                                                    for (i in 0 until js.length()) {
                                                        val value = js.getJSONObject("Element_%03d".format(i))
                                                        when (value["type"]) {
                                                            "NameTagBox", "SingleTextBox", "MultiTextBox" -> item {
                                                                Text(
                                                                    parse(value.getString("value"))
                                                                )
                                                            }
                                                            "ItemPartBox" -> item {
                                                                Column() {
                                                                    Text(
                                                                        text = parse(
                                                                            value.getJSONObject("value")
                                                                                .getString("Element_000")
                                                                        )
                                                                    )
                                                                    Row() {
                                                                        Text(
                                                                            text = parse(
                                                                                value.getJSONObject("value")
                                                                                    .getString("Element_001")
                                                                            )
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                            "ItemTitle" -> item {
                                                                Column() {
                                                                    AsyncImage(
                                                                        model = value.getJSONObject("value")
                                                                            .getJSONObject("slotData")
                                                                            .getString("iconPath"),
                                                                        contentDescription = "?????? ?????????"
                                                                    )
                                                                    Text(
                                                                        text = parse(
                                                                            value.getJSONObject("value")
                                                                                .getString("leftStr0")
                                                                        )
                                                                    )
                                                                    Text(
                                                                        text = parse(
                                                                            value.getJSONObject("value")
                                                                                .getString("leftStr2")
                                                                        )
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                    items(gemslist[i]!!.Effects) {
                                                        if(gemslist[0]!!.Gems[if(index == -1) 0 else index]?.slot == it?.gemSlot) {
                                                            Log.d("????????????1", "${it1?.slot}  ${it?.gemSlot}")
                                                            for (i in 0 until EffectJs.length()) {
                                                                val value = EffectJs.getJSONObject("Element_%03d".format(i))
                                                                when (value["type"]) {
                                                                    "NameTagBox", "SingleTextBox", "MultiTextBox" ->
                                                                        Text(
                                                                            parse(value.getString("value"))
                                                                        )
                                                                    "ItemPartBox" ->
                                                                        Column() {
                                                                            Text(
                                                                                text = parse(
                                                                                    value.getJSONObject("value")
                                                                                        .getString("Element_000")
                                                                                )
                                                                            )
                                                                            Row() {
                                                                                Text(
                                                                                    text = parse(
                                                                                        value.getJSONObject("value")
                                                                                            .getString("Element_001")
                                                                                    )
                                                                                )
                                                                            }
                                                                        }
                                                                    "CommonSkillTitle" ->
                                                                        Column() {
                                                                            AsyncImage(
                                                                                model = value.getJSONObject("value")
                                                                                    .getJSONObject("slotData")
                                                                                    .getString("iconPath"),
                                                                                contentDescription = "?????? ?????????"
                                                                            )
                                                                            Text(
                                                                                "${parse(
                                                                                    value.getJSONObject("value")
                                                                                        .getString("leftText"))}"
                                                                            )
                                                                            Text(
                                                                                text = parse(
                                                                                    value.getJSONObject("value")
                                                                                        .getString("level")
                                                                                )
                                                                            )
                                                                            Text(
                                                                                text = parse(
                                                                                    value.getJSONObject("value")
                                                                                        .getString("middleText")
                                                                                )
                                                                            )
                                                                            Text(
                                                                                text = parse(
                                                                                    value.getJSONObject("value")
                                                                                        .getString("name")
                                                                                )
                                                                            )
                                                                        }
                                                                    "TripodSkillCustom" ->
                                                                        Column() {
                                                                            if (!value.getJSONObject("value")
                                                                                    .getJSONObject("Element_000")
                                                                                    .getBoolean("lock")
                                                                            ) {
                                                                                AsyncImage(
                                                                                    model = value.getJSONObject(
                                                                                        "value"
                                                                                    )
                                                                                        .getJSONObject("Element_000")
                                                                                        .getJSONObject("slotData")
                                                                                        .getString("iconPath"),
                                                                                    contentDescription = "?????? ?????????"
                                                                                )
                                                                                Text(
                                                                                    "${
                                                                                        parse(
                                                                                            value.getJSONObject(
                                                                                                "value"
                                                                                            )
                                                                                                .getJSONObject(
                                                                                                    "Element_000"
                                                                                                )
                                                                                                .getString(
                                                                                                    "desc"
                                                                                                )
                                                                                        )
                                                                                    } ${
                                                                                        parse(
                                                                                            value.getJSONObject(
                                                                                                "value"
                                                                                            )
                                                                                                .getJSONObject(
                                                                                                    "Element_000"
                                                                                                )
                                                                                                .getString(
                                                                                                    "name"
                                                                                                )
                                                                                        )
                                                                                    }"
                                                                                )
                                                                                Text(
                                                                                    parse(
                                                                                        value.getJSONObject(
                                                                                            "value"
                                                                                        )
                                                                                            .getJSONObject(
                                                                                                "Element_000"
                                                                                            )
                                                                                            .getString(
                                                                                                "tier"
                                                                                            )
                                                                                    )
                                                                                )
                                                                            } else {
                                                                                Text(
                                                                                    parse(
                                                                                        value.getJSONObject(
                                                                                            "value"
                                                                                        )
                                                                                            .getJSONObject(
                                                                                                "Element_000"
                                                                                            )
                                                                                            .getString(
                                                                                                "desc"
                                                                                            )
                                                                                    )
                                                                                )
                                                                            }
                                                                            if (!value.getJSONObject("value")
                                                                                    .getJSONObject("Element_001")
                                                                                    .getBoolean("lock")
                                                                            ) {
                                                                                AsyncImage(
                                                                                    model = value.getJSONObject(
                                                                                        "value"
                                                                                    )
                                                                                        .getJSONObject("Element_001")
                                                                                        .getJSONObject("slotData")
                                                                                        .getString("iconPath"),
                                                                                    contentDescription = "?????? ?????????"
                                                                                )
                                                                                Text(
                                                                                    "${
                                                                                        parse(
                                                                                            value.getJSONObject(
                                                                                                "value"
                                                                                            )
                                                                                                .getJSONObject(
                                                                                                    "Element_001"
                                                                                                )
                                                                                                .getString(
                                                                                                    "desc"
                                                                                                )
                                                                                        )
                                                                                    } ${
                                                                                        parse(
                                                                                            value.getJSONObject(
                                                                                                "value"
                                                                                            )
                                                                                                .getJSONObject(
                                                                                                    "Element_001"
                                                                                                )
                                                                                                .getString(
                                                                                                    "name"
                                                                                                )
                                                                                        )
                                                                                    }"
                                                                                )
                                                                                Text(
                                                                                    "${
                                                                                        parse(
                                                                                            value.getJSONObject(
                                                                                                "value"
                                                                                            )
                                                                                                .getJSONObject(
                                                                                                    "Element_001"
                                                                                                )
                                                                                                .getString(
                                                                                                    "tier"
                                                                                                )
                                                                                        )
                                                                                    }"
                                                                                )
                                                                            } else {
                                                                                Text(
                                                                                    "${
                                                                                        parse(
                                                                                            value.getJSONObject(
                                                                                                "value"
                                                                                            )
                                                                                                .getJSONObject(
                                                                                                    "Element_001"
                                                                                                )
                                                                                                .getString(
                                                                                                    "desc"
                                                                                                )
                                                                                        )
                                                                                    }")
                                                                            }
                                                                            if (!value.getJSONObject("value")
                                                                                    .getJSONObject("Element_002")
                                                                                    .getBoolean("lock")
                                                                            ) {
                                                                                AsyncImage(
                                                                                    model = value.getJSONObject(
                                                                                        "value"
                                                                                    )
                                                                                        .getJSONObject("Element_002")
                                                                                        .getJSONObject("slotData")
                                                                                        .getString("iconPath"),
                                                                                    contentDescription = "?????? ?????????"
                                                                                )
                                                                                Text(
                                                                                    "${
                                                                                        parse(
                                                                                            value.getJSONObject(
                                                                                                "value"
                                                                                            )
                                                                                                .getJSONObject(
                                                                                                    "Element_002"
                                                                                                )
                                                                                                .getString(
                                                                                                    "desc"
                                                                                                )
                                                                                        )
                                                                                    } ${
                                                                                        parse(
                                                                                            value.getJSONObject(
                                                                                                "value"
                                                                                            )
                                                                                                .getJSONObject(
                                                                                                    "Element_002"
                                                                                                )
                                                                                                .getString(
                                                                                                    "name"
                                                                                                )
                                                                                        )
                                                                                    }"
                                                                                )
                                                                                Text(
                                                                                    "${
                                                                                        parse(
                                                                                            value.getJSONObject(
                                                                                                "value"
                                                                                            )
                                                                                                .getJSONObject(
                                                                                                    "Element_002"
                                                                                                )
                                                                                                .getString(
                                                                                                    "tier"
                                                                                                )
                                                                                        )
                                                                                    }"
                                                                                )
                                                                            } else {
                                                                                Text(
                                                                                    "${
                                                                                        parse(
                                                                                            value.getJSONObject(
                                                                                                "value"
                                                                                            )
                                                                                                .getJSONObject(
                                                                                                    "Element_002"
                                                                                                )
                                                                                                .getString(
                                                                                                    "desc"
                                                                                                )
                                                                                        )
                                                                                    }")
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
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun avatar(avatarslist: SnapshotStateList<charterAvatars?>, charterProfile: charterProfile) {
    if(avatarslist.isNullOrEmpty()) {
        Text("???????????? ???????????? ????????????")
    } else {
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()) {
            var index by remember { mutableStateOf(-1) }
            AsyncImage(model = "${charterProfile.CharacterImage}", contentDescription = "????????? ?????????", modifier = Modifier.fillMaxSize())
            LazyVerticalGrid(columns = GridCells.Fixed(2),
                Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp)
                ,horizontalArrangement = Arrangement.Center,verticalArrangement = Arrangement.spacedBy(16.dp),) {
                itemsIndexed(avatarslist.toList()) { idx, it ->
                    if(it == null) {
                        Text("????????? ??????")
                    } else {
                        val json = JSONObject(it.Tooltip?.let { it1 -> tootipParse(it1) })
                        for (i in 0 until json.length() - 1) {
                            val value = json.getJSONObject("Element_%03d".format(i))
                            if(value["type"] == "ItemTitle") {
                                Log.d("value123","${value}")
                                Box(
                                    Modifier
                                        .size(40.dp)
                                        .clickable {
                                            index = idx

                                        }) {
                                    AsyncImage(model = "${it.Icon}", contentDescription = "?????? ?????????", modifier = Modifier.fillMaxSize())
                                }
                            }
                        }
                        if(index != -1) {
                            Dialog(
                                onDismissRequest = {
                                    index = -1
                                }
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .width(250.dp)
                                        .padding(10.dp),
                                    color = Color.White
                                ) {
                                    Box(Modifier.fillMaxSize()) {
                                        LazyColumn(Modifier.fillMaxSize()) {
                                            val js = JSONObject(avatarslist.toList()[if(index == -1) 0 else index]!!.Tooltip?.let { it1 -> tootipParse(it1) })
                                            for (i in 0 until js.length()-1) {
                                                val value = js.getJSONObject("Element_%03d".format(i))
                                                when (value["type"]) {
                                                    "NameTagBox", "SingleTextBox", "MultiTextBox", "ShowMeTheMoney" -> item {
                                                        Text(
                                                            parse(value.getString("value"))
                                                        )
                                                    }
                                                    "ItemPartBox" -> item {
                                                        Column() {
                                                            Text(
                                                                text = parse(
                                                                    value.getJSONObject("value")
                                                                        .getString("Element_000")
                                                                )
                                                            )
                                                            Row() {
                                                                Text(
                                                                    text = parse(
                                                                        value.getJSONObject("value")
                                                                            .getString("Element_001")
                                                                    )
                                                                )
                                                            }
                                                        }
                                                    }
                                                    "IndentStringGroup" -> item {
                                                        Column() {
                                                            keyToList<JSONObject>(value.getJSONObject("value")).map { first ->
                                                                keyToList<JSONObject>(first.getJSONObject("contentStr")).map { second ->
                                                                    when (second["bPoint"]) {
                                                                        true, 1 -> Text(
                                                                            parse(
                                                                                second.getString(
                                                                                    "contentStr"
                                                                                )
                                                                            ), color = Color.White
                                                                        )
                                                                        false, 0 -> Text(
                                                                            parse(
                                                                                second.getString(
                                                                                    "contentStr"
                                                                                )
                                                                            ), color = Color.DarkGray
                                                                        )
                                                                        else -> null
                                                                    }
                                                                }
                                                                Text(parse(first.getString("topStr")))
                                                            }
                                                        }
                                                    }
                                                    "Progress" -> item {
                                                        Column() {
                                                            Text(
                                                                text = parse(
                                                                    value.getJSONObject("value")
                                                                        .getString("forceValue")
                                                                )
                                                            )
                                                            Text(
                                                                text = value.getJSONObject("value")
                                                                    .getInt("maximum").toString()
                                                            )
                                                            Text(
                                                                text = value.getJSONObject("value")
                                                                    .getInt("minimum").toString()
                                                            )
                                                            Text(
                                                                text = parse(
                                                                    value.getJSONObject("value")
                                                                        .getString("title")
                                                                )
                                                            )
                                                            Text(
                                                                text = parse(
                                                                    value.getJSONObject("value")
                                                                        .getString("value")
                                                                )
                                                            )
                                                        }
                                                    }
                                                    "ItemTitle" -> item {
                                                        Column() {
                                                            AsyncImage(
                                                                model = value.getJSONObject("value")
                                                                    .getJSONObject("slotData")
                                                                    .getString("iconPath"),
                                                                contentDescription = "?????? ?????????"
                                                            )
                                                            Text(
                                                                text = parse(
                                                                    value.getJSONObject("value")
                                                                        .getString("leftStr0")
                                                                )
                                                            )
                                                            Text(
                                                                text = parse(
                                                                    value.getJSONObject("value")
                                                                        .getString("leftStr2")
                                                                )
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
            }
        }
    }
}
@Composable
fun equipment(equipmentlist: SnapshotStateList<charterEquipment?>, charterProfile: charterProfile) {
    if(equipmentlist.isNullOrEmpty()) {
        Text("???????????? ?????????????????????")
    } else {
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()) {
            var index by remember { mutableStateOf(-1) }
            AsyncImage(model = "${charterProfile.CharacterImage}", contentDescription = "????????? ?????????", modifier = Modifier.fillMaxSize())
            LazyVerticalGrid(columns = GridCells.Fixed(2),
                Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp)
                    .align(Alignment.BottomCenter)
                ,horizontalArrangement = Arrangement.Center,verticalArrangement = Arrangement.spacedBy(16.dp),) {
                itemsIndexed(equipmentlist.toList()) { idx, it ->
                    if(it == null) {
                        Text("?????? ??????")
                    } else {
                        val json = JSONObject(it.Tooltip?.let { it1 -> tootipParse(it1) })
                        for (i in 0 until json.length()) {
                            val value = json.getJSONObject("Element_%03d".format(i))
                            if(value["type"] == "ItemTitle") {
                                Log.d("value123","${value}")
                                Box(
                                    Modifier
                                        .size(40.dp)
                                        .clickable {
                                            index = idx
                                            Log.d("?????????", "${index}")
                                        }) {
                                    AsyncImage(model = value.getJSONObject("value").getJSONObject("slotData").getString("iconPath"), contentDescription = "?????? ?????????", modifier = Modifier.fillMaxSize())
                                    when(value.getJSONObject("value").getInt("qualityValue") / 10) {
                                        0 -> Box(
                                            Modifier
                                                .width(40.dp)
                                                .height(7.dp)
                                                .background(Color.Red)
                                                .align(Alignment.BottomCenter)) {}
                                        1,2 -> Box(
                                            Modifier
                                                .width(40.dp)
                                                .height(7.dp)
                                                .background(Color.Yellow)
                                                .align(Alignment.BottomCenter)) {}
                                        3,4,5,6 -> Box(
                                            Modifier
                                                .width(40.dp)
                                                .height(7.dp)
                                                .background(Color.Green)
                                                .align(Alignment.BottomCenter)) {}
                                        7,8 -> Box(
                                            Modifier
                                                .width(40.dp)
                                                .height(7.dp)
                                                .background(Color.Blue)
                                                .align(Alignment.BottomCenter)) {}
                                        9,10 -> Box(
                                            Modifier
                                                .width(40.dp)
                                                .height(7.dp)
                                                .background(Color.Magenta)
                                                .align(Alignment.BottomCenter)) {}
                                        else -> Box(
                                            Modifier
                                                .width(40.dp)
                                                .height(7.dp)
                                                .background(Color.Black)
                                                .align(Alignment.BottomCenter)
                                        ) {}
                                    }
                                }
                            }
                        }
                        if(index != -1) {
                            Dialog(
                                onDismissRequest = {
                                    index = -1
                                }
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .width(250.dp)
                                        .padding(10.dp),
                                    color = Color.White
                                ) {
                                    Box(Modifier.fillMaxSize()) {
                                        LazyColumn(Modifier.fillMaxSize()) {
                                            val js = JSONObject(equipmentlist.toList()[if(index == -1) 0 else index]!!.Tooltip?.let { it1 -> tootipParse(it1) })
                                            for (i in 0 until js.length()) {
                                                val value = js.getJSONObject("Element_%03d".format(i))
                                                when (value["type"]) {
                                                    "NameTagBox", "SingleTextBox", "MultiTextBox", "ShowMeTheMoney" -> item {
                                                        Text(
                                                            parse(value.getString("value"))
                                                        )
                                                    }
                                                    "ItemPartBox" -> item {
                                                        Column() {
                                                            Text(
                                                                text = parse(
                                                                    value.getJSONObject("value")
                                                                        .getString("Element_000")
                                                                )
                                                            )
                                                            Row() {
                                                                Text(
                                                                    text = parse(
                                                                        value.getJSONObject("value")
                                                                            .getString("Element_001")
                                                                    )
                                                                )
                                                            }
                                                        }
                                                    }
                                                    "IndentStringGroup" -> item {
                                                        Column() {
                                                            keyToList<JSONObject>(value.getJSONObject("value")).map { first ->
                                                                keyToList<JSONObject>(first.getJSONObject("contentStr")).map { second ->
                                                                    when (second["bPoint"]) {
                                                                        true, 1 -> Text(
                                                                            parse(
                                                                                second.getString(
                                                                                    "contentStr"
                                                                                )
                                                                            ), color = Color.DarkGray
                                                                        )
                                                                        false, 0 -> Text(
                                                                            parse(
                                                                                second.getString(
                                                                                    "contentStr"
                                                                                )
                                                                            ), color = Color.DarkGray
                                                                        )
                                                                        else -> null
                                                                    }
                                                                }
                                                                Text(parse(first.getString("topStr")))

                                                            }
                                                        }
                                                    }
                                                    "Progress" -> item {
                                                        Column() {
                                                            Text(
                                                                text = parse(
                                                                    value.getJSONObject("value")
                                                                        .getString("forceValue")
                                                                )
                                                            )
                                                            Text(
                                                                text = value.getJSONObject("value")
                                                                    .getInt("maximum").toString()
                                                            )
                                                            Text(
                                                                text = value.getJSONObject("value")
                                                                    .getInt("minimum").toString()
                                                            )
                                                            Text(
                                                                text = parse(
                                                                    value.getJSONObject("value")
                                                                        .getString("title")
                                                                )
                                                            )
                                                            Text(
                                                                text = parse(
                                                                    value.getJSONObject("value")
                                                                        .getString("value")
                                                                )
                                                            )
                                                        }
                                                    }
                                                    "ItemTitle" -> item {
                                                        Column() {
                                                            AsyncImage(
                                                                model = value.getJSONObject("value")
                                                                    .getJSONObject("slotData")
                                                                    .getString("iconPath"),
                                                                contentDescription = "?????? ?????????"
                                                            )
                                                            Text(
                                                                text = parse(
                                                                    value.getJSONObject("value")
                                                                        .getString("leftStr0")
                                                                )
                                                            )
                                                            Text(
                                                                text = parse(
                                                                    value.getJSONObject("value")
                                                                        .getString("leftStr2")
                                                                )
                                                            )
                                                            when (value.getJSONObject("value")
                                                                .getInt("qualityValue") / 10) {
                                                                0, 1, 2 -> Box(
                                                                    Modifier
                                                                        .fillMaxWidth()
                                                                        .height(5.dp)
                                                                        .padding(16.dp)
                                                                        .background(Color.Red)
                                                                ) {}
                                                                3, 4 -> Box(
                                                                    Modifier
                                                                        .fillMaxWidth()
                                                                        .height(5.dp)
                                                                        .padding(16.dp)
                                                                        .background(Color.Yellow)
                                                                ) {}
                                                                else -> Box(
                                                                    Modifier
                                                                        .fillMaxWidth()
                                                                        .height(5.dp)
                                                                        .padding(16.dp)
                                                                        .background(Color.Green)
                                                                ) {}
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

                }
            }
        }
    }
    // openDialog.value = !openDialog.value
}
inline fun <reified T> keyToList(key: JSONObject):List<T> {
    val list:ArrayList<T> = arrayListOf<T>()
    for (i in 0 until key.length()) {
        when(T::class) {
            String::class -> list.add(key.getString("Element_%03d".format(i)) as T)
            JSONObject::class -> list.add(key.getJSONObject("Element_%03d".format(i)) as T)
            else -> null
        }
    }
    return list
}
fun parse(string: String):String {
    val regex ="<[^<>]+>".toRegex()
    val str = string.replace("<br>", "\n").replace("\t", "")
    var str2 = str.replace("<BR>", "\n").replace(regex, "")
    Log.d("?????????", "${str2}")
    return str2
}
fun tootipParse (str: String):String {
    val str1 = str.replace("\\r\\n", "")
    Log.d("adsdasasd", "${str1.replace("\\\"", "\"")}")
    return str1.replace("\\\"", "\"")
}
@ExperimentalPagerApi
@Composable
fun profileDetails(colosseums: SnapshotStateList<charterColosseums?>, charterProfile: charterProfile) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
        LazyColumn(Modifier.padding(16.dp)) {
            item {
                Column() {
                    Text("?????? ?????? : ${charterProfile.ServerName?: ""}")
                    Text("?????? ?????? : ${charterProfile.GuildName?: ""}")
                    Text("?????? ?????? : ${charterProfile.GuildMemberGrade?: ""}")
                    Text("????????? ?????? : ${charterProfile.ExpeditionLevel?: ""}")
                    Text("PVP ?????? : ${charterProfile.PvpGradeName?: ""}")
                    Text("?????? ?????? : ${charterProfile.TownName?: ""}")
                    Text("?????? ?????? : ${charterProfile.TownLevel?: ""}")
                    Text("??????")
                    for (i in charterProfile.Tendencies.indices) {
                            Text( "${charterProfile.Tendencies[i].type?: ""} : ${charterProfile.Tendencies[i].Point?: 0} / ${charterProfile.Tendencies[i].MaxPoint?: 0}")
                    }
                    Text("?????????")
                    for (i in charterProfile.Stats.indices) {
                        var expandableState by remember {
                            mutableStateOf(false)
                        }
                        val rotate by animateFloatAsState(targetValue = if(expandableState) 180f else 0f)
                        Card(
                            Modifier
                                .fillMaxWidth()
                                .animateContentSize(animationSpec = tween(durationMillis = 100,))
                                .padding(10.dp)) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)) {
                                Row(Modifier
                                    .fillMaxWidth()) {
                                    Text("${charterProfile.Stats[i].type?: ""} ${charterProfile.Stats[i].value?: ""}", Modifier.weight(6f))
                                    IconButton(onClick = { expandableState = !expandableState},
                                        Modifier
                                            .weight(1f)
                                            .rotate(rotate)) {
                                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "??????")
                                    }
                                }
                                AnimatedVisibility(visible = expandableState) {
                                    Column() {
                                        for (j in charterProfile.Stats[i].tooltip.indices) {
                                            Text("${j + 1} : ${parse(charterProfile.Stats[i].tooltip[j])?: ""}")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if(colosseums.isNullOrEmpty()) {
                item{ Text("????????? ????????????") }
            } else {
                items(colosseums.toList()) {
                    if(it == null) {
                        Text("?????? ??????")
                    } else {
                        Column(Modifier.fillMaxWidth()) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("${it.Colosseums.last().seasonName}")
                                Spacer(Modifier.width(10.dp))
                                Text("?????? ?????? : ${if (it.Rank == 0) "??????" else it.Rank ?: "??????"}")
                            }
                            Spacer(Modifier.height(5.dp))
                            HorizontalPager( // ????????? ????????? ????????? pager
                                count = it.Colosseums.size, // ????????? ???
                                state = pagerState // PagerState
                            ) { page ->
                                // ????????? content
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(150.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column() {
                                            if (it.Colosseums[pagerState.currentPage].competitive == null) {
                                                Text("??????????????????")
                                            } else {
                                                Column() {
                                                    AsyncImage(
                                                        it.Colosseums[pagerState.currentPage].competitive?.rankIcon,
                                                        contentDescription = "??????"
                                                    )
                                                    Text("${it.Colosseums[pagerState.currentPage].competitive?.rankName}")
                                                }
                                                Text("?????? : ${it.Colosseums[pagerState.currentPage].competitive?.playCount}")
                                                Text("?????? : ${it.Colosseums[pagerState.currentPage].competitive?.victoryCount} / ${it.Colosseums[pagerState.currentPage].competitive?.loseCount}")
                                            }
                                        }
                                        Column() {
                                            if (it.Colosseums[pagerState.currentPage].teamDeathmatch == null) {
                                                Text("??????????????? ????????????")
                                            } else {
                                                Text("?????? : ${it.Colosseums[pagerState.currentPage].teamDeathmatch?.playCount}")
                                                Text("?????? : ${it.Colosseums[pagerState.currentPage].teamDeathmatch?.victoryCount} / ${it.Colosseums[pagerState.currentPage].teamDeathmatch?.loseCount}")
                                            }

                                            if (it.Colosseums[pagerState.currentPage].deathmatch == null) {
                                                Text("???????????? ????????????")
                                            } else {
                                                Text("?????? : ${it.Colosseums[pagerState.currentPage].deathmatch?.playCount}")
                                                Text("?????? : ${it.Colosseums[pagerState.currentPage].deathmatch?.victoryCount} / ${it.Colosseums[pagerState.currentPage].deathmatch?.loseCount}")
                                            }


                                            if (it.Colosseums[pagerState.currentPage].teamElimination == null) {
                                                Text("????????? ????????????")
                                            } else {
                                                Text("?????? : ${it.Colosseums[pagerState.currentPage].teamElimination?.playCount}")
                                                Text("?????? : ${it.Colosseums[pagerState.currentPage].teamElimination?.victoryCount} / ${it.Colosseums[pagerState.currentPage].teamElimination?.loseCount}")
                                            }

                                            if (it.Colosseums[pagerState.currentPage].coOpBattle == null) {
                                                Text("??????????????????")
                                            } else {
                                                Text("?????? : ${it.Colosseums[pagerState.currentPage].coOpBattle?.playCount}")
                                                Text("?????? : ${it.Colosseums[pagerState.currentPage].coOpBattle?.victoryCount} / ${it.Colosseums[pagerState.currentPage].coOpBattle?.loseCount}")
                                            }

                                        }
                                    }
                                }

                            }
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            if (pagerState.currentPage <= 0) return@launch
                                            // animateScrollToPage ????????? suspend function ?????? ????????? Coroutine scope ????????? ???????????? ???
                                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                        }
                                    },
                                ) {
                                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "??????")
                                }
                                Text(
                                    "${it.Colosseums[pagerState.currentPage].seasonName}",
                                    Modifier.padding(top = 14.dp)
                                )
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            if (pagerState.currentPage >= it.Colosseums.size) return@launch
                                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                        }

                                    }
                                ) {
                                    Icon(
                                        Icons.Default.KeyboardArrowRight,
                                        contentDescription = "??????"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
}

@Composable
fun collection(items: List<@Composable () -> Unit>) {
    val text = listOf("??????", "?????????")
    var index = remember {
        mutableStateOf(0)
    }
    Box() {
        Column() {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(500.dp)) {
                if(items.isNullOrEmpty()) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(size = 64.dp),
                        color = Color.Black,
                        strokeWidth = 6.dp
                    )
                } else {
                    items[index.value]()
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp), contentAlignment = Alignment.BottomCenter) {
                LazyVerticalGrid(columns = GridCells.Adaptive(minSize = (200/2).dp)) {
                    text.forEachIndexed { i, s ->
                        item {
                            TextButton(onClick = {index.value = i}) {
                                Text(text = s)
                            }
                        }
                    }
                }
            }
        }
    }
}
