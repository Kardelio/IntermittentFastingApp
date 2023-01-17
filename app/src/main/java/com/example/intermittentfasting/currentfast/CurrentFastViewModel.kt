package com.example.intermittentfasting.currentfast

import android.text.format.DateFormat
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermittentfasting.alarm.AlarmScheduler
import com.example.intermittentfasting.data.UserRepository
import com.example.intermittentfasting.domain.FastCurrentActiveState
import com.example.intermittentfasting.domain.FastUseCase
import com.example.intermittentfasting.model.FastAlarmItem
import com.example.intermittentfasting.model.PrettyFast
import com.example.utils.TimeUtils
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
    private val userRepository: UserRepository,
    private val alarmScheduler: AlarmScheduler,
    private val locale: Locale
) : ViewModel() {

    private val _currentFast: MutableStateFlow<PrettyFast?> = MutableStateFlow(null)
    val currentFast: StateFlow<PrettyFast?> = _currentFast.asStateFlow()

    val targetHours: MutableState<Int> = mutableStateOf(16)

    val listOfCurrentAlarms = mutableListOf<Int>()

    init {
        targetHours.value = userRepository.getLastSetTargetHours()
        getCurrentTime()
        viewModelScope.launch {
            usecase.getCurrentOrLast().collectLatest {
                if (it != null) {
                    _currentFast.value = PrettyFast.toPrettyFast(locale, it)
                }
            }
        }
    }

    fun setTargetHours(hours: Int) {
        targetHours.value = hours
    }

    fun getTimePassed(): String {
        currentFast.value?.let {
            it.startTime
            return TimeUtils.getTimeDifferenceToNow(locale, it.explicitStart)
        } ?: run {
            return ""
        }
    }

    fun toggleFast() {
        val oneHourBeforeTargetFinishTime = TimeUtils.getCurrentTimePlusXHours(targetHours.value - 1)
        val targetFinishTime = TimeUtils.getCurrentTimePlusXHours(targetHours.value)
        Log.d("BK", "Targets Hours : ${targetHours.value}")
        Log.d("BK", "Targets : ${oneHourBeforeTargetFinishTime} and ${targetFinishTime}")

        userRepository.setLastTargetHours(targetHours.value)
        viewModelScope.launch {
            val activeState = usecase.toggleFast(targetHours.value)
            if (activeState == FastCurrentActiveState.NowActive) {
                Log.d("BK", "Setting alrarms for notifications")
                val hourPreTargetAlarmItem = FastAlarmItem(
                    oneHourBeforeTargetFinishTime,
                    "One Hour Left!",
                    "You made it to ${targetHours.value - 1} hours! One more hour left!",
                    targetHours.value
                )
                val targetAlarmItem = FastAlarmItem(
                    targetFinishTime,
                    "Fast Complete!",
                    "You did it! Fast is officially over! Great job! Go eat!",
                    targetHours.value
                )
                alarmScheduler.schedule(hourPreTargetAlarmItem)
                alarmScheduler.schedule(targetAlarmItem)
                listOfCurrentAlarms.add(hourPreTargetAlarmItem.hashCode())
                listOfCurrentAlarms.add(targetAlarmItem.hashCode())
            } else {
                Log.d("BK", "Cancelling any alarms")
                listOfCurrentAlarms.forEach {
                    alarmScheduler.cancel(it)
                }
                listOfCurrentAlarms.clear()
            }
        }
    }

    fun submitForgottenStart(start: String) {
        viewModelScope.launch {
            usecase.manualFastEntryForForgottenStart(start, targetHours.value)
        }
    }

    fun submitForgottenEnd(end: String) {
        viewModelScope.launch {
            usecase.manualFastEntryForForgottenEnd(end)
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