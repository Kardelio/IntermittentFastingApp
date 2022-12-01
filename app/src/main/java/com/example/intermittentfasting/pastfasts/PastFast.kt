package com.example.intermittentfasting.pastfasts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intermittentfasting.model.PastFast
import com.example.intermittentfasting.model.TimeContainer

@Composable
fun PastFastCard(pastFast: PastFast, onClick: (Int) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp), elevation = 4.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = "${pastFast.length.hours}h ${pastFast.length.minutes}m ${pastFast.length.seconds}s",
                    style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold)
                )
                if (pastFast.length.days > 0) {
                    Text(
                        text = "Over ${pastFast.length.days} days",
                        style = TextStyle(fontSize = 18.sp)
                    )
                }
                Text(
                    text = "Started at: ${pastFast.startTime}",
                    style = TextStyle(fontSize = 14.sp, color = Color.Black.copy(alpha = 0.5f))
                )
                Text(
                    text = "Finished at: ${pastFast.endTime}",
                    style = TextStyle(fontSize = 14.sp, color = Color.Black.copy(alpha = 0.5f))
                )
            }
            IconButton(modifier = Modifier.padding(8.dp),onClick = { onClick(pastFast.id) }) {
                Icon(modifier = Modifier.size(48.dp), imageVector = Icons.Default.Delete, contentDescription = null)
            }

        }
    }
}

@Preview
@Composable
fun PastFastCardPreview() {
    PastFastCard(pastFast = PastFast(0, "abc", "cde", TimeContainer(2L, 3L, 4L, 5L))) {}
}