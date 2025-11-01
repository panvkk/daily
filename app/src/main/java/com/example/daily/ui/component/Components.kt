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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.daily.R
import com.example.daily.model.Topic
import com.example.daily.model.TopicSpec
import com.example.daily.ui.viewmodel.MainViewModel
import com.kizitonwose.calendar.core.CalendarDay
import java.time.format.TextStyle
import java.util.Locale

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
            LazyColumn {
                items(specs) { spec ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { addMark(spec.color) }
                    ) {
                        ColorBox(
                            color = Color(spec.color),
                            modifier = Modifier.padding(end = 8.dp),
                            size = 52.dp
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
            TextButton(
                onClick = { deleteMark() }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopDailyBar(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit,
) {
    val topic = viewModel.currentTopic.collectAsState().value
    val topicList = viewModel.topics.collectAsState().value

    if(topic != null) {
        TopAppBar(
            title = {
                Text(
                    text = topic.name,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            },
            navigationIcon = {
                TopicsIcon(
                    onTopicChoice = { topic -> viewModel.updateSelectedTopic(topic) },
                    onNewTopicChoice = { viewModel.updateShowDialogState(true) },
                    onDeleteTopic = { topic -> viewModel.deleteTopic(topic) },
                    topicList = topicList
                )
            },
            actions = {
                TopicSpecIcon(navigateSpecs = openDrawer)
            },
            modifier = modifier
        )
    } else {
        TopAppBar(title = {
            Text(
                text = "Daily",
                textAlign = TextAlign.Center
            )
        })
    }
}

@Composable
fun TopicSpecIcon(
    navigateSpecs: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(4.dp)
            .size(48.dp)
            .clickable { navigateSpecs() }
    ) {
        Box(
            modifier = Modifier
                .size(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.specs_settings_icon),
                contentDescription = "Topic specs settings",
                modifier = Modifier.fillMaxSize()
            )
        }
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
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(end = 16.dp, start = 4.dp, top = 4.dp, bottom = 4.dp)
            .size(28.dp)
            .clickable { if (!expanded) expanded = true }
    ) {

        Icon(
            painter = painterResource(R.drawable.topic_icon),
            contentDescription = "topics",
            modifier = Modifier.fillMaxSize()
        )


        Popup(
            onDismissRequest = { expanded = false }
        ) {
            AnimatedVisibility( // почему-то не работает
                visible = expanded,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(durationMillis = 150)
                ) + fadeIn(animationSpec = tween(durationMillis = 150)),
                exit = slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(durationMillis = 150)
                ) + fadeOut(animationSpec = tween(durationMillis = 150))
            ) {
                Card(
                    shape = RoundedCornerShape(
                        topStart = 4.dp,
                        topEnd = 16.dp,
                        bottomEnd = 16.dp,
                        bottomStart = 16.dp
                    ),
                    border = CardDefaults.outlinedCardBorder(),

                ) {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .width(150.dp)
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
                                    HorizontalDivider(thickness = 1.dp)
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
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .height(40.dp)
                .weight(1f)
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = { showDeleteIcon = !showDeleteIcon }
                )
        ) {
            Text(
                text = topic.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 8.dp)
            )
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
        textStyle = MaterialTheme.typography.bodyLarge,
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,

            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
//            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,

            //bottom line
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun ButtonPassive(
    onClick: () -> Unit,
    title: String,
    modifier: Modifier = Modifier
) {
    Button (
        modifier = modifier
            .padding(16.dp)
            .clip(MaterialTheme.shapes.small)
            .width(160.dp)
            .height(36.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ButtonActive(
    onClick: () -> Unit,
    title: String,
    modifier: Modifier = Modifier
) {
    Button (
        modifier = Modifier
            .padding(16.dp)
            .clip(MaterialTheme.shapes.small)
            .width(160.dp)
            .height(36.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ColorBox(
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    Card (
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        shape = MaterialTheme.shapes.small,
        border = CardDefaults.outlinedCardBorder(),
        modifier = modifier
            .height(size)
            .width(size)
    ) {    }
}