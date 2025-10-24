package com.example.daily.ui.viewmodel

import com.example.daily.data.repository.DailyRepository
import com.example.daily.model.Mark
import com.kizitonwose.calendar.core.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dailyRepository: DailyRepository
) {
    private val _uiState = MutableStateFlow<List<Mark>>(emptyList())
    val uiState: StateFlow<List<Mark>> = _uiState

    private val _selectedDay = MutableStateFlow<CalendarDay?>(null)
    val selectedDay: StateFlow<CalendarDay?> = _selectedDay



}