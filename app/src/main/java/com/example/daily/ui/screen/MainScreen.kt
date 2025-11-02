package com.example.daily.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.daily.model.TopicSpec
import com.example.daily.ui.component.ButtonActive
import com.example.daily.ui.component.ColorBox
import com.example.daily.ui.component.DailyCalendar
import com.example.daily.ui.viewmodel.MainViewModel
import com.kizitonwose.calendar.core.CalendarDay
import kotlinx.coroutines.launch
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    navigateSpecCreator: () -> Unit,
    viewModel: MainViewModel
) {
    val selectedDay = viewModel.selectedDay.collectAsState().value
    val uiState = viewModel.uiState.collectAsState().value
    val showNewTopicDialog = viewModel.showDialogState.collectAsState().value
    val currentTopic = viewModel.currentTopic.collectAsState().value

    val scope = rememberCoroutineScope()

    if(currentTopic != null) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        DismissibleDrawerSheet {
                            TopicSpecsScreen(
                                viewModel = viewModel,
                                navigateToSpecCreator = navigateSpecCreator,
                                navigateBack = {
                                    scope.launch { drawerState.close() }
                                },
                                modifier = Modifier.fillMaxWidth(0.85f)
                            )
                        }
                    }
                }
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Column(modifier = modifier) {
                        DailyCalendar(
                            isDaySelected = { day -> day == selectedDay },
                            onDayClicked = { day -> viewModel.updateSelectedDay(day) },
                            getMarkColor = { day -> uiState[day.date.toString()] }
                        )
                        if (selectedDay != null) {
                            if (currentTopic.specs.isEmpty()) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Пусто!",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Text(
                                        text = "Создайте свою метку",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    ButtonActive(
                                        onClick = { scope.launch { drawerState.open() } },
                                        title = "Создать"
                                    )
                                }
                            } else {
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
                    }


                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Пусто!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Создайте свой раздел",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            ButtonActive(
                onClick = { viewModel.updateShowDialogState(true) },
                title = "Создать"
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

@Composable
fun DayPanel(
    specs: List<TopicSpec>,
    day: CalendarDay,
    addMark: (Long) -> Unit,
    deleteMark: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "${day.date.dayOfMonth} ${day.date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${day.date.year}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(16.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                 modifier = Modifier.weight(1f)
            ) {
                items(specs) { spec ->
                    Card(
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = CardDefaults.outlinedCardBorder(),
                        modifier = Modifier
                            .height(70.dp)
                            .width(320.dp)
                            .clickable { addMark(spec.color) }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .fillMaxHeight(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            ColorBox(
                                color = Color(spec.color),
                                modifier = Modifier.padding(end = 8.dp),
                                size = 48.dp
                            )
                            Text(
                                text = spec.description,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            TextButton(
                onClick = { deleteMark() },
                modifier = Modifier
            ) {
                Text(
                    text = "Удалить метку",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}