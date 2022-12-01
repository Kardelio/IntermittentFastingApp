package com.example.intermittentfasting.manualentry

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermittentfasting.domain.FastUseCase
import com.example.intermittentfasting.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ManualEntryViewModel @Inject constructor(
    private val locale: Locale,
    private val useCase: FastUseCase
) : ViewModel() {

    /*
    ability to enter past fasts into DB
     */

    fun getCleanedUpDateString(
        startDate: Int,
        startMonth: Int,
        startYear: Int,
        startHour: Int,
        startMinute: Int
    ): String {
        return TimeUtils.convertStringsToUTCString(
            locale,
            startDate,
            startMonth,
            startYear,
            startHour,
            startMinute
        )
    }

    fun submitFast(
        startDateString: String,
        endDateString: String
    ) {
        val isIt = TimeUtils.isFirstDateBeforeOther(locale, startDateString, endDateString)
        Log.d("BK", "is start before end: ${isIt}")
        if (isIt) {
            // all good can create a fast
            viewModelScope.launch {
                useCase.manualFastEntry(startDateString, endDateString)
                //clear fields that should be moved to the VM
            }
        } else {
            //error end date can not be before the start date
        }
    }

}