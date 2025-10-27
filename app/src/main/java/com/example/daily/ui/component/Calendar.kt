package com.example.daily.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.Year
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DailyCalendar(
    getMarkColor: (CalendarDay) -> Long?,
    isDaySelected: (CalendarDay) -> Boolean,
    onDayClicked: (CalendarDay) -> Unit
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    Column (
        modifier = Modifier.clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        verticalArrangement = Arrangement.Top
    ) {
        HorizontalCalendar(
            state = state,
            dayContent = {
                Day(
                    it,
                    isSelected = isDaySelected,
                    onClickBehavior = onDayClicked,
                    markColor = getMarkColor
                )
            },
            monthHeader = {
                MonthHeader(it)
            },
        )
    }
}

@Composable
private fun MonthHeader(calendarMonth: CalendarMonth) {
    val daysOfWeek = calendarMonth.weekDays.first().map { it.date.dayOfWeek }
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(top = 6.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp),
            text = calendarMonth.yearMonth.month.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault()),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            for (dayOfWeek in daysOfWeek) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun YearHeader(year: Year) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 20.dp),
            text = year.toString()
        )
        HorizontalDivider()
    }
}

@Composable
fun Day(
    day: CalendarDay,
    markColor: (CalendarDay) -> Long?,
    isSelected: (CalendarDay) -> Boolean,
    onClickBehavior: (CalendarDay) -> Unit
) {
    val color = markColor(day)

    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .aspectRatio(1f) // This is important for square sizing!
            .clickable { if(!isSelected(day)) { onClickBehavior(day) } }
            .padding(1.dp),
        border = if(isSelected(day)) CardDefaults.outlinedCardBorder(true) else null,
        colors = CardDefaults.cardColors(
            containerColor = if(isSelected(day)) {
                    MaterialTheme.colorScheme.surfaceContainer
                } else {
                    if(color != null && day.position == DayPosition.MonthDate)
                        Color(color) else MaterialTheme.colorScheme.surfaceVariant
                }
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                color = if(day.position == DayPosition.MonthDate)
                    MaterialTheme.colorScheme.onSurfaceVariant else Color.Gray,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}