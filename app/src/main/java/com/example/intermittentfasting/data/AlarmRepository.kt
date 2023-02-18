package com.example.intermittentfasting.data

import javax.inject.Inject

interface AlarmRepository {
    //just store alarm ids for now...
    //just in sharedprefs for now?
    fun saveCurrentAlarmIds(listOfIds: List<Int>)
    fun getCurrentAlarmIds(): List<Int>
}

class AlarmRepositoryImpl @Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : AlarmRepository {
    //TODO when device is turned off and on again
    //need to re assign the alrams
    override fun saveCurrentAlarmIds(listOfIds: List<Int>) {
        sharedPreferencesRepository.setString(ALARM_IDS_KEY, listOfIds.joinToString())
    }

    override fun getCurrentAlarmIds(): List<Int> {
        val stored = sharedPreferencesRepository.getString(ALARM_IDS_KEY)
        return if (!stored.isNullOrBlank()) {
            stored.split(", ").map {
                it.toInt()
            }
        } else {
            emptyList()
        }
    }

    companion object {
        const val ALARM_IDS_KEY = "ALARM_IDS_KEY"
    }
}