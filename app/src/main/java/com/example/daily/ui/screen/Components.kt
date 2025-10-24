package com.example.daily.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kizitonwose.calendar.core.CalendarDay

@Composable
fun DayPanel(
    addMark: (Int, String) -> Unit
) {
    Column {
        Text(
            text = "Покрасить в красный",
            modifier = Modifier.clickable {
                addMark(0x0000, "краснинки")
            }
        )
        Text(
            text = "Покрасить в зелёный",
            modifier = Modifier.clickable {
                addMark(0x8000, "зиёниньки")
            }
        )
    }
}