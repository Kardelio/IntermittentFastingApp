package com.example.intermittentfasting.data

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

interface SharedPreferencesRepository {
    fun getString(key: String): String?
    fun getBoolean(key: String): Boolean
    fun getInt(key: String): Int

    fun setString(key: String, contents: String)
    fun setBoolean(key: String, contents: Boolean)
    fun setInt(key: String, contents: Int)

}

class SharedPreferencesRepositoryImpl @Inject constructor(
    private val sharedPrefs: SharedPreferences
) : SharedPreferencesRepository {
    override fun getString(key: String): String? {
        return sharedPrefs.getString(key, "")
    }

    override fun getBoolean(key: String): Boolean {
        return sharedPrefs.getBoolean(key, false)
    }

    override fun getInt(key: String): Int {
        return sharedPrefs.getInt(key, 16)
    }

    override fun setBoolean(key: String, contents: Boolean) {
        sharedPrefs.edit {
            putBoolean(key, contents)
        }
    }

    override fun setString(key: String, contents: String) {
        sharedPrefs.edit {
            putString(key, contents)
        }
    }

    override fun setInt(key: String, contents: Int) {
        sharedPrefs.edit {
            putInt(key, contents)
        }
    }


}