package com.example.daily.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.daily.R
import com.example.daily.ui.component.ButtonActive
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
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Новый раздел",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            InputField(
                label = stringResource(R.string.input_topic_name),
                value = input,
                onValueChange = { dialogViewModel.updateInputTopicName(it) },
            )
            ButtonActive(
                onClick = {
                    onCreateTopic(input)
                    onDismiss()
                },
                title = "Создать",
            )
        }
    }
}
