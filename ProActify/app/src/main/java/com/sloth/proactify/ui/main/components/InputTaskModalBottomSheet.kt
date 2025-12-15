package com.sloth.proactify.ui.main.components

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.sloth.proactify.domain.model.TaskModel
import com.sloth.proactify.ui.main.MainEvent
import com.sloth.proactify.ui.main.MainState
import com.sloth.proactify.ui.theme.Beige50
import com.sloth.proactify.ui.theme.Blue10
import com.sloth.proactify.ui.theme.Gradient20
import com.sloth.proactify.ui.theme.Orange50
import com.sloth.proactify.ui.theme.Peach20
import com.sloth.proactify.ui.theme.Sage50
import com.sloth.proactify.utils.TestTags
import java.util.Calendar

@Composable
fun BottomSheetContent(
    stateSave: MainState,
    stateIsEdit: MainState,
    onEvent: (MainEvent) -> Unit,
    onClose: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val colorList: List<Color> = listOf(Orange50, Gradient20, Beige50, Blue10, Peach20)

    var isReadOnly by remember {
        mutableStateOf(false)
    }

    var id by remember {
        mutableStateOf(0L)
    }

    var title by remember {
        mutableStateOf("")
    }

    var date by remember {
        mutableStateOf("")
    }

    var color by remember {
        mutableStateOf(colorList.random())
    }

    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf("") }
    val calendar = Calendar.getInstance()

    var description by remember {
        mutableStateOf("")
    }

    LaunchedEffect(stateIsEdit) {
        if (stateIsEdit.isEdit && stateIsEdit.editData != null) {
            isReadOnly = stateIsEdit.isReadOnly
            id = stateIsEdit.editData.id
            title = stateIsEdit.editData.name
            date = stateIsEdit.editData.date
            color = stateIsEdit.editData.color
            description = stateIsEdit.editData.description
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .testTag(TestTags.BOTTOM_SHEET)
            .fillMaxWidth()
            .size(500.dp)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Icon(
            Icons.Rounded.Close,
            contentDescription = "Close",
            modifier = Modifier
                .size(30.dp)
                .clickable(onClick = onClose)
                .align(Alignment.End)
        )
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ){
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.tertiary),
                label = {
                    Text(text = "Title")
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                    disabledLabelColor = Color.Black,
                    disabledTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                enabled = !isReadOnly,
                value = title,
                onValueChange = { newText ->
                    title = newText
                }

            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.tertiary)
                    .clickable {
                        keyboardController?.hide()
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                selectedDate = "$dayOfMonth/${month + 1}/$year"
                                date = selectedDate
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                label = {
                    Text(text = "Date")
                },
                readOnly = true,
                enabled = false,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                    disabledLabelColor = Color.Black,
                    disabledTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                value = date,
                onValueChange = { newText ->
                    date = newText
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.tertiary),
                label = {
                    Text(text = "Description")
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                    disabledLabelColor = Color.Black,
                    disabledTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                enabled = !isReadOnly,
                minLines = 3,
                maxLines = 3,
                value = description,
                onValueChange = { newText ->
                    description = newText
                }

            )
            Spacer(modifier = Modifier.height(16.dp))
            if (isReadOnly) {
                SlideToDone(isLoading = false, onUnlockRequested = {
                    onEvent(
                        MainEvent.SaveData(
                            TaskModel(
                                id = id,
                                name = title,
                                date = date,
                                color = color,
                                isDone = true,
                                description = description,
                                createdDate = System.currentTimeMillis().toString()
                            )
                        ))
                })
            } else {
                Button(
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(15.dp)),
                    onClick = {
                        onEvent(
                            MainEvent.SaveData(
                                TaskModel(
                                    id = id,
                                    name = title,
                                    date = date,
                                    color = color,
                                    isDone = false,
                                    description = description,
                                    createdDate = System.currentTimeMillis().toString()
                                )
                            ))
                    }
                ) {
                    if (stateSave.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text("Submit", color = MaterialTheme.colorScheme.tertiary)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}