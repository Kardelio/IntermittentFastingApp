package com.example.intermittentfasting.model

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector

data class Award(
    val title: String,
    val description: String,
    val foregroundColor: Color,
    val backgroundColor: Color,
    val backgroundShape: Shape,
    val icon: ImageVector
)

val mapOfAwards = mapOf<Int, Award>(
    1 to Award(
        title = "test",
        description = "description",
        foregroundColor = Color.Black,
        backgroundColor = Color.Red,
        backgroundShape = CircleShape,
        icon = Icons.Filled.Shield,
    ),
    2 to Award(
        title = "test",
        description = "description",
        foregroundColor = Color.Black,
        backgroundColor = Color.Red,
        backgroundShape = CircleShape,
        icon = Icons.Filled.Shield,
    ),
    3 to Award(
        title = "test",
        description = "description",
        foregroundColor = Color.Black,
        backgroundColor = Color.Red,
        backgroundShape = CircleShape,
        icon = Icons.Filled.Shield,
    ),
    4 to Award(
        title = "test",
        description = "description",
        foregroundColor = Color.Black,
        backgroundColor = Color.Red,
        backgroundShape = CircleShape,
        icon = Icons.Filled.Shield,
    ),
    5 to Award(
        title = "test",
        description = "description",
        foregroundColor = Color.Black,
        backgroundColor = Color.Red,
        backgroundShape = CircleShape,
        icon = Icons.Filled.Shield,
    ),
)