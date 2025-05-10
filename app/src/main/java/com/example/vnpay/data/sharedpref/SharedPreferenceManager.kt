package com.example.vnpay.data.sharedpref

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.getString
import com.example.vnpay.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

val DARK_MODE_PREF = "dark_mode"
val DARK_MODE_KEY = "dark_mode_key"

class SharedPreferenceManager @Inject constructor(@ApplicationContext val context: Context) {

    private fun getDarkModeSharePref(): SharedPreferences = context.getSharedPreferences(DARK_MODE_PREF, Context.MODE_PRIVATE)

    fun setDarkMode(isDarkMode: Boolean) {
        with (getDarkModeSharePref().edit()) {
            putBoolean(DARK_MODE_KEY, isDarkMode)
            apply()
        }
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun getDarkMode(): Boolean = getDarkModeSharePref().getBoolean(DARK_MODE_KEY, false)
}