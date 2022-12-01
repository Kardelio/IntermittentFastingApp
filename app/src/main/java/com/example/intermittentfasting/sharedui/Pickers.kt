package com.example.intermittentfasting.sharedui

//@Composable
//fun DateRangePicker(
//    modifier: Modifier = Modifier,
//    label: String,
//    titleText: String,
//    undefinedText: String = "undefined",
//    currentDateRange: Pair<Long, Long>?,
//    onDateRangeChanged: (Pair<Long, Long>) -> Unit
//) {
//    val context = LocalContext.current
//    OutlinedBox(
//        modifier = modifier,
//        label = label,
//        onClick = {
//            val picker = MaterialDatePicker.Builder
//                .dateRangePicker()
//                .setTitleText(titleText)
//                .build()
//            (context as? AppCompatActivity)?.let { cxt ->
//                picker.show(cxt.supportFragmentManager, picker.toString())
//                picker.addOnPositiveButtonClickListener {
//                    if (it != null) {
//                        onDateRangeChanged(it.toKotlinPair())
//                    }
//                }
//            }
//
//        }) {
//        currentDateRange?.let {
//            val fullRange =
//                "${DateUtils.simpleDateOnlyFormatter.format(Date(it.first))} - ${
//                    DateUtils.simpleDateOnlyFormatter.format(
//                        Date(it.second)
//                    )
//                }"
//            Text(
//                text = fullRange,
//                style = TextStyle(fontSize = 20.sp)
//            )
//        } ?: run {
//            Text(
//                undefinedText,
//                style = TextStyle(fontSize = 20.sp, color = Color.LightGray)
//            )
//        }
//    }
//}
//
//@Composable
//fun TimePicker(
//    modifier: Modifier = Modifier,
//    label: String,
//    titleText: String,
//    undefinedText: String = "undefined",
//    currentStartTime: String?,
//    onTimeChanged: (String) -> Unit
//) {
//    val context = LocalContext.current
//    OutlinedBox(modifier = modifier, label = label, onClick = {
//        val pickerTime =
//            MaterialTimePicker.Builder()
//                .setTimeFormat(TimeFormat.CLOCK_12H)
//                .setTitleText(titleText)
//                .build()
//        (context as? AppCompatActivity)?.let { cxt ->
//            pickerTime.show(cxt.supportFragmentManager, pickerTime.toString())
//            pickerTime.addOnPositiveButtonClickListener {
//                onTimeChanged("${pickerTime.hour}:${pickerTime.minute}")
//            }
//        }
//    }) {
//        currentStartTime?.let {
//            Text(
//                text = it,
//                style = TextStyle(fontSize = 20.sp)
//            )
//        } ?: run {
//            Text(
//                text = undefinedText,
//                style = TextStyle(fontSize = 20.sp, color = Color.LightGray)
//            )
//        }
//    }
//}
