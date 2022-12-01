package com.example.intermittentfasting.currentfast

import android.text.format.DateFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermittentfasting.domain.FastUseCase
import com.example.intermittentfasting.model.PrettyFast
import com.example.intermittentfasting.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

//class CleanedFast(
//    val startTime: String,
//    val isActive: Boolean
//)

@HiltViewModel
class CurrentFastViewModel @Inject constructor(
    private val usecase: FastUseCase,
    private val locale: Locale
) : ViewModel() {

    /*
    time fasting
    time feeding
    button to toggle
    currently fasting or feedbing
     */
//    val aa: StateFlow<Fast?> =usecase.getCurrentOrLast().stateIn(viewModelScope, SharingStarted.Lazily, null)
    private val _currentFast: MutableStateFlow<PrettyFast?> = MutableStateFlow(null)
    val currentFast: StateFlow<PrettyFast?> = _currentFast.asStateFlow()

//    private val _timePassed: MutableStateFlow<String?> = MutableStateFlow(null)
//    val timePassed: StateFlow<String?> = _timePassed.asStateFlow()

    init {
        Log.d("BK", "VM init")
//        usecase()
        getCurrentTime()
        viewModelScope.launch {
            usecase.getCurrentOrLast().collectLatest {
                if (it != null) {
                    Log.d("BK", "UPDTAED fast: ${it}")
                    _currentFast.value = PrettyFast.toPrettyFast(locale, it)
                }
            }
        }
    }

    fun getTimePassed(): String {
        //
        currentFast.value?.let {
            it.startTime
            return TimeUtils.getTimeDifferenceToNow(locale, it.explicitStart)
        } ?: run {
            return ""
        }
    }

    fun toggleFast() {
        Log.d("BK", "Fast toggled")
        val timeString = TimeUtils.getCurrentUTCTimeString(locale = locale)
        Log.d("BK", "Time Now: ${timeString}")
        viewModelScope.launch {
            usecase.toggleFast()
        }
    }

    fun submitForgottenStart(start: String) {
        viewModelScope.launch {
            usecase.manualFastEntryForForgottenStart(start)
        }
    }

    /*
    toggle captures time everytime!
    saves it in DB
    are you sure box required!
    DB
    id start end type

   started if most recent field has a start time but no stop time
   stopped if no empty field in most recent field in DB

   IDEA:
   manual entry
    past fasts graph or results
     */

    fun getCurrentTime() {
        val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
        val calendar: Calendar = Calendar.getInstance(timeZone)
        val simpleDateFormat = SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", locale)
        Log.d("BK", "UTC Time: ${simpleDateFormat.format(calendar.time)}")

        val cal = Calendar.getInstance()
        val time = cal.time
        Log.d(
            "BK",
            "Time: ${cal.toString()} - ${time.toLocaleString()} - ${DateFormat.format("", time)}"
        )
    }
}