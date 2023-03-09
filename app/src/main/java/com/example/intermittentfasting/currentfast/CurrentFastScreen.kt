package com.example.intermittentfasting.currentfast

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import com.example.shared_ui.ElapsedAndRemaining
import com.example.shared_ui.FastingInfoBlock
import com.example.shared_ui.FastingTitle
import com.example.shared_ui.PercentageCircleButton
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
                    modifier = Modifier.weight(1f),
                    it.startTime,
                    vm::getTimePassed,
                    vm::getTimeRemaining,
                    vm::getCurrentFastPercentage,
                    it.targetHours,
                    it.endTimeToDisplay
                ) {
                    vm.toggleFast()
                }
            }
        } else {
            CurrentlyFeeding(modifier = Modifier.weight(1f), targetHours = targetHours, {
                vm.setTargetHours(it)
            }) {
                vm.toggleFast()
            }
        }
        CurrentFastFooter(fast?.startTime ?: "", fast?.endTimeToDisplay ?: "")
        DateAndTimePicker(
            modifier = Modifier.padding(vertical = 8.dp),
            label = "Forgot to " + if (!isActive) "Start" else "End",
            currentDateString = if (!isActive) cleanedUpStart else cleanedUpEnd,
            onSubmit = {
                if (!isActive) {
                    vm.submitForgottenStart(cleanedUpStart)
                } else {
                    vm.submitForgottenEnd(cleanedUpEnd)
                }
            }
        ) { date, month, year, hour, min ->
            if (!isActive) {
                cleanedUpStart =
                    TimeUtils.convertStringsToUTCString(locale, date, month, year, hour, min)
            } else {
                cleanedUpEnd =
                    TimeUtils.convertStringsToUTCString(locale, date, month, year, hour, min)
            }
        }
    }
}

@Composable
fun CurrentFastFooter(startTime: String, endTime: String) {
    Text("Start Time: ${startTime}")
    Text("End Time: ${endTime}")
}

@Composable
fun CurrentFastTopInfoHeader() {

}

@Composable
fun CurrentlyFeeding(
    modifier: Modifier = Modifier,
    targetHours: Int,
    setTargetHours: (Int) -> Unit,
    onButtonClick: () -> Unit
) {
    //TODO show time when you should start next fast
    Column(
        modifier = modifier.background(color = Color.Red), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
//        Text("Currently FEEDING")
        NumberPicker(
            value = targetHours,
            range = 1..24,
            onValueChange = {
                setTargetHours(it)
            }
        )
//        PercentageCircleButton() {
//            onButtonClick()
//
//        }
        PercentageCircleButton(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(16.dp),
            content = {
                FastingInfoBlock(currentlyFasting = false)
            },
            padding = with(LocalDensity.current) { 12.dp.toPx() },
            baseThickness = with(LocalDensity.current) { 32.dp.toPx() },
            overlayThickness = with(LocalDensity.current) { 28.dp.toPx() },
            currentPercent = 1f
        ) {
//            perr += 0.01f
            onButtonClick()
        }
    }
}

@Composable
fun CurrentlyFasting(
    modifier: Modifier = Modifier,
    startTime: String,
    getTimePassed: () -> String,
    getTimeRemaining: () -> String,
    getCurrentFastPercentage: () -> Float,
    targetHours: Int,
    endTime: String,
    onButtonClick: () -> Unit
) {
    Column(
        modifier = modifier.background(color = Color.Red),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var passed by remember { mutableStateOf("") }
        var remaining by remember { mutableStateOf("") }
        var percentageOfFast by remember { mutableStateOf(0f) }
        LaunchedEffect(Unit) {
            while (true) {
                passed = getTimePassed()
                remaining = getTimeRemaining()
                percentageOfFast = getCurrentFastPercentage()
                println(percentageOfFast)
                delay(1000)
            }
        }
//        Text("Fasting!")
//        Text("Start: ${startTime}")
//        Text("  End: ${endTime}")
//        Text("Target Hours: ${targetHours}")
//        ElapsedAndRemaining(elapsed = passed, remaining = remaining)
        PercentageCircleButton(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(16.dp),
            content = {
                FastingInfoBlock(currentlyFasting = true, passed, remaining)
            },
            padding = with(LocalDensity.current) { 12.dp.toPx() },
            baseThickness = with(LocalDensity.current) { 32.dp.toPx() },
            overlayThickness = with(LocalDensity.current) { 28.dp.toPx() },
            currentPercent = percentageOfFast
        ) {
//            perr += 0.01f
            onButtonClick()
        }
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