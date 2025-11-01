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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.daily.R
import com.example.daily.model.Topic
import com.example.daily.ui.viewmodel.MainViewModel

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
                .size(32.dp),
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
        modifier = Modifier
            .padding(end = 16.dp, start = 4.dp, top = 4.dp, bottom = 4.dp)
            .clickable { if (!expanded) expanded = true }
            .size(48.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(R.drawable.topic_icon),
                contentDescription = "topics",
                modifier = Modifier.size(32.dp)
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