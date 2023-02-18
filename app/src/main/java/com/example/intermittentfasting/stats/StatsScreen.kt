package com.example.intermittentfasting.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.intermittentfasting.domain.StatBlock
import com.example.intermittentfasting.model.Fast
import com.example.intermittentfasting.model.PastFast
import com.example.intermittentfasting.pastfasts.SingleFastCard
import com.example.utils.TimeUtils

@Composable
fun StatsScreen(
    vm: StatsViewModel = hiltViewModel()
) {
    val stats = vm.stats.collectAsState()
    if (stats.value != null) {
        StatsScreenContents(stats.value!!)
    } else {

    }
}

@Composable
fun StatsScreenContents(stats: StatBlock) {
    val locale = LocalContext.current.resources.configuration.locales[0]
    Column {
        //TODO handle no stats situation
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

//@Preview
//@Composable
//fun StatsScreenContentsPreview() {
//    StatsScreenContents(
//        StatBlock(10, Fast())
//    )
//}