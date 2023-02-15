package com.example.myapplication.Page

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

@ExperimentalPagerApi
@Composable
fun ProfileDetailHome(
    navController: NavHostController,
    username: String,
    context: Context,
    lifecycleScope: LifecycleCoroutineScope
) {

    val list = remember {
        mutableStateListOf<charterProfile>()
    }
//    var url:String
    val scope = MainScope()
    DisposableEffect(0){
        scope.launch {
            while (true) {
                getJSONProfile(list, context, username)
                delay(3000)
            }
        }
        onDispose {
            scope.cancel()
        }
    }
    val db: DBSearchData = DBSearchData(context);
    val str = db.selectDataByName(username);

    db.deleteData(str?.name)
    list.map {
        db.addSearchData(searchData = SearchData(time = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),name = it.CharacterName, title = it.Title?: "", image = it.CharacterImage?: ""))
    }
    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState) {
        LazyColumn() {
            items(list) {
                profile(it)
                profileContent(it, context, username, scaffoldState, lifecycleScope)
            }
        }
    }
}

@Composable
fun profile(it: charterProfile) {
    Box(
        Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.DarkGray)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            val className = when (it.CharacterClassName) {
                "아르카나" -> R.drawable.arcana
                "바드" -> R.drawable.bard
                "배틀마스터" ->R.drawable.battle_master
                "버서커" -> R.drawable.berserker
                "블레이드" -> R.drawable.blade
                "블레스터" -> R.drawable.blaster
                "데모닉" -> R.drawable.demonic
                "디스트로이어" -> R.drawable.destroyed
                "데빌헌터" -> R.drawable.devil_hunter
                "도화가" -> R.drawable.drawing_artist
                "건슬링어" -> R.drawable.gunslinger
                "호크아이" -> R.drawable.hawkeye
                "홀리나이트" -> R.drawable.holy_night
                "기상술사" -> R.drawable.meteorologist
                "인파이터" -> R.drawable.infight
                "리퍼" -> R.drawable.reaper
                "스카우터" -> R.drawable.scouter
                "슬레이어" -> R.drawable.slayer
                "소서리스" -> R.drawable.sorceress
                "창술사" -> R.drawable.spearman
                "스트라이커" -> R.drawable.striker
                "서머너" -> R.drawable.summoner
                "기공사" -> R.drawable.technician
                "워로드" -> R.drawable.warlord
                else -> null
            }
            Box(
                Modifier
                    .size(50.dp)
                    .background(Color.White)
                    .clip(CircleShape), contentAlignment = Alignment.Center) {
                if(it.CharacterClassName.isNullOrEmpty()) {
                    null
                } else {
                    className?.let { it1 -> painterResource(id = it1) }?.let { it2 ->
                        Image(
                            painter = it2,
                            contentDescription = "직업 아이콘",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                }

            }
            Column(Modifier.width(200.dp)) {
                Text("캐릭터 이름 : ${it.CharacterName}")
                Text("칭호 : ${it.Title?: ""}")
                Text("캐릭터 레벨 : Lv.${it.CharacterLevel}")
                Text("최대아이템 레벨 : Lv.${it.ItemMaxLevel}")
                Text("현재아이템 레벨 : Lv.${it.ItemAvgLevel}")
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
fun profileContent(
    it: charterProfile,
    context: Context,
    username: String,
    scaffoldState: ScaffoldState,
    lifecycleScope: LifecycleCoroutineScope
) {
    // it : 캐릭터 정보
    // 스킬 정보
    val skillslist = remember {
        mutableStateListOf<charterCombatSkills>()
    }
    // 아바타 정보
    val avatarslist = remember {
        mutableStateListOf<charterAvatars>()
    }
    // 장비 정보
    val equipmentlist = remember {
        mutableStateListOf<charterEquipment>()
    }
    // 카드 정보
    val cardslist = remember {
        mutableStateOf<charterCards?>(null)
    }
    // 수집품 정보
    val collectibleslist = remember {
        mutableStateListOf<charterCollectibles>()
    }
    // 투기장 정보
    val colosseumslist = remember {
        mutableStateListOf<charterColosseums>()
    }
    // 각인 정보
    val engravingslist = remember {
        mutableStateListOf<charterEngravings>()
    }
    // 보석 정보
    val gemslist = remember {
        mutableStateListOf<charterGems>()
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
    val pages = listOf("캐릭터 정보", "아이템", "스킬", "수집품")
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
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
                val collectionItem = listOf<@Composable ()->Unit>( { card(cardslist,scaffoldState, lifecycleScope) }, { collections(collectibleslist) })
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
@Composable
fun card(
    cardslist: MutableState<charterCards?>,
    scaffoldState: ScaffoldState,
    lifecycleScope: LifecycleCoroutineScope
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
                    item { Text(text = "정보를 불러오지 못했습니다") }
                } else {
                    itemsIndexed(cardslist.value?.Cards!!) { idx1, it ->
                        if(cardslist.value?.Effects.isNullOrEmpty()) {
                            Text(text = "적용중인 카드셋가 없습니다")
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
                                                                        contentDescription = "카드 이미지"
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
                                Log.d("로그로그", "${i}")
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
fun collections(collectionslist: SnapshotStateList<charterCollectibles>) {

}

@Composable
fun itemslist(items: List<@Composable () -> Unit>) {
    val text = listOf("장비", "아바타", "젬")
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
    val text = listOf("스킬", "각인")
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
fun combatSkill(skillslist: SnapshotStateList<charterCombatSkills>) {
    Log.d("돼지돼지", "${skillslist}")
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)) {
        var index by remember { mutableStateOf(-1) }
        LazyVerticalGrid(columns = GridCells.Fixed(4),
            Modifier
                .fillMaxSize()) {
            itemsIndexed(skillslist.toList()) { idx, it ->
                Column(Modifier.clickable {
                    Log.d("인덱", "${idx}")
                    index = idx
                }) {
                    AsyncImage("${it.icon}", contentDescription = "스킬 이미지")
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
                                AsyncImage("${it.rune.icon}", contentDescription = "룬 이미지")
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
                                        JSONObject(skillslist.toList()[if (index == -1) 0 else index].tooltip)
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
                                                            contentDescription = "스킬 아이콘"
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
                                                            contentDescription = "스킬 아이콘"
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
                                                            contentDescription = "스킬 아이콘"
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
                                                        contentDescription = "스킬 아이콘"
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
                                    if (skillslist.toList()[if (index == -1) 0 else index].tripods.isNullOrEmpty()) {
                                        item { Text("적용된 트라이포드가 없습니다") }
                                    } else {
                                        if (skillslist.toList()[if (index == -1) 0 else index].tripods != null) {
                                            items(skillslist.toList()[if (index == -1) 0 else index].tripods) {
                                                AsyncImage(
                                                    "${it.icon}",
                                                    contentDescription = "스킬이미지"
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

@Composable
fun engravings(
    engravingslist: SnapshotStateList<charterEngravings>,
    scaffoldState: ScaffoldState,
    lifecycleScope: LifecycleCoroutineScope
) {
    Column(
        Modifier
            .padding(16.dp)
            .fillMaxSize()) {
        var index by remember { mutableStateOf(-1) }
        LazyColumn() {
            items(engravingslist.toList()) {
                if(it.Engravings.isNullOrEmpty()) {
                } else {
                    for(i in it.Engravings.indices) {
                        Row(Modifier.clickable { index = i }) {
                            AsyncImage("${it.Engravings[i].icon}", contentDescription = "아이콘")
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
                                                                contentDescription = "스킬 아이콘"
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
            items(engravingslist.toList()) {
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
@Composable
fun gems(gemslist: SnapshotStateList<charterGems>) {
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
                    itemsIndexed(gemslist[i].Gems) { idx, it1 ->
                        Box(
                            Modifier
                                .size(60.dp)
                                .clickable {
                                    Log.d("인덱스", "${idx}")
                                    index = idx
                                }) {
                            Column() {
                                AsyncImage(
                                    "${it1?.icon}",
                                    contentDescription = "보석 아이콘",
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
                                                val js = JSONObject(tootipParse(gemslist[0].Gems[if(index == -1) 0 else index]?.tooltip?: ""))
                                                val EffectJs = JSONObject(tootipParse(gemslist[0].Effects[if (index == -1) 0 else index]?.tooltip?: ""))
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
                                                                    contentDescription = "장비 아이콘"
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
                                                items(gemslist[i].Effects) {
                                                    if(gemslist[0].Gems[if(index == -1) 0 else index]?.slot == it?.gemSlot) {
                                                        Log.d("로그로그1", "${it1?.slot}  ${it?.gemSlot}")
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
                                                                            contentDescription = "스킬 아이콘"
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
                                                                                contentDescription = "스킬 아이콘"
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
                                                                                }")
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
                                                                                contentDescription = "스킬 아이콘"
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
                                                                                contentDescription = "스킬 아이콘"
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
@Composable
fun avatar(avatarslist: SnapshotStateList<charterAvatars>, charterProfile: charterProfile) {
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
        var index by remember { mutableStateOf(-1) }
        AsyncImage(model = "${charterProfile.CharacterImage}", contentDescription = "캐릭터 이미지", modifier = Modifier.fillMaxSize())
        LazyVerticalGrid(columns = GridCells.Fixed(2),
            Modifier
                .fillMaxSize()
                .padding(top = 40.dp)
                .align(Alignment.BottomCenter)
            ,horizontalArrangement = Arrangement.Center,verticalArrangement = Arrangement.spacedBy(16.dp),) {
            itemsIndexed(avatarslist.toList()) { idx, it ->
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
                            AsyncImage(model = value.getJSONObject("value").getJSONObject("slotData").getString("iconPath"), contentDescription = "장비 아이콘", modifier = Modifier.fillMaxSize())
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
                                    val js = JSONObject(avatarslist.toList()[if(index == -1) 0 else index].Tooltip?.let { it1 -> tootipParse(it1) })
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
                                                        contentDescription = "장비 아이콘"
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
@Composable
fun equipment(equipmentlist: SnapshotStateList<charterEquipment>, charterProfile: charterProfile) {
    // openDialog.value = !openDialog.value
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
        var index by remember { mutableStateOf(-1) }
        AsyncImage(model = "${charterProfile.CharacterImage}", contentDescription = "캐릭터 이미지", modifier = Modifier.fillMaxSize())
        LazyVerticalGrid(columns = GridCells.Fixed(2),
            Modifier
                .fillMaxSize()
                .padding(top = 40.dp)
                .align(Alignment.BottomCenter)
            ,horizontalArrangement = Arrangement.Center,verticalArrangement = Arrangement.spacedBy(16.dp),) {
            itemsIndexed(equipmentlist.toList()) { idx, it ->
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
                                    Log.d("애미야", "${index}")
                                }) {
                            AsyncImage(model = value.getJSONObject("value").getJSONObject("slotData").getString("iconPath"), contentDescription = "장비 아이콘", modifier = Modifier.fillMaxSize())
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
                                    val js = JSONObject(equipmentlist.toList()[if(index == -1) 0 else index].Tooltip?.let { it1 -> tootipParse(it1) })
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
                                                        contentDescription = "장비 아이콘"
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
    Log.d("씨이빠", "${str2}")
    return str2
}
fun tootipParse (str: String):String {
    val str1 = str.replace("\\r\\n", "")
    Log.d("adsdasasd", "${str1.replace("\\\"", "\"")}")
    return str1.replace("\\\"", "\"")
}
@ExperimentalPagerApi
@Composable
fun profileDetails(colosseums: SnapshotStateList<charterColosseums>, charterProfile: charterProfile) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
        LazyColumn(Modifier.padding(16.dp)) {
            item {
                Column() {
                    Text("서버 이름 : ${charterProfile.ServerName?: ""}")
                    Text("길드 이름 : ${charterProfile.GuildName?: ""}")
                    Text("길드 역할 : ${charterProfile.GuildMemberGrade?: ""}")
                    Text("원정대 레벨 : ${charterProfile.ExpeditionLevel?: ""}")
                    Text("PVP 등급 : ${charterProfile.PvpGradeName?: ""}")
                    Text("영지 이름 : ${charterProfile.TownName?: ""}")
                    Text("영지 레벨 : ${charterProfile.TownLevel?: ""}")
                    Text("성향")
                    for (i in charterProfile.Tendencies.indices) {
                            Text( "${charterProfile.Tendencies[i].type?: ""} : ${charterProfile.Tendencies[i].Point?: 0} / ${charterProfile.Tendencies[i].MaxPoint?: 0}")
                    }
                    Text("능력치")
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
                                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "열기")
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
            items(colosseums.toList()) {
                Column(Modifier.fillMaxWidth()) {
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        Text("${it.Colosseums.last().seasonName}")
                        Text("현재 순위 : ${it.Rank?: "없음"}")
                    }
                    HorizontalPager( // 가로로 스크롤 가능한 pager
                        count = it.Colosseums.size, // 페이지 수
                        state = pagerState // PagerState
                    ) { page ->
                        // 페이지 content
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(150.dp)) {
                            Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                                Column() {
                                    if (it.Colosseums[pagerState.currentPage].competitive == null) {
                                        Text("시즌정보없음")
                                    } else {
                                        Column() {
                                            AsyncImage(it.Colosseums[pagerState.currentPage].competitive?.rankIcon, contentDescription = "랭크")
                                            Text("${it.Colosseums[pagerState.currentPage].competitive?.rankName}")
                                        }
                                        Text("판수 : ${it.Colosseums[pagerState.currentPage].competitive?.playCount}")
                                        Text("승패 : ${it.Colosseums[pagerState.currentPage].competitive?.victoryCount} / ${it.Colosseums[pagerState.currentPage].competitive?.loseCount}")
                                    }
                                }
                                Column() {
                                    if(it.Colosseums[pagerState.currentPage].teamDeathmatch == null) {
                                        Text("팀데스매치 정보없음")
                                    } else {
                                        Text("판수 : ${it.Colosseums[pagerState.currentPage].teamDeathmatch?.playCount}")
                                        Text("승패 : ${it.Colosseums[pagerState.currentPage].teamDeathmatch?.victoryCount} / ${it.Colosseums[pagerState.currentPage].teamDeathmatch?.loseCount}")
                                    }

                                    if(it.Colosseums[pagerState.currentPage].deathmatch == null) {
                                        Text("데스매치 정보없음")
                                    } else {
                                        Text("판수 : ${it.Colosseums[pagerState.currentPage].deathmatch?.playCount}")
                                        Text("승패 : ${it.Colosseums[pagerState.currentPage].deathmatch?.victoryCount} / ${it.Colosseums[pagerState.currentPage].deathmatch?.loseCount}")
                                    }


                                    if(it.Colosseums[pagerState.currentPage].teamElimination == null) {
                                        Text("대장전 정보없음")
                                    } else {
                                        Text("판수 : ${it.Colosseums[pagerState.currentPage].teamElimination?.playCount}")
                                        Text("승패 : ${it.Colosseums[pagerState.currentPage].teamElimination?.victoryCount} / ${it.Colosseums[pagerState.currentPage].teamElimination?.loseCount}")
                                    }

                                    if(it.Colosseums[pagerState.currentPage].coOpBattle == null) {
                                        Text("시즌정보없음")
                                    } else {
                                        Text("판수 : ${it.Colosseums[pagerState.currentPage].coOpBattle?.playCount}")
                                        Text("승패 : ${it.Colosseums[pagerState.currentPage].coOpBattle?.victoryCount} / ${it.Colosseums[pagerState.currentPage].coOpBattle?.loseCount}")
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
                                    if(pagerState.currentPage <= 0) return@launch
                                    // animateScrollToPage 함수는 suspend function 이기 때문에 Coroutine scope 내에서 호출해야 함
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            },
                            ) {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "이전")
                        }
                        Text("${it.Colosseums[pagerState.currentPage].seasonName}", Modifier.padding(top = 14.dp))
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    if(pagerState.currentPage >= it.Colosseums.size) return@launch
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }

                            }
                        ) {
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "다음")
                        }
                    }
                }
            }
        }
}

@Composable
fun collection(items: List<@Composable () -> Unit>) {
    val text = listOf("카드", "수집품")
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
