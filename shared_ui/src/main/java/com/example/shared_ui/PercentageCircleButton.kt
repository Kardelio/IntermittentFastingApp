package com.example.shared_ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PercentageCircleButton(
    modifier: Modifier = Modifier.size(100.dp),
    content: (@Composable () -> Unit)? = null,
    text: String = "test",
    padding: Float = 10f,
    baseThickness: Float = 10f,
    overlayThickness: Float = 30f,
    backgroundColor: Color = Color.LightGray,
    foregroundColor: Color = Color.Blue,
    currentPercent: Float = 0.1f,
    circleSize: Float? = null,
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
    Box(
        modifier = modifier
            .shadow(elevation = 8.dp, shape = CircleShape)
            .clip(shape = CircleShape)
            .background(color = Color.White, shape = CircleShape)
            .clickable {
                onClick()
            }, contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = modifier.clip(CircleShape)) {
            drawArc(
                color = backgroundColor,
                -90f,
                360f,
                false,
                topLeft = Offset(
                    padding / 2 + (baseThickness / 2),
                    padding / 2 + (baseThickness / 2)
                ),
                style = Stroke(width = baseThickness, cap = StrokeCap.Round),
                size = Size(
                    size.width - padding - (baseThickness),
                    size.height - padding - (baseThickness)
                )
            )
            if(currentPercent < 1f){
                drawArc(
                    color = foregroundColor,
                    -90f,
                    animat.value,
                    false,
                    topLeft = Offset(
                        padding / 2 + (baseThickness / 2),
                        padding / 2 + (baseThickness / 2)
                    ),
                    style = Stroke(width = overlayThickness, cap = StrokeCap.Round),
                    size = Size(
                        size.width - padding - (baseThickness),
                        size.height - padding - (baseThickness)
                    )
                )
            }
//            if (animat.value <= 360) {
//                drawArc(
//                    color = foregroundColor,
//                    -90f,
//                    animat.value,
//                    false,
//                    topLeft = Offset(
//                        padding / 2 + (baseThickness / 2),
//                        padding / 2 + (baseThickness / 2)
//                    ),
//                    style = Stroke(width = overlayThickness, cap = StrokeCap.Round),
//                    size = Size(
//                        size.width - padding - (baseThickness),
//                        size.height - padding - (baseThickness)
//                    )
//                )
//            }
            if (circleSize != null) {
                drawPoints(
                    points = listOf(Offset(100f, 100f)),
                    pointMode = PointMode.Points,
                    color = Color.Blue,
                    strokeWidth = circleSize,
                    cap = StrokeCap.Round
                )
            }
        }
        if (content != null) {
            content()
        }

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
fun PercentageCircleButtonPreview() {
    Column {
        val p by remember {
            mutableStateOf(0.5f)
        }
        PercentageCircleButton(
            currentPercent = p, content = {
                Text(text = "This is test")
            },
            circleSize = 100f
        )
        PercentageCircleButton(padding = 10f, baseThickness = 50f, currentPercent = p)
        PercentageCircleButton(padding = 10f, baseThickness = 50f)
        PercentageCircleButton(padding = 30f, baseThickness = 50f, currentPercent = p)
    }
}

@Preview(showSystemUi = true)
@Composable
fun FullScreenPercentageCircleButtonPreview() {
    val p by remember {
        mutableStateOf(0.5f)
    }
    PercentageCircleButton(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(16.dp),
        padding = 30f,
        baseThickness = 50f,
        currentPercent = p
    )
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