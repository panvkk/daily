package com.example.daily.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daily.ui.viewmodel.MainViewModel
import com.kizitonwose.calendar.core.CalendarDay
import java.util.Formatter

@Composable
fun DayPanel(
    day: CalendarDay,
    addMark: (Long) -> Unit,
    deleteMark: () -> Unit
) {
    Column {
        Text(
            text = day.date.toString(),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = "Покрасить в красный",
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clickable {
                    addMark(0xFFFF0000)
                }
        )
        Text(
            text = "Покрасить в зелёный",
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clickable {
                    addMark(0xFF008000)
                }
        )
        Text(
            text = "УДАЛИТЬ",
            modifier = Modifier.clickable {
                deleteMark()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopDailyBar(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val topic = viewModel.currentTopic.collectAsState().value
    TopAppBar(
        title = {
            Text(
                text = topic,
                fontSize = 32.sp
            )
        },
        modifier = modifier
    )
}