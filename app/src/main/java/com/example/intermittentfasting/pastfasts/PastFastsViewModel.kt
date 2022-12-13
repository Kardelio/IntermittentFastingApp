package com.example.intermittentfasting.pastfasts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermittentfasting.domain.FastUseCase
import com.example.intermittentfasting.domain.FileUseCase
import com.example.intermittentfasting.model.PastFast
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
    private val locale: Locale
): ViewModel(){

   private val _pastfasts: MutableStateFlow<List<PastFast>> = MutableStateFlow(emptyList())
    val pastfasts: StateFlow<List<PastFast>> = _pastfasts.asStateFlow()

    init {
        Log.d("BK", "VM init")
        viewModelScope.launch {
            usecase.getAllPastFasts().collectLatest { list ->
                Log.d("BK","List: ${list}")
                _pastfasts.value = list.map { PastFast.toPastFast(locale,it) }
            }
//            usecase.getCurrentOrLast().collectLatest {
//                if (it != null) {
//                    Log.d("BK", "UPDTAED fast: ${it}")
//                    _currentFast.value = PrettyFast.toPrettyFast(locale, it)
//                }
//            }
        }
    }

   fun exportFileToDownloads(){
        fileUseCase.writeFileWithFasts()
   }

   fun importFileFromDownloads(){
        viewModelScope.launch {

            val oldFasts = fileUseCase.readInFileWithFasts()
            usecase.manualImportOfFasts(oldFasts)

        }

   }

    fun deleteFast(pastFastId: Int){
        Log.d("BK","Past fast: ${pastFastId}")
        viewModelScope.launch {
            usecase.deleteSpecificFast(pastFastId)
        }
    }
}