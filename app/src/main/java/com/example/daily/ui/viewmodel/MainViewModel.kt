package com.example.daily.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daily.data.repository.DailyRepository
import com.example.daily.model.Date
import com.example.daily.model.Mark
import com.kizitonwose.calendar.core.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TOPIC = "Походы в зал"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dailyRepository: DailyRepository
) : ViewModel() {
    private val _currentTopic = MutableStateFlow<String>(TOPIC)
    val uiState: StateFlow<Map<String, Mark>> = _currentTopic
        .flatMapLatest { topic ->
            dailyRepository.getAllMarksByTopic(topic)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    private val _selectedDay = MutableStateFlow<CalendarDay?>(null)
    val selectedDay: StateFlow<CalendarDay?> = _selectedDay

    fun updateSelectedDay(day: CalendarDay) {
        _selectedDay.value = day
    }

    fun addMark(day: CalendarDay, color: Int, description: String) {
        viewModelScope.launch {
            dailyRepository.putMark(
                Date(day.date.toString()),
                Mark(
                    topic = _currentTopic.value,
                    color = color,
                    description = description
                )
            )
        }
    }
}