package com.example.intermittentfasting.manualentry

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.intermittentfasting.utils.TimeUtils

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
        DateAndTimePicker("Start", cleanedUpStart) { date, month, year, hour, min ->
            cleanedUpStart =
                TimeUtils.convertStringsToUTCString(locale, date, month, year, hour, min)
        }
        DateAndTimePicker("End", cleanedUpEnd) { date, month, year, hour, min ->
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


@Composable
fun DateAndTimePicker(
    label: String,
    currentDateString: String,
    onValueChange: (Int, Int, Int, Int, Int) -> Unit
) {
    val context = LocalContext.current
    Row {
        Text(text = label)
        Button(onClick = {

            val dp = DatePickerDialog(
                context
            )
            dp.setOnDateSetListener { _, year, month, date ->
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        onValueChange(date, month, year, hour, minute)
                    },
                    0, 0, true
                ).show()
            }
            dp.show()


        }) {
            Text(text = "-->${currentDateString}")
        }
    }
}