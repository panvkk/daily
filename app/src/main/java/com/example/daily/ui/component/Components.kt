package com.example.daily.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.lifecycle.viewModelScope
import com.example.daily.R
import com.example.daily.model.Topic
import com.example.daily.model.TopicSpec
import com.example.daily.ui.viewmodel.MainViewModel
import com.kizitonwose.calendar.core.CalendarDay
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                onDeleteTopic = { topic -> viewModel.deleteTopic(topic) },
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
    onDeleteTopic: (Topic) -> Unit,
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

        AnimatedVisibility( // почему-то не работает(понятно, надо не сразу присваивать expanded, а с задержкой)
            visible = expanded,
            enter = slideInVertically(initialOffsetY = { -it },
                animationSpec = tween(durationMillis = 150)) +
                    fadeIn(animationSpec = tween(durationMillis = 150)),
            exit = slideOutVertically(targetOffsetY = { -it },
                animationSpec = tween(durationMillis = 150)) +
                    fadeOut(animationSpec = tween(durationMillis = 150))
        ) {
            Popup(
                onDismissRequest = { expanded = false }
            ) {

                Column(
                    modifier = Modifier
                        .background(Color.Gray)
                        .width(200.dp)
                ) {
                    LazyColumn {
                        items(items = topicList, key = { it.name }) { topic ->
                            Column {
                                DropdownItem(
                                    topic = topic,
                                    onClick = {
                                        expanded = false
                                        onTopicChoice(topic)
                                    },
                                    onDeleteTopic = { onDeleteTopic(topic) }
                                )
                                HorizontalDivider(thickness = 2.dp)
                            }
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

@Composable
fun DropdownItem(
    topic: Topic,
    onClick: () -> Unit,
    onDeleteTopic: () -> Unit
) {
    var showDeleteIcon by remember { mutableStateOf(false) }

    Row {
        Box(
            modifier = Modifier.padding(4.dp)
                .fillMaxWidth()
                .weight(1f)
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = { showDeleteIcon = !showDeleteIcon }
                )
        ) {
            Text(text = topic.name)
        }
        if(showDeleteIcon) {
            IconButton(onClick = onDeleteTopic) {
                Icon(
                    painter = painterResource(R.drawable.delete_icon),
                    contentDescription = "delete"
                )
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