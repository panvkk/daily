package com.example.daily.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daily.ui.component.ButtonActive
import com.example.daily.ui.component.DailyCalendar
import com.example.daily.ui.component.DayPanel
import com.example.daily.ui.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.clickable {
                                        scope.launch { drawerState.open() }
                                    }
                                ) {
                                    Text(
                                        text = "Создайте метку",
                                        style = MaterialTheme.typography.displaySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxSize()
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

                    if(showNewTopicDialog) {
                        NewTopicDialog(
                            onDismiss = { viewModel.updateShowDialogState(false) },
                            onCreateTopic = { name -> viewModel.createTopic(name) }
                        )
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
}