package com.example.daily.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.toString
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.daily.model.TopicSpec
import com.example.daily.ui.component.InputField
import com.example.daily.ui.viewmodel.SpecCreatorVM
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun SpecCreatorScreen(
    viewModel: SpecCreatorVM,
    onCrateSpec: (TopicSpec) -> Unit,
    navigateTopicSpecs: () -> Unit
) {
    val input = viewModel.inputDescription.collectAsState().value
    val pickedColor = viewModel.pickedColor.collectAsState().value
    val showColorPicker = viewModel.showColorPicker.collectAsState().value

    if(showColorPicker) {
        ColorPicker(viewModel)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(pickedColor != null) {
            Row {
                Box(
                    modifier = Modifier
                        .background(Color(pickedColor))
                        .padding(end = 12.dp)
                        .size(32.dp)
                        .clickable {
                            viewModel.updatePickerVisibility(true)
                        }
                )
                Text(
                    text = pickedColor.toString(16),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            TextButton(
                onClick = { viewModel.updatePickerVisibility(true) }
            ) {
                Text(
                    "Выберите цвет",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }
        InputField(
            label = "Описание",
            value = input,
            onValueChange = { viewModel.updateInput(it) }
        )
        TextButton(
            modifier = Modifier.padding(vertical = 16.dp),
            onClick = {
                if(pickedColor != null && input.isNotBlank()) {
                    onCrateSpec(
                        TopicSpec(
                            color = pickedColor,
                            description = input
                        )
                    )
                    navigateTopicSpecs()
                }
            }
        ) {
            Text(
                text = "Подтвердить",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ColorPicker(
    viewModel: SpecCreatorVM
) {
    val controller = rememberColorPickerController()
    val color = viewModel.pickedColor.collectAsState().value

    Dialog(
        onDismissRequest = { viewModel.updatePickerVisibility(false) }
    ) {
        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(10.dp),
                controller = controller,
                onColorChanged = { colorEnvelope: ColorEnvelope ->
                    viewModel.updateColor(colorEnvelope.hexCode.toLong(16))
                }
            )
            TextButton(
                onClick =  {
                    if(color != null) {
                        viewModel.updatePickerVisibility(false)
                    }
                }
            ) {
                Text(
                    text = "Подтвердить",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}