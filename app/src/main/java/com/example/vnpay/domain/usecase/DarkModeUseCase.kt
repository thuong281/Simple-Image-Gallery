package com.example.vnpay.domain.usecase

import com.example.vnpay.data.sharedpref.SharedPreferenceManager
import com.example.vnpay.domain.repository.ImageRepository
import javax.inject.Inject

class DarkModeUseCase @Inject constructor(
    private val sharedPreferenceManager: SharedPreferenceManager
) {
    fun getDarkMode(): Boolean = sharedPreferenceManager.getDarkMode()
    fun setDarkMode(isDarkMode: Boolean) = sharedPreferenceManager.setDarkMode(isDarkMode)
}