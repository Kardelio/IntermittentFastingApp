package com.example.shared_ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.PathParser
import com.example.utils.TimeUtils


@Composable
fun DateAndTimePicker(
    modifier: Modifier = Modifier,
    label: String,
    currentDateString: String,
    onSubmit: (() -> Unit)? = null,
    onValueChange: (Int, Int, Int, Int, Int) -> Unit
) {
    val context = LocalContext.current
    Column(modifier = modifier.padding(horizontal = 8.dp)) {
        val darker = remember {
            mutableStateOf(true)
        }
        Text(
            modifier = Modifier
                .offset(32.dp, 1.dp)
                .background(
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(4.dp, 4.dp, 0.dp, 0.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp),
            text = label,
            color = Color.White,
            fontSize = 14.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .background(color = MaterialTheme.colorScheme.secondary, shape = CircleShape)
                    .border(2.dp, color = MaterialTheme.colorScheme.tertiary, shape = CircleShape)
                    .clip(CircleShape)
                    .clickable {
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

                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                text = "${currentDateString}",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            if (onSubmit != null) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = CircleShape
                        )
                        .border(
                            2.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                        .clickable {
                            onSubmit()
                        },
                    contentAlignment = Alignment.Center,

                    ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check_in_circle),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(32.dp)
                    )
                }
            }
        }

    }
}

val pathData = "M 100,100 L 100,0 L 30,100"

@Composable
fun AnimatedPathTest() {
    LaunchedEffect(key1 = Unit, block = {
//        CircleShape
        val a = PathParser.createPathFromPathData(pathData)
//        a.
    })
    Canvas(modifier = Modifier.size(100.dp), onDraw = {
        drawLine(color = Color.Red, start = Offset(10f, 10f), end = Offset(100f, 100f))
        drawPath(
            color = Color.Blue,
            path = PathParser.createPathFromPathData(pathData).asComposePath().apply {
            }
        )
    })
}

@Preview
@Composable
fun AnimatedPathTestPreview() {
    AnimatedPathTest()
}

@Composable
fun dasdsa() {
    LaunchedEffect(key1 = Unit, block = {
//        PathParser.crea
    })
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DateAndTimePickerPreview() {

    Box(modifier = Modifier.padding(20.dp)) {
        DateAndTimePicker(
            label = "test",
            currentDateString = "date 123 456",
            onSubmit = {},
            onValueChange = { _, _, _, _, _ -> })
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
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