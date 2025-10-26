package com.example.daily.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TopicDialogVM @Inject constructor() : ViewModel() {
    private val _inputTopicName = MutableStateFlow("")
    val inputTopicName: StateFlow<String> = _inputTopicName

    fun updateInputTopicName(value: String) {
        _inputTopicName.update { value }
    }
}