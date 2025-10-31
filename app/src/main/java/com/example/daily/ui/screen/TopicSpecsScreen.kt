package com.example.daily.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.daily.ui.component.ButtonPassive
import com.example.daily.ui.viewmodel.MainViewModel

@Composable
fun TopicSpecsScreen(
    viewModel: MainViewModel,
    navigateToSpecCreator: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val topic = viewModel.currentTopic.collectAsState().value

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            items(topic!!.specs) { spec ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(spec.color))
                            .padding(end = 12.dp)
                            .size(32.dp)
                    )
                    Text(
                        text = spec.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        ButtonPassive(
            onClick = {
                if(topic != null) viewModel.deleteTopic(topic)
                navigateBack()
            },
            title = "Удалить раздел"
        )
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(
                onClick = navigateBack
            ) {
                Text(
                    text = "Назад",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
            TextButton(
                onClick = navigateToSpecCreator
            ) {
                Text(
                    text = "Добавить",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}