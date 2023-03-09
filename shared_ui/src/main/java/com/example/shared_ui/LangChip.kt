package com.example.shared_ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun LangChipPrev() {
    LanguageChip(language = "rwatr", select = {})
}


@Composable
fun LanguageChip(language: String, select: (String) -> Unit) {
    Box(
        modifier = Modifier
            .testTag("Language Chip [asdsa]")
            .padding(8.dp)
            .selectable(
                selected = true,
                interactionSource = MutableInteractionSource(),
                indication = null,
            ) {
                select(language)
            }
    ) {
        Icon(
            imageVector = Icons.Default.HideImage,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(
                    ratio = 2f,
                    matchHeightConstraintsFirst = false
                )
                .background(
                    colorResource(id = android.R.color.darker_gray),
                    RoundedCornerShape(size = 8.dp)
                )
                .clip(RoundedCornerShape(size = 8.dp)),
            contentDescription = "Test Image"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            textAlign = TextAlign.Start,
            text = "teat",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopEnd)
        ) {
            Image(
                imageVector = Icons.Default.ImageNotSupported,
                contentDescription = "Checked",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}