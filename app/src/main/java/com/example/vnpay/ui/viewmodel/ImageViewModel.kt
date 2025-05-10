package com.example.vnpay.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vnpay.data.model.ImageFolder
import com.example.vnpay.domain.usecase.DarkModeUseCase
import com.example.vnpay.domain.usecase.GetImageFolderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val getImageFolderUseCase: GetImageFolderUseCase,
    private val darkModeUseCase: DarkModeUseCase
): ViewModel() {

    private val _imageFolder: MutableStateFlow<List<ImageFolder>> = MutableStateFlow(listOf())
    val imageFolder: StateFlow<List<ImageFolder>> = _imageFolder.asStateFlow()

    private val _selectedFolderPos: MutableStateFlow<Int> = MutableStateFlow(-1)
    val selectedFolder: MutableStateFlow<ImageFolder?> = MutableStateFlow(null)

    private val _currentSelectedImagePos: MutableStateFlow<Int> = MutableStateFlow(-1)
    val currentSelectedImagePos = _currentSelectedImagePos.asStateFlow()

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    init {
        viewModelScope.launch {
            _imageFolder.combine(_selectedFolderPos) { imageFolder, pos ->
                if (pos == -1) return@combine
                selectedFolder.emit(imageFolder[pos])
            }.collect()
        }
    }

    fun getImageFolder() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_imageFolder.value.isEmpty()) {
                _loading.emit(true)
            }
           _imageFolder.emit(getImageFolderUseCase())
            _loading.emit(false)
        }
    }

    fun setSelectedFolderPos(pos: Int) {
        viewModelScope.launch {
            _selectedFolderPos.emit(pos)
        }
    }

    fun updateCurrentSelectImage(pos: Int) {
        viewModelScope.launch {
            _currentSelectedImagePos.emit(pos)
        }
    }

    fun setDarkMode(darkMode: Boolean) {
        darkModeUseCase.setDarkMode(darkMode)
    }

    fun getDarkMode() = darkModeUseCase.getDarkMode()
}