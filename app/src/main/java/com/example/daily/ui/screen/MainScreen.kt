package com.example.daily.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.daily.ui.viewmodel.MainViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val selectedDay = viewModel.selectedDay.collectAsState()
    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DailyCalendar(
            isDaySelected = { day -> day == selectedDay.value },
            onDayClicked = { day -> viewModel.updateSelectedDay(day)},
            getMarkColor = { day -> uiState.value[day.date.toString()]?.color }
        )
        val currentDay = selectedDay.value
        if(currentDay != null) {
            DayPanel(
                addMark = { color, description ->
                    viewModel.addMark(currentDay, color, description)
                }
            )
        }
    }
}