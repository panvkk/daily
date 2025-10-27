package com.example.daily.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.daily.ui.component.DailyCalendar
import com.example.daily.ui.component.DayPanel
import com.example.daily.ui.viewmodel.MainViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    val selectedDay = viewModel.selectedDay.collectAsState().value
    val uiState = viewModel.uiState.collectAsState().value
    val showNewTopicDialog = viewModel.showDialogState.collectAsState().value
    val currentTopic = viewModel.currentTopic.collectAsState().value

    Column {
        DailyCalendar(
            isDaySelected = { day -> day == selectedDay },
            onDayClicked = { day -> viewModel.updateSelectedDay(day) },
            getMarkColor = { day -> uiState[day.date.toString()] }
        )
        if(selectedDay != null) {
            DayPanel(
                day = selectedDay,
                addMark = { color ->
                    viewModel.addMark(selectedDay, color)
                },
                deleteMark = {
                    viewModel.deleteMark(selectedDay)
                },
                specs = currentTopic.specs
            )
        }
    }

    if(showNewTopicDialog) {
        NewTopicDialog(
            onDismiss = { viewModel.updateShowDialogState(false) },
            onCreateTopic = { name -> viewModel.createTopic(name) }
        )
    }
}