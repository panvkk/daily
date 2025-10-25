package com.example.daily.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daily.data.repository.DailyRepository
import com.example.daily.model.Mark
import com.kizitonwose.calendar.core.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TOPIC = "Походы в зал"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dailyRepository: DailyRepository
) : ViewModel() {
    private val _currentTopic = MutableStateFlow<String>(TOPIC)
    val currentTopic: StateFlow<String> = _currentTopic
    val uiState: StateFlow<Map<String, Long>> = _currentTopic
        .flatMapLatest { topic ->
            dailyRepository.getAllMarksByTopic(topic)
        }
        .map { markList ->
            markList.associate {
                it.date to it.color
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    private val _selectedDay = MutableStateFlow<CalendarDay?>(null)
    val selectedDay: StateFlow<CalendarDay?> = _selectedDay

    fun updateSelectedDay(day: CalendarDay) {
        _selectedDay.update { day }
    }

    fun addMark(day: CalendarDay, color: Long) {
        viewModelScope.launch {
            dailyRepository.putMark(
                Mark(
                    topic = _currentTopic.value,
                    color = color,
                    date = day.date.toString()
                )
            )
        }
    }

    fun deleteMark(day: CalendarDay) {
        viewModelScope.launch {
            val color = uiState.value[day.date.toString()]
            if(color != null) {
                dailyRepository.deleteMark(
                    Mark(
                        date = day.date.toString(),
                        color = color,
                        topic = _currentTopic.value
                    )
                )
            }
        }
    }
}