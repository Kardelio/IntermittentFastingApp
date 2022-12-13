package com.example.intermittentfasting.manualentry

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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


@Composable
fun DateAndTimePicker(
    modifier: Modifier = Modifier,
    label: String,
    currentDateString: String,
    onValueChange: (Int, Int, Int, Int, Int) -> Unit
) {
    val context = LocalContext.current
    Box(modifier = modifier.clickable {
        val dp = DatePickerDialog(
            context
        )
        dp.setOnDateSetListener { _, year, month, date ->
            TimePickerDialog(
                context,
                { _, hour, minute ->
                    onValueChange(date, month, year, hour, minute)
                },
                TimeUtils.getHourOfTheDay(), TimeUtils.getMinuteOfTheDay(), true
            ).show()
        }
        dp.show()

    }) {
        Text(
            modifier = Modifier
                .background(color = MaterialTheme.colors.secondary, shape = CircleShape)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            text = "${currentDateString}",
            color = Color.White
        )
        Text(
            modifier = Modifier
                .offset(10.dp, -6.dp)
                .background(color = Color.Black, shape = RoundedCornerShape(4.dp))
                .padding(horizontal = 4.dp), text = label, color = Color.White
        )

    }
}

@Preview(showSystemUi = true)
@Composable
fun DateAndTimePickerPreview() {
    Box(modifier = Modifier.padding(20.dp)) {
        DateAndTimePicker(
            label = "test",
            currentDateString = "date 123 456",
            onValueChange = { _, _, _, _, _ -> })
    }
}

@Preview
@Composable
fun DateAndTimePickerPreviewFullWidth() {
    Column(modifier = Modifier.fillMaxWidth()) {
        DateAndTimePicker(
            modifier = Modifier.fillMaxWidth(),
            label = "test",
            currentDateString = "date 123 456",
            onValueChange = { _, _, _, _, _ -> })
    }
}
