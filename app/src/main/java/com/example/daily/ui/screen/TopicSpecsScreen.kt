package com.example.daily.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daily.ui.viewmodel.MainViewModel

@Composable
fun TopicSpecsScreen(
    viewModel: MainViewModel,
    navigateToSpecCreator: () -> Unit,
    navigateHome: () -> Unit
) {
    val topic = viewModel.currentTopic.collectAsState().value

    Column {
        LazyColumn {
            items(topic.specs) { spec ->
                Row {
                    Box(
                        modifier = Modifier
                            .background(Color(spec.color))
                            .padding(end = 12.dp)
                            .size(32.dp)
                    )
                    Text(text = spec.description)
                }
            }
        }
        TextButton(
            modifier = Modifier.padding(16.dp).background(Color.Red),
            onClick = {
                viewModel.deleteTopic(topic)
                navigateHome()
            }
        ) {
            Text(text = "Удалить тему")
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(
                onClick = navigateHome
            ) {
                Text(text = "Назад")
            }
            TextButton(
                onClick = navigateToSpecCreator
            ) {
                Text(text = "Добавить")
            }
        }
    }
}