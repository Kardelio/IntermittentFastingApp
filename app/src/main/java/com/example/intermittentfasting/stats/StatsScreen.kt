package com.example.intermittentfasting.stats

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.intermittentfasting.domain.StatBlock
import com.example.intermittentfasting.model.PastFast
import com.example.intermittentfasting.pastfasts.SingleFastCard
import com.example.shared_ui.LineGraph
import com.example.utils.TimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@Composable
fun StatsScreen(
    vm: StatsViewModel = hiltViewModel()
) {
    val stats = vm.stats.collectAsState()
    if (stats.value != null) {
        StatsScreenContents(stats.value!!) {
            vm.updateStats(it)
        }
    } else {

    }
}

@Composable
fun StatsScreenContents(stats: StatBlock, toggleChanged: (Boolean) -> Unit) {
    val locale = LocalContext.current.resources.configuration.locales[0]
    var amount by remember {
        mutableStateOf(AmountToShow.ALL)
    }
    var showDays by remember {
        mutableStateOf(false)
    }
    var showTargets by remember {
        mutableStateOf(false)
    }
    var selectedCoolOption by remember {
        mutableStateOf("one")
    }
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.verticalScroll(scrollState)) {
        //TODO handle no stats situation
        LineGraph(
            stats.lastFastLengths,
            lastFastTargets = if (showTargets) stats.lastFastTargets else null,
            showXAmountOfDataPoints = amount.amount,
            lastFastDaysSinceWeekend = stats.lastFastDaysSinceWeekend,
            showDaysFlag = showDays
        )
        DisplayFastAmountButtons(
            maxAMountToShow = stats.numberOfItemsActuallyShowing,
            selectedOption = amount,
            onButtonClicked = { amount = it })
        Row(modifier = Modifier.padding(vertical = 8.dp)){
            Text(text = "Displaying: ${if (amount.amount == -1) stats.numberOfItemsActuallyShowing else amount.amount}")
            OptionSelector(options = listOf("fasts", "days"), selectedOption = if(showDays) "days" else "fasts", onTap = {
                showDays = (it == "days")
                toggleChanged(showDays)
            })
            Text(text = "Target Line:")
            OptionSelector(options = listOf("on", "off"), selectedOption = if(showTargets) "on" else "off", onTap = {
                showTargets = (it == "on")
            })
        }
        Text(text = " -> ${stats.numberOfFasts}")
        Text(text = "First Fast:")
        SingleFastCard(pastFast = PastFast.toPastFast(locale, stats.firstFast))
        Text(text = "Most Recent Fast:")
        SingleFastCard(pastFast = PastFast.toPastFast(locale, stats.mostRecentCompletedFast))
        Text(text = "Longest Fast:")
        SingleFastCard(pastFast = PastFast.toPastFast(locale, stats.longestFast))
        Text(text = "Shortest Fast:")
        SingleFastCard(pastFast = PastFast.toPastFast(locale, stats.shortestFast))
        Text(
            text = "Average Length: ${
                TimeUtils.getLengthInTimeFromLong(stats.averageLength)
            }"
        )
    }
}

enum class AmountToShow(val title: String, val amount: Int) {
    SEVEN("7", 7),
    FORTEEN("14", 14),
    THIRTY("30", 30),
    NINETY("90", 90),
    ALL("all", -1)
}

@Composable
fun DisplayFastAmountButtons(
    maxAMountToShow: Int,
    selectedOption: AmountToShow,
    onButtonClicked: (AmountToShow) -> Unit,

    ) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AmountToShow.values().forEachIndexed { i, amount ->

            if (amount.amount <= maxAMountToShow) {
                Box(modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (selectedOption.amount == amount.amount) Color.Blue else Color.Green,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .clickable { onButtonClicked(amount) }
                    .padding(8.dp), contentAlignment = Alignment.Center) {

                    Text(
                        text = amount.title,
                        style = TextStyle(fontSize = 20.sp, color = Color.White)
                    )
                }
            }
        }

    }
}

@Preview
@Composable
fun CoolSwitchPreview() {
    OptionSelector(options = listOf("one", "two", "three"), selectedOption = "two", onTap = {})
}

@Composable
fun OptionSelector(
    options: List<String>,
    selectedOption: String,
    onTap: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    require(options.size >= 2) { "This Composable requires at least 2 options" }
    require(options.contains(selectedOption)) { "The selected option is not part of the provided list" }

    val animationMillis = remember {
        300
    }
    val xOffset = remember {
        Animatable(0f)
    }
    val widthOfSelectedBar = remember {
        Animatable(0f)
    }
    val fontAlpha = remember {
        Animatable(Color.Black)
    }
    var mapOfWidths = remember {
        mutableListOf<Float>()
    }
    var selectedIndexValue by remember {
        mutableStateOf(options.indexOf(selectedOption))
    }
    LaunchedEffect(key1 = selectedOption) {
        awaitAll(
            async {
                widthOfSelectedBar.animateTo(
                    mapOfWidths[selectedIndexValue] ?: 0f,
                    animationSpec = tween(animationMillis)
                )
            },
//            async {
//                fontAlpha.animateTo(
//                    mapOfWidths[selectedIndexValue] ?: 0f,
//                    animationSpec = tween(animationMillis)
//                )
//            },
            async {
                xOffset.animateTo(
                    mapOfWidths.subList(0, selectedIndexValue).sum(),
                    animationSpec = tween(animationMillis)
                )
            }
        )
    }

    Layout(
        modifier = modifier
            .clip(
                shape = RoundedCornerShape(percent = 50)
            )
            .clickable {
                var selectedIndex = options.indexOf(selectedOption)
                selectedIndex += 1
                if (selectedIndex >= options.size) {
                    selectedIndex = 0
                }
                onTap(options[selectedIndex])
            }
            .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(50))
            .background(
                MaterialTheme.colors.surface
            ), content = {
            options.forEachIndexed { ind, opt ->
                Text(
                    modifier = Modifier
                        .layoutId("option")
                        .`if`(ind == 0) {
                            padding(start = 4.dp)
                        }
                        .`if`(ind == options.size - 1) {
                            padding(end = 4.dp)
                        }
                        .padding(horizontal = 8.dp)
                        .alpha(
                            if (selectedIndexValue == ind) 1f else 0.3f
                        ), text = opt,
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = if (selectedIndexValue == ind) Color.White else Color.Black
                    )
                )
            }
            Box(
                modifier = Modifier
                    .layoutId("select-box")
                    .background(MaterialTheme.colors.primary),
            )
        }) { measurables, constraints ->
        selectedIndexValue = options.indexOf(selectedOption)
        val placeables =
            measurables.filter { it.layoutId == "option" }.map { it.measure(constraints) }
        mapOfWidths.clear()
        placeables.forEachIndexed { index, placeable ->
            mapOfWidths.add(placeable.width.toFloat())
        }
        val h = placeables.maxOf { it.height }

        val selectorPlaceable = measurables.first { it.layoutId == "select-box" }
            .measure(
                constraints.copy(
                    minWidth = widthOfSelectedBar.value.toInt(),
                    maxWidth = widthOfSelectedBar.value.toInt(),
                    minHeight = h,
                    maxHeight = h
                )
            )

        var x = 0
        layout(placeables.sumOf { it.width }, h) {
            selectorPlaceable.place(xOffset.value.toInt(), 0)
            placeables.forEach {
                it.place(x, 0)
                x += it.width
            }
        }
    }
}

fun Modifier.`if`(
    condition: Boolean,
    then: Modifier.() -> Modifier
): Modifier =
    if (condition) {
        then()
    } else {
        this
    }
