package com.example.intermittentfasting.pastfasts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermittentfasting.common.SnackBarFlow
import com.example.intermittentfasting.domain.FastUseCase
import com.example.intermittentfasting.domain.FileUseCase
import com.example.intermittentfasting.model.EmptyFast
import com.example.intermittentfasting.model.Fast
import com.example.intermittentfasting.model.FastContainer
import com.example.intermittentfasting.model.PastFast
import com.example.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PastFastsViewModel @Inject constructor(
    private val usecase: FastUseCase,
    private val fileUseCase: FileUseCase,
    private val locale: Locale,
    private val snackBarFlow: SnackBarFlow
) : ViewModel() {

    private val _pastfasts: MutableStateFlow<List<FastContainer>> = MutableStateFlow(emptyList())
    val pastfasts: StateFlow<List<FastContainer>> = _pastfasts.asStateFlow()

    init {
        Log.d("BK", "VM init")
        viewModelScope.launch {
            usecase.getAllPastFasts().collectLatest { list ->
                Log.d("BK", "List: ${list}")
                mapPastFasts(list)
            }
//            usecase.getCurrentOrLast().collectLatest {
//                if (it != null) {
//                    Log.d("BK", "UPDTAED fast: ${it}")
//                    _currentFast.value = PrettyFast.toPrettyFast(locale, it)
//                }
//            }
        }
    }

    private fun mapPastFasts(list: List<Fast>) {
        val out = mutableListOf<FastContainer>()
        list.forEachIndexed { index, fast ->
            if (index == 0) {
                out.add(PastFast.toPastFast(locale, fast))
            } else {
                val previous = index - 1
                val ans =
                    TimeUtils.getDayDifference(locale, fast.endTimeUTC, list[previous].startTimeUTC)
                Log.d("BK", "===> ${ans}")
                if (ans < 1) {
                    out.add(PastFast.toPastFast(locale, fast))
                } else {
                    Log.d("BK", "and $ans")
                    out.add(EmptyFast(ans.toInt()))
                    out.add(PastFast.toPastFast(locale, fast))
                }
            }
        }
        _pastfasts.value = out
    }

    fun exportFileToDownloads() {
        fileUseCase.writeFileWithFasts()
        //TODO
        //println(" ${snackBarFlow.hashCode()}")
        snackBarFlow.notifyChange("Saved file to Downloads")
    }

    fun importFileFromDownloads() {
        viewModelScope.launch {

            val oldFasts = fileUseCase.readInFileWithFasts()
            usecase.manualImportOfFasts(oldFasts)

        }

    }

    fun deleteFast(pastFastId: Int) {
        Log.d("BK", "Past fast: ${pastFastId}")
        viewModelScope.launch {
            usecase.deleteSpecificFast(pastFastId)
        }
    }
}