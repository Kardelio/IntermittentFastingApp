package com.example.shared_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//@Composable
//fun NumberPicker(number: Int, onChange: (Int) -> Unit) {
//
//    BasicTextField(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .clip(RoundedCornerShape(4.dp))
//            .background(Color.White)
//            .padding(4.dp),
//        textStyle = TextStyle(fontSize = 24.sp),
//        value = "${number}", onValueChange = {
//            try {
//                val converted = it.toInt()
//                onChange(converted)
//            } catch (e: Exception) {
//                onChange(0)
//            }
//        }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
//    )
////    var text = remember { mutableStateOf("")}
////
////    val change : (String) -> Unit = { it ->
////        value.value = it    // you have this which is not correct and I don't think it even compiled
////        text.value = it  // it is supposed to be this
////    }
////
////    TextField(
////        value = text.value,
////        modifier = Modifier.fillMaxWidth(),
////        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
////        onValueChange = change
////    )
//}