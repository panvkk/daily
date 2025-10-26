package com.example.daily.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.daily.R
import com.example.daily.ui.component.InputField
import com.example.daily.ui.viewmodel.TopicDialogVM


@Composable
fun NewTopicDialog(
    onCreateTopic: (String) -> Unit,
    onDismiss: () -> Unit,
    dialogViewModel: TopicDialogVM = hiltViewModel(),
) {
    val input = dialogViewModel.inputTopicName.collectAsState().value
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            InputField(
                label = stringResource(R.string.input_topic_name),
                value = input,
                onValueChange = { dialogViewModel.updateInputTopicName(it) },
                modifier = Modifier.padding(16.dp)
            )
            TextButton(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    onCreateTopic(input)
                    onDismiss()
                }
            ) {
                Text(text = "Создать")
            }
        }
    }
}
