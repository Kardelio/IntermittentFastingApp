package com.example.intermittentfasting.domain

sealed class FastCurrentActiveState{
    object NowActive: FastCurrentActiveState()
    object NowInActive: FastCurrentActiveState()
}