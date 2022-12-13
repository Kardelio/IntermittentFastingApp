package com.example.intermittentfasting.pastfasts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PastFastsScreen(
    vm: PastFastsViewModel = hiltViewModel()
) {
    val fasts = vm.pastfasts.collectAsState()
    
    LazyColumn{
        item {
            if(fasts.value.isNotEmpty()) {

                Button(
                    onClick = {
                        vm.exportFileToDownloads()

                    }
                ){
                    Text("Export")
                }
            } else {
                Button(
                    onClick = {

                        vm.importFileFromDownloads()
                    }
                ){
                    Text("Import from downloads")
                }

            }
        }
        items(fasts.value){
            PastFastCard(pastFast = it){
                vm.deleteFast(it)
            }
        }
    }
}