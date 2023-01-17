package com.example.intermittentfasting.currentfast

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chargemap.compose.numberpicker.NumberPicker
import com.example.intermittentfasting.ui.theme.IntermittentFastingTheme
import com.example.shared_ui.DateAndTimePicker
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
//    var targetHours by rememberSaveable {
//        mutableStateOf(16)
//    }
    val locale = LocalContext.current.resources.configuration.locales[0]
    val isActive by remember {
        derivedStateOf {
            return@derivedStateOf fast != null && fast!!.isActive
        }
    }

    Column {
        if (isActive) {
            var passed by remember { mutableStateOf("") }
            LaunchedEffect(Unit) {
                while (true) {
                    passed = vm.getTimePassed()
                    delay(1000)
                }
            }
            Text("Currently FASTING: Started on ${fast?.startTime}")
            Text("Time elapsed: ${passed}")
        } else {
            Text("Currently FEEDING")
        }
        //TODO Target hours
//        NumberPicker(number = targetHours, onChange = {
//
//            targetHours = it
//        })
        if (!isActive) {
            NumberPicker(
                value = targetHours,
                range = 1..24,
                onValueChange = {
                    vm.setTargetHours(it)
//                    targetHours = it
                }
            )
        }

        BigFastButton("toggle") {
            vm.toggleFast()
        }
//        Button(onClick = { vm.toggleFast() }) {
//            if (fast != null) {
//                Text("---")
//            }
//            Text("start or stop")
//
//        }

        Spacer(modifier = Modifier.height(32.dp))
        if (!isActive) {
            /*
                modifier: Modifier = Modifier,
                    label: String,
                        currentDateString: String,
                            onValueChange: (Int, Int, Int, Int, Int) -> Unit

            */
            DateAndTimePicker(
                label = "Forgot to start START",
                currentDateString = cleanedUpStart
            ) { date, month, year, hour, min ->
                cleanedUpStart =
                    TimeUtils.convertStringsToUTCString(locale, date, month, year, hour, min)
            }
            Button(onClick = {
                vm.submitForgottenStart(cleanedUpStart)
            }) {
                Text("Submit")
            }
        } else {
            DateAndTimePicker(
                label = "Forgot to start END",
                currentDateString = cleanedUpEnd
            ) { date, month, year, hour, min ->
                cleanedUpEnd =
                    TimeUtils.convertStringsToUTCString(locale, date, month, year, hour, min)
            }
            Button(onClick = {
                vm.submitForgottenEnd(cleanedUpEnd)
            }) {
                Text("Submit")
            }

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