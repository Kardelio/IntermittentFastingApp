package com.example.shared_ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ClockFace() {
    val infTransition = rememberInfiniteTransition()

    val clockAnimation by infTransition.animateFloat(
        initialValue = 0f,
        targetValue = 720f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    // Now we have the rotation animation infinite going


    var strokeWidth by remember {
        mutableStateOf(0f)
    }

    val currentHour by remember(clockAnimation) {
        derivedStateOf { clockAnimation.toInt() / 30 }
    }

    Spacer(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { strokeWidth = (it.size.width / 24).toFloat() }
            .drawBehind {
                val stepheight = size.height / 24
                val center = Offset(size.width / 2, size.height / 2)
                val endOffset = Offset(size.width / 2, size.height / 2 - calculateClockHandLength(stepheight, currentHour))

                rotate(clockAnimation, pivot = center) {
                    drawLine(
                        color = Color.Blue,
                        start = center,
                        end = endOffset,
                        strokeWidth = strokeWidth
                    )
                }
            }
    )
}

fun calculateClockHandLength(stepheight: Float, currentHour: Int): Float {
    return stepheight * if (currentHour < 12) {
        12 - 1 - currentHour
    } else {
        currentHour - 12
    }
}

@Preview(showBackground = true)
@Composable
fun ClockFacePreview() {
    // From this https://proandroiddev.com/amazing-clock-animation-with-jetpack-compose-part-1-8d143f38a3cd
    Box(modifier = Modifier.width(300.dp).height(300.dp).background(color = Color.Yellow)){
        ClockFace()
    }
}