package com.example.intermittentfasting.currentfast

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chargemap.compose.numberpicker.NumberPicker
import com.example.intermittentfasting.ui.theme.IntermittentFastingTheme
import com.example.shared_ui.DateAndTimePicker
import com.example.shared_ui.ProgressBar
import com.example.utils.TimeUtils
import kotlinx.coroutines.delay

@Composable
fun CurrentFastScreen(
    vm: CurrentFastViewModel = hiltViewModel()
) {
    val fast by vm.currentFast.collectAsState()
    var cleanedUpEnd by remember {
        mutableStateOf("")
    }
    var cleanedUpStart by remember {
        mutableStateOf("")
    }
    val targetHours by remember {
        vm.targetHours
    }
    val locale = LocalContext.current.resources.configuration.locales[0]
    val isActive by remember {
        derivedStateOf {
            return@derivedStateOf fast != null && fast!!.isActive
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (isActive) {
//            var passed by remember { mutableStateOf("") }
//            var remaining by remember { mutableStateOf("") }
//            LaunchedEffect(Unit) {
//                while (true) {
//                    passed = vm.getTimePassed()
//                    remaining = vm.getTimeRemaining()
//                    delay(1000)
//                }
//            }
//            Text("Currently FASTING: Started on ${fast?.startTime}")
//            Text("Time elapsed: ${passed}")
//            Text("Target Hours: ${fast?.targetHours}")
//            Text("End Time: ${fast?.endTimeToDisplay}")
//            Text("Time remaining till target: ${remaining}")
            fast?.let {
                CurrentlyFasting(
                    it.startTime,
                    vm::getTimePassed,
                    vm::getTimeRemaining,
                    it.targetHours,
                    it.endTimeToDisplay
                )
            }
        } else {
            CurrentlyFeeding(targetHours = targetHours) {
                vm.setTargetHours(it)
            }
        }
        BigFastButton("toggle") {
            vm.toggleFast()
        }
        Spacer(modifier = Modifier.height(32.dp))
        DateAndTimePicker(
            label = "Forgot to " + if (!isActive) "Start" else "End",
            currentDateString = if (!isActive) cleanedUpStart else cleanedUpEnd,
        ) { date, month, year, hour, min ->
            if (!isActive) {
                cleanedUpStart =
                    TimeUtils.convertStringsToUTCString(locale, date, month, year, hour, min)
            } else {
                cleanedUpEnd =
                    TimeUtils.convertStringsToUTCString(locale, date, month, year, hour, min)
            }
        }
        Button(onClick = {
            if (!isActive) {
                vm.submitForgottenStart(cleanedUpStart)
            } else {
                vm.submitForgottenEnd(cleanedUpEnd)
            }
        }) {
            Text("Submit")
        }
    }
}

@Composable
fun CurrentlyFeeding(targetHours: Int, setTargetHours: (Int) -> Unit) {
    //TODO show time when you should start next fast
    Column {
        Text("Currently FEEDING")
        NumberPicker(
            value = targetHours,
            range = 1..24,
            onValueChange = {
                setTargetHours(it)
            }
        )
    }
}

@Composable
fun CurrentlyFasting(
    startTime: String,
    getTimePassed: () -> String,
    getTimeRemaining: () -> String,
    targetHours: Int,
    endTime: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var passed by remember { mutableStateOf("") }
        var remaining by remember { mutableStateOf("") }
        LaunchedEffect(Unit) {
            while (true) {
                passed = getTimePassed()
                remaining = getTimeRemaining()
                delay(1000)
            }
        }
        Text("Fasting!")
        Text("Start: ${startTime}")
        Text("  End: ${endTime}")
        Text("Target Hours: ${targetHours}")
        ElapsedAndRemaining(elapsed = passed, remaining = remaining)
        var perr by remember {
            mutableStateOf(0.9f)
        }
        ProgressBar(
            modifier = Modifier.fillMaxWidth().aspectRatio(1f).padding(16.dp),
            padding = with(LocalDensity.current) { 8.dp.toPx() },
            baseThickness = with(LocalDensity.current) { 20.dp.toPx() },
            overlayThickness = with(LocalDensity.current) { 20.dp.toPx() },
            currentPercent = perr
        ) {
            perr += 0.01f
        }
        ProgressBar(
            modifier = Modifier.size(100.dp),
            padding = 10f,
            baseThickness = with(LocalDensity.current) { 8.dp.toPx() },
            overlayThickness = with(LocalDensity.current) { 12.dp.toPx() },
            backgroundColor = Color.Black,
            foregroundColor = Color.Green,
            currentPercent = perr
        ) {
            perr += 0.01f
        }
//        Text("Time elapsed: ${passed}")
//        Text("End Time: ${endTime}")
//        Text("Time remaining till target: ${remaining}")
    }
}

@Composable
fun ElapsedAndRemaining(elapsed: String, remaining: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TitleAndTime("Elapsed", elapsed)
        Spacer(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .width(1.dp)
                .background(color = Color.Black)
        )
        TitleAndTime("Remaining", remaining)
    }
}

@Preview
@Composable
fun ElapsedAndRemainingPreview() {
    ElapsedAndRemaining("abc", "gsdgs")
}

@Composable
fun TitleAndTime(title: String, time: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
        Text(time, style = TextStyle(fontSize = 20.sp))
    }
}


@Composable
fun BigFastButton(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(color = Color.Red)
            .border(2.dp, Color.DarkGray, shape = CircleShape)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center

    ) {
        Text(label)
    }
}

@Preview
@Composable
fun BigFastButtonPreview() {
    BigFastButton("Test") {}
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DateAndTimePickerPreview() {
    IntermittentFastingTheme {

        Box(modifier = Modifier.padding(20.dp)) {
            DateAndTimePicker(
                label = "test",
                currentDateString = "date 123 456",
                onValueChange = { _, _, _, _, _ -> })
        }
    }

}