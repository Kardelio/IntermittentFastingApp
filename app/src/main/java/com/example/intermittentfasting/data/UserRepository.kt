package com.example.intermittentfasting.data

import javax.inject.Inject

interface UserRepository {
    fun getLastSetTargetHours(): Int
    fun setLastTargetHours(hours: Int)
}

class UserRepositoryImpl @Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : UserRepository {
    override fun getLastSetTargetHours(): Int {
        return sharedPreferencesRepository.getInt(TARGET_HOURS_KEY)
    }

    override fun setLastTargetHours(hours: Int) {
        sharedPreferencesRepository.setInt(TARGET_HOURS_KEY, hours)
    }

    companion object {
        const val TARGET_HOURS_KEY = "TARGET_HOURS"
    }
}