package com.example.shared_ui

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ProgressBar(
    modifier: Modifier = Modifier.size(100.dp),
    content: @Composable () -> Unit,
    text: String = "test",
    padding: Float = 10f,
    baseThickness: Float = 10f,
    overlayThickness: Float = 30f,
    backgroundColor: Color = Color.LightGray,
    foregroundColor: Color = Color.Blue,
    currentPercent: Float = 0.1f,
    onClick: () -> Unit = {}
) {
    /*
    angle start to the right of the canvas is 0 to sweeping up and left -90 degrees starts at the top of the circle
     */
    val animat = remember {
        Animatable(0f)
    }
    LaunchedEffect(currentPercent) {
        animat.animateTo(3.6f * (currentPercent * 100), tween(1000))
    }
    Box(modifier = modifier
        .clip(CircleShape)
        .clickable {
            onClick()
        }, contentAlignment = Alignment.Center) {
        Canvas(modifier = modifier) {
            drawArc(
                color = backgroundColor,
                -90f,
                360f,
                false,
                topLeft = Offset(padding / 2 + (baseThickness / 2), padding / 2 + (baseThickness / 2)),
                style = Stroke(width = baseThickness, cap = StrokeCap.Round),
                size = Size(
                    size.width - padding - (baseThickness),
                    size.height - padding - (baseThickness)
                )
            )
            drawArc(
                color = foregroundColor,
                -90f,
                animat.value,
                false,
                topLeft = Offset(padding / 2 + (baseThickness / 2), padding / 2 + (baseThickness / 2)),
                style = Stroke(width = overlayThickness, cap = StrokeCap.Round),
                size = Size(
                    size.width - padding - (baseThickness),
                    size.height - padding - (baseThickness)
                )
            )
        }
        Text(text)

    }
//    Canvas(modifier = modifier
//        .clip(CircleShape)
//        .clickable {
//            onClick()
//        }) {
//        drawArc(
//            color = backgroundColor,
//            -90f,
//            360f,
//            false,
//            topLeft = Offset(padding / 2 + (baseThickness / 2), padding / 2 + (baseThickness / 2)),
//            style = Stroke(width = baseThickness, cap = StrokeCap.Round),
//            size = Size(
//                size.width - padding - (baseThickness),
//                size.height - padding - (baseThickness)
//            )
//        )
//        drawArc(
//            color = foregroundColor,
//            -90f,
//            animat.value,
//            false,
//            topLeft = Offset(padding / 2 + (baseThickness / 2), padding / 2 + (baseThickness / 2)),
//            style = Stroke(width = overlayThickness, cap = StrokeCap.Round),
//            size = Size(
//                size.width - padding - (baseThickness),
//                size.height - padding - (baseThickness)
//            )
//        )
//    }
}

@Preview
@Composable
fun ProgressBarPreview() {
    Column {
        val p by remember {
            mutableStateOf(0.5f)
        }
        ProgressBar(currentPercent = p)
        ProgressBar(padding = 10f, baseThickness = 50f, currentPercent = p)
        ProgressBar(padding = 10f, baseThickness = 50f)
        ProgressBar(padding = 30f, baseThickness = 50f, currentPercent = p)
    }
}

/*
package com.example.shared_ui

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ProgressBar(
    padding: Float = 10f,
    baseThickness: Float = 10f,
    overlayThickness: Float = 30f,
    currentPercent: Float = 0.1f,
    onClick: ()-> Unit = {}
) {
    /*
    angle start to the right of the canvas is 0 to sweeping up and left -90 degrees starts at the top of the circle
     */
//    var startingPer by remember {
//        mutableStateOf(currentPercent)
//    }
    var animat = remember {
        Animatable(0f)
    }
    LaunchedEffect(currentPercent) {
//        while (startingPer<=1f) {
//            println("ksdakhdksahkdj")
            animat.animateTo(3.6f * (currentPercent * 100), tween(1000))
//            delay(3000)
//            startingPer+=0.01f
//        }
    }
    Canvas(modifier = Modifier.size(100.dp).clickable {
        onClick()
    }) {
        drawArc(
            color = Color.White,
            -90f,
            360f,
            false,
            topLeft = Offset(padding / 2 + (baseThickness / 2), padding / 2 + (baseThickness / 2)),
            style = Stroke(width = baseThickness, cap = StrokeCap.Round),
            size = Size(
                size.width - padding - (baseThickness),
                size.height - padding - (baseThickness)
            )
        )
        drawArc(
            color = Color.Blue,
            -90f,
            animat.value,
            false,
            topLeft = Offset(padding / 2 + (baseThickness / 2), padding / 2 + (baseThickness / 2)),
            style = Stroke(width = overlayThickness, cap = StrokeCap.Round),
            size = Size(
                size.width - padding - (baseThickness),
                size.height - padding - (baseThickness)
            )
        )
    }
}

@Preview
@Composable
fun ProgressBarPreview() {
    Column {

        ProgressBar(currentPercent = 0.5f)
        ProgressBar(10f, 50f, currentPercent = 0.7f)
        ProgressBar(10f, 30f, 25f)
        ProgressBar(10f, 30f, 25f, currentPercent = 1f)
    }
}
 */