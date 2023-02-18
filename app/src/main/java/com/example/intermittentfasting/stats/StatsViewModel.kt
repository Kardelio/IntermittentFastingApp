package com.example.intermittentfasting.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intermittentfasting.domain.StatBlock
import com.example.intermittentfasting.domain.StatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StatsViewModel @Inject constructor(
    private val usecase: StatsUseCase
) : ViewModel() {

    private val _stats: MutableStateFlow<StatBlock?> = MutableStateFlow(null)
    val stats: StateFlow<StatBlock?> = _stats.asStateFlow()

    init {
        viewModelScope.launch {
            usecase.getStatsNumbers().collectLatest {
                _stats.value = it
            }
        }
    }

}