package com.example.intermittentfasting.manualentry

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shared_ui.DateAndTimePicker
import com.example.utils.TimeUtils

@Composable
fun ManualEntryScreen(
    vm: ManualEntryViewModel = hiltViewModel()
) {
    var cleanedUpStart by remember {
        mutableStateOf("")
    }
    var cleanedUpEnd by remember {
        mutableStateOf("")
    }
    val locale = LocalContext.current.resources.configuration.locales[0]

    Column {
        DateAndTimePicker(
            label = "Start",
            currentDateString = cleanedUpStart
        ) { date, month, year, hour, min ->
            cleanedUpStart =
                TimeUtils.convertStringsToUTCString(locale, date, month, year, hour, min)
        }
        DateAndTimePicker(
            label = "End",
            currentDateString = cleanedUpEnd
        ) { date, month, year, hour, min ->
            cleanedUpEnd =
                TimeUtils.convertStringsToUTCString(locale, date, month, year, hour, min)
        }
        Button(onClick = {
            vm.submitFast(cleanedUpStart, cleanedUpEnd)
        }) {
            Text("Submit")

        }
    }
}


