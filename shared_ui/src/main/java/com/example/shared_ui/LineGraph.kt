package com.example.shared_ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Point(
    val x: Float, val y: Float
)

fun generatePathWithFastData(
    data: List<Float>,
    targetData: List<Int>?,
    lastXDays: Int,
    maxWidth: Float,
    maxHeight: Float,
    curvedGraph: Boolean
): Pair<Path, List<Point>> {
    val path = Path()
    val successfulTargetBeats = mutableListOf<Point>()

    var actualXDays = lastXDays
    if (data.size < lastXDays) {
        actualXDays = data.size - 1
    }
    if (lastXDays == -1) {
        //all case
        actualXDays = data.size - 1
    }
    data.reversed().takeLast(actualXDays + 1).forEachIndexed { index, hours ->
        val y = (24 - hours) * (maxHeight / 24)
        var x = (maxWidth / actualXDays) * (index)
        if (index == 0) {
            x = 0f
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
//            path.cubicTo(x+10f, y+10f, x +10f, y +10f,x, y)
        }
        if (targetData != null) {
            if (hours >= targetData.reversed().takeLast(actualXDays + 1)[index]) {
                println(
                    "Yep: ${hours} >= ${
                        targetData.reversed().takeLast(actualXDays + 1)[index]
                    }"
                )
                successfulTargetBeats.add(Point(x, y - 50))
            }
        }
//        path.lineTo(point.x, point.y)
    }
    return Pair(path, successfulTargetBeats)
}

fun generateTargetsPath(data: List<Int>?, lastXDays: Int, maxWidth: Float, maxHeight: Float): Path {
    val path = Path()
    if (data != null) {
        var actualXDays = lastXDays
        if (data.size < lastXDays) {
            actualXDays = data.size - 1
        }
        if (lastXDays == -1) {
            //all case
            actualXDays = data.size - 1
        }
        data.reversed().takeLast(actualXDays + 1).forEachIndexed { index, target ->
            val y = (24 - target) * (maxHeight / 24)
            var x = (maxWidth / actualXDays) * (index)
            if (index == 0) {
                x = 0f
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
//            path.cubicTo(x+10f, y+10f, x +10f, y +10f,x, y)
            }
//        path.lineTo(point.x, point.y)
        }
    }
    return path
}

/*
total hours in decimal
0-24?
missed days are 0
show the last week (changeable) today - 7 days

 */

@OptIn(ExperimentalTextApi::class)
@Composable
fun LineGraph(
    lastFastLengths: List<Float>,
    lastFastTargets: List<Int>? = null,
    showYAxisLabels: Boolean = true,
    curved: Boolean = false,
    showXAmountOfDataPoints: Int = 7,
    barColor: Color = Color.Green
) {

    val tm = rememberTextMeasurer()
    Spacer(modifier = Modifier
        .padding(
            start = if (showYAxisLabels) 20.dp else 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp
        )
        .aspectRatio(3 / 2f)
        .fillMaxSize()
        .drawWithCache {
            val generatedData = generatePathWithFastData(
                lastFastLengths,
                lastFastTargets,
                showXAmountOfDataPoints,
                this.size.width,
                this.size.height,
                curved
            )
            val path = generatedData.first
            val listOfTargetBeats = generatedData.second
            val targetPath = generateTargetsPath(
                lastFastTargets,
                showXAmountOfDataPoints,
                this.size.width,
                this.size.height,
            )
            val filledPath = Path()
            filledPath.addPath(path)
            filledPath.lineTo(size.width, size.height)
            filledPath.lineTo(0f, size.height)
            filledPath.close()

            val gradBrush = Brush.verticalGradient(
                listOf(
                    barColor.copy(alpha = 0.4f), Color.Transparent
                )
            )

            onDrawBehind {
                withTransform({
//                        this.scale(-0.95f,1f)
                }) {
                    drawRect(color = Color.LightGray, style = Stroke(1.dp.toPx()))


                    //HERE
                    val vertlines = if (showXAmountOfDataPoints == -1) {
                        lastFastLengths.size
                    } else {
                        showXAmountOfDataPoints - 1
                    }
                    val verticalSize = size.width / (vertlines + 1)
                    repeat(vertlines) { i ->
                        val startX = verticalSize * (i + 1)
                        drawLine(
                            color = Color.LightGray.copy(alpha = 0.3f),
                            start = Offset(startX, 0f),
                            end = Offset(startX, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    val amountOfLinesHor = 23
                    val horizontalSize = size.height / (amountOfLinesHor + 1)
                    repeat(amountOfLinesHor) { i ->
                        val startY = horizontalSize * (i + 1)
                        drawLine(
                            color = Color.LightGray.copy(alpha = 0.3f),
                            start = Offset(0f, startY),
                            end = Offset(size.width, startY),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    val amountOfLinesHor2 = 3
                    val horizontalSize2 = size.height / (amountOfLinesHor2 + 1)
                    repeat(amountOfLinesHor2) { i ->
                        val startY = horizontalSize2 * (i + 1)
                        drawLine(
                            color = Color.LightGray,
                            start = Offset(0f, startY),
                            end = Offset(size.width, startY),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    val hortlines = 3
                    val horizontalSizeSimp = size.height / (hortlines + 1)
                    if (showYAxisLabels) {
                        repeat(hortlines + 2) { i ->
                            val t = "${24 - (i * (24 / (hortlines + 1)))}"
                            val textSize = tm.measure(buildAnnotatedString {
                                append(t)
                                toAnnotatedString()
                            }, style = TextStyle(fontSize = 12.sp)).size
//                            tm.measure(t).size.height
                            drawText(
                                tm,
                                overflow = TextOverflow.Visible,
                                text = t,
                                topLeft = Offset(
                                    -((textSize.width).toFloat() + 4.dp.toPx()),
                                    (horizontalSizeSimp * i) - (textSize.height / 2)

                                ),
                                style = TextStyle(fontSize = 12.sp, textAlign = TextAlign.End)
                            )
                        }
                    }
                }


                //drawPath
                drawPath(path, color = barColor, style = Stroke(2.dp.toPx()))
                if (lastFastTargets != null) {

                    drawPath(targetPath, color = Color.Red, style = Stroke(2.dp.toPx()))
                }
                drawPath(filledPath, brush = gradBrush, style = Fill)

                if (listOfTargetBeats.isNotEmpty()) {
                    drawPoints(
                        points = listOfTargetBeats.map { Offset(it.x, it.y) },
                        pointMode = PointMode.Points,
                        color = Color.Green,
                        strokeWidth = 20f,
                        cap = StrokeCap.Round
                    )
                }
            }
        })
}

@Preview
@Composable
fun LineGraphPreview() {
    Column {
        LineGraph(
            listOf(
                10f, 20f, 24f, 2f
            )
        )
        LineGraph(
            listOf(
                10f, 20f, 24f, 2f
            ), showYAxisLabels = false,
            barColor = Color.Blue
        )
        LineGraph(
            listOf(
                10f, 20f, 24f, 2f
            ), showYAxisLabels = false,
            barColor = Color.Red
        )
    }
}