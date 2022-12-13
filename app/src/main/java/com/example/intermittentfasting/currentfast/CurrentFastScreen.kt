package com.example.intermittentfasting.currentfast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.intermittentfasting.manualentry.DateAndTimePicker
import com.example.intermittentfasting.utils.TimeUtils
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
                while(true) {
                    passed = vm.getTimePassed()
                    delay(1000)
                }
            }
            Text("Currently FASTING: Started on ${fast?.startTime}")
            Text("Time elapsed: ${passed}")
        } else {
            Text("Currently FEEDING")
        }
        Button(onClick = { vm.toggleFast() }) {
            if (fast != null) {
                Text("---")
            }
            Text("start or stop")

        }

        Spacer(modifier = Modifier.height(32.dp))
        if (!isActive) {
            DateAndTimePicker(
                "Forgot to start START",
                cleanedUpStart
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
                "Forgot to start END",
                cleanedUpEnd
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