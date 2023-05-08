package com.example.intermittentfasting.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SnackBarFlow {
    fun collectChanges(): SharedFlow<String>
    fun notifyChange(message: String)
}

class SnackBarFlowImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher
) : SnackBarFlow {

    init {
        println("Init of snackbarflow")
    }

    private val _updateFlow: MutableSharedFlow<String> = MutableSharedFlow(0)
    private val updateFlow: SharedFlow<String> = _updateFlow

    override fun collectChanges(): SharedFlow<String> = updateFlow

    override fun notifyChange(message: String) {
        CoroutineScope(dispatcher).launch {
            _updateFlow.emit(message)
        }
    }

}