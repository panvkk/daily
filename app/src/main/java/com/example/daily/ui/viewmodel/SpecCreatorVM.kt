package com.example.daily.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SpecCreatorVM @Inject constructor() : ViewModel() {
    private val _pickedColor = MutableStateFlow<Long?>(null)
    val pickedColor: StateFlow<Long?> = _pickedColor

    fun updateColor(newColor: Long) {
        _pickedColor.update { newColor }
    }

    private val _inputDescription = MutableStateFlow<String>("")
    val inputDescription: StateFlow<String> = _inputDescription

    fun updateInput(value: String) {
        _inputDescription.update { value }
    }

    private val _showColorPicker = MutableStateFlow<Boolean>(false)
    val showColorPicker: StateFlow<Boolean> = _showColorPicker

    fun updatePickerVisibility(value: Boolean) {
        _showColorPicker.update { value }
    }

}