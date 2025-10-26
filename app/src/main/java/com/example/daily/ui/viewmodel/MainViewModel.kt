package com.example.daily.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daily.data.repository.DailyRepository
import com.example.daily.model.Mark
import com.example.daily.model.Topic
import com.example.daily.model.TopicSpec
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

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dailyRepository: DailyRepository
) : ViewModel() {

    val topics = dailyRepository.getAllTopics()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    private val _currentTopic = MutableStateFlow<Topic>(Topic("Добавьте бебеб", listOf(TopicSpec(1, "desc"))))
    val currentTopic: StateFlow<Topic> = _currentTopic

    val uiState: StateFlow<Map<String, Long>> = _currentTopic
        .flatMapLatest { topic ->
            dailyRepository.getAllMarksByTopic(topic.name)
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

    private val _showDialogState = MutableStateFlow(false)
    val showDialogState: StateFlow<Boolean> = _showDialogState

    fun updateShowDialogState(value: Boolean) {
        _showDialogState.update { value }
    }

    fun updateSelectedDay(day: CalendarDay) {
        _selectedDay.update { day }
    }

    fun updateSelectedTopic(newTopic: Topic) {
        _currentTopic.update { newTopic }
    }

    fun addMark(day: CalendarDay, color: Long) {
        viewModelScope.launch {
            dailyRepository.putMark(
                Mark(
                    topic = _currentTopic.value.name,
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
                        topic = _currentTopic.value.name
                    )
                )
            }
        }
    }

    fun createTopic(topicName: String) {
        viewModelScope.launch {
            dailyRepository.putTopic(
                Topic(
                    name = topicName,
                    specs = listOf(TopicSpec(1, "desc"))
                )
            )
        }
    }
}