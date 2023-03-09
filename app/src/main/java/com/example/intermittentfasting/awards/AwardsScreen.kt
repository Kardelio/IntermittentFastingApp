package com.example.intermittentfasting.awards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.intermittentfasting.model.Award
import com.example.intermittentfasting.model.mapOfAwards

@Composable
fun AwardsScreen(
    vm: AwardsViewModel = hiltViewModel()
) {
    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(mapOfAwards.toList()) {
            AwardIcon(award = it.second)
        }
    }
}

@Composable
fun AwardIcon(award: Award) {
    Box(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.shadow(8.dp, shape = award.backgroundShape).background(
                shape = award.backgroundShape,
                color = award.backgroundColor
            )
        ) {

            Icon(
                modifier = Modifier.size(100.dp),
                imageVector = award.icon,
                tint = award.foregroundColor,
                contentDescription = ""
            )
        }
    }
}