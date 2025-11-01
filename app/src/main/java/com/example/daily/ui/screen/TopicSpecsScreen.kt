package com.example.daily.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
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
import com.example.daily.ui.component.ButtonActive
import com.example.daily.ui.component.ButtonPassive
import com.example.daily.ui.component.ColorBox
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
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            LazyColumn {
                items(topic!!.specs) { spec ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        ColorBox(
                            color = Color(spec.color),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = spec.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            ButtonActive(
                onClick = navigateToSpecCreator,
                title = "Добавить"
            )
        }
        TextButton(
            onClick = navigateToSpecCreator
        ) {
            Text(
                text = "Удалить раздел",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }
    }
}