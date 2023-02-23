package com.example.shared_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FastingTitle(currentlyFasting: Boolean) {
    if (currentlyFasting) {
        Text(
            "FASTING",
            style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold, color = ifred)
        )
    } else {
        Text(
            "FEEDING",
            style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold, color = ifgreen)
        )
    }
}

@Composable
fun TitleAndTime(title: String, time: String, smaller: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            title,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = if (smaller) 18.sp else 24.sp,
                color = if (smaller) Color.LightGray else Color.Black
            )
        )
        Text(
            time,
            style = TextStyle(
                fontSize = if (smaller) 18.sp else 24.sp,
                color = if (smaller) Color.LightGray else Color.Black
            )
        )
    }
}

//@Composable
//fun ElapsedAndRemaining(elapsed: String, remaining: String) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(IntrinsicSize.Min),
//        horizontalArrangement = Arrangement.SpaceEvenly,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        TitleAndTime("Elapsed", elapsed)
//        Spacer(
//            modifier = Modifier
//                .fillMaxHeight(0.8f)
//                .width(1.dp)
//                .background(color = Color.Black)
//        )
//        TitleAndTime("Remaining", remaining)
//    }
//}
@Composable
fun ElapsedAndRemaining(elapsed: String, remaining: String) {
    Column(
        modifier = Modifier
            .width(IntrinsicSize.Max),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleAndTime("Done", elapsed, true)
        Spacer(modifier = Modifier.height(4.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(2.dp)
                .background(color = Color.LightGray)
        )
        Spacer(modifier = Modifier.height(4.dp))
        TitleAndTime("Todo", remaining)
    }
}

@Composable
fun FastingInfoBlock(
    currentlyFasting: Boolean,
    elapsed: String? = null,
    remaining: String? = null
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (currentlyFasting) {

            ElapsedAndRemaining(elapsed ?: "", remaining ?: "")
            Spacer(modifier = Modifier.height(12.dp))
        }
        FastingTitle(currentlyFasting = currentlyFasting)
    }
}

//===============================================
//================= PREVIEWS ====================
//===============================================

@Preview
@Composable
fun FastingTitlePreview() {
    Column {
        FastingTitle(true)
        FastingTitle(false)
    }
}

@Preview(showSystemUi = true)
@Composable
fun FastingTitleWithinPercentageButtonPreview() {
    PercentageCircleButton(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(16.dp),
        content = {
            FastingInfoBlock(currentlyFasting = true)
        },
        padding = with(LocalDensity.current) { 12.dp.toPx() },
        baseThickness = with(LocalDensity.current) { 32.dp.toPx() },
        overlayThickness = with(LocalDensity.current) { 28.dp.toPx() },
        currentPercent = 0.5f
    ) {
//            perr += 0.01f
    }
}


@Preview
@Composable
fun ElapsedAndRemainingPreview() {
    ElapsedAndRemaining("abc", "gsdgs")
}
