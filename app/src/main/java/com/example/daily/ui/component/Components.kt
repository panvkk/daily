package com.example.daily.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.example.daily.R
import com.example.daily.model.Topic
import com.example.daily.model.TopicSpec
import com.example.daily.ui.viewmodel.MainViewModel
import com.kizitonwose.calendar.core.CalendarDay

@Composable
fun DayPanel(
    specs: List<TopicSpec>,
    day: CalendarDay,
    addMark: (Long) -> Unit,
    deleteMark: () -> Unit
) {
    Column {
        Text(
            text = day.date.toString(),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        LazyColumn {
            items(specs) { spec ->
                Row(
                    modifier = Modifier
                        .clickable {
                            addMark(spec.color)
                        }
                ){
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
    modifier: Modifier = Modifier,
    navigateSpecs: () -> Unit
) {
    val topic = viewModel.currentTopic.collectAsState().value
    val topicList = viewModel.topics.collectAsState().value

    TopAppBar(
        title = {
            Text(
                text = topic.name,
                fontSize = 32.sp
            )
        },
        navigationIcon = {
            TopicsIcon(
                onTopicChoice = { topic -> viewModel.updateSelectedTopic(topic) },
                onNewTopicChoice = { viewModel.updateShowDialogState(true) } ,
                topicList = topicList
            ) },
        actions = {
            TopicSpecIcon(navigateSpecs = navigateSpecs)
        },
        modifier = modifier
    )
}

@Composable
fun TopicSpecIcon(
    navigateSpecs: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clickable { navigateSpecs() }
    ) {
        Icon(
            painter = painterResource(R.drawable.specs_settings_icon),
            contentDescription = "Topic specs settings"
        )
    }
}

@Composable
fun TopicsIcon(
    topicList: List<Topic>,
    onTopicChoice: (Topic) -> Unit,
    onNewTopicChoice: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(4.dp)
            .clickable { if(!expanded) expanded = true }
    ) {
        Icon(
            painter = painterResource(R.drawable.topic_icon),
            contentDescription = "topics"
        )

        if(expanded) {
            Popup(
                onDismissRequest = { expanded = false }
            ) {
                AnimatedVisibility( // почему-то не работает
                    visible = expanded,
                    enter = slideInVertically(initialOffsetY = { -it },
                        animationSpec = tween(durationMillis = 150)) +
                            fadeIn(animationSpec = tween(durationMillis = 300)),
                    exit = slideOutVertically(targetOffsetY = { -it },
                        animationSpec = tween(durationMillis = 150)) +
                            fadeOut(animationSpec = tween(durationMillis = 300))
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color.Gray)
                            .width(150.dp)
                    ) {
                        LazyColumn {
                            items(topicList) { topic ->
                                DropdownMenuItem(
                                    text = { Text(text = topic.name) },
                                    onClick = {
                                        expanded = false
                                        onTopicChoice(topic)
                                    }
                                )
                            }
                        }
                        DropdownMenuItem(
                            text = { Text(text = "+") },
                            onClick = {
                                expanded = false
                                onNewTopicChoice()
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    label: String,
    onValueChange: (String) -> Unit,
    value: String,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label) },
        visualTransformation = visualTransformation,
        textStyle = MaterialTheme.typography.labelSmall,
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(
//            focusedPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer,
//            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,

            //turning off bottom line
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}