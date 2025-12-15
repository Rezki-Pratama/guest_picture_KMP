package com.sloth.proactify.ui.main.components

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.sloth.proactify.ui.main.MainEvent
import com.sloth.proactify.ui.main.MainViewModel

@Composable
fun TaskScreen(viewModel: MainViewModel) {
    val state by viewModel.state.collectAsState()

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        viewModel.onEvent(MainEvent.GetData(id = ""))
        Log.d("MESSAGE",state.toString())
    }

    if (state.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(), // Fills the available space
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            CircularProgressIndicator()
        }
    } else if (state.data.isEmpty() || state.isFailure) {
        Column(
            modifier = Modifier.fillMaxSize().padding(2.dp), // Fills the available space
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(text = if (state.isFailure) state.message else "No tasks here yet. Let's add a new task to get started!",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White
                ))
        }
    } else if (state.isSuccess) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            state.data.forEach { item ->
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(item.color)
                        .fillMaxWidth()
                        .padding(16.dp)
                        .animateContentSize(animationSpec = tween(durationMillis = 500))
                ) {
                    Column {
                        var isShowMenu by remember { mutableStateOf(false) }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f)
                            )
                            if (item.isDone) {
                                Box(
                                    modifier = Modifier
                                        .clip(
                                            MaterialTheme.shapes.small.copy(
                                                CornerSize(percent = 50)
                                            )
                                        )
                                        .background(MaterialTheme.colorScheme.background.copy(0.3f))
                                        .padding(10.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Done",
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable {
                                                isShowMenu = true
                                            })
                                }
                            } else {
                                    Box(
                                        modifier = Modifier
                                            .animateContentSize()
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(
                                                MaterialTheme.colorScheme.background.copy(
                                                    0.3f
                                                )
                                            )
                                            .padding(5.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            if (isShowMenu) {
                                                Icon(
                                                    Icons.Default.Edit,
                                                    contentDescription = "Edit",
                                                    tint = Color.White,
                                                    modifier = Modifier
                                                        .size(30.dp)
                                                        .clickable {
                                                            viewModel.onEvent(
                                                                MainEvent.IsEdit(
                                                                    item,
                                                                    readOnly = false
                                                                )
                                                            )
                                                        })
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Icon(
                                                    Icons.Default.List,
                                                    contentDescription = "Detail",
                                                    tint = Color.White,
                                                    modifier = Modifier
                                                        .size(30.dp)
                                                        .clickable {
                                                            viewModel.onEvent(
                                                                MainEvent.IsEdit(
                                                                    item,
                                                                    readOnly = true
                                                                )
                                                            )
                                                        })
                                                Spacer(modifier = Modifier.width(10.dp))
                                                if (isShowMenu) {
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(
                                                                MaterialTheme.shapes.small.copy(
                                                                    CornerSize(percent = 50)
                                                                )
                                                            )
                                                            .background(Color.White)
                                                    ) {
                                                        Icon(
                                                            Icons.Default.Close ,
                                                            contentDescription = "Menu",
                                                            modifier = Modifier
                                                                .size(30.dp)
                                                                .clickable {
                                                                    isShowMenu = false
                                                                })
                                                    }
                                                } else {
                                                    Icon(
                                                        Icons.Default.Menu,
                                                        contentDescription = "Menu",
                                                        tint = Color.White,
                                                        modifier = Modifier
                                                            .size(30.dp)
                                                            .clickable {
                                                                isShowMenu = false
                                                            })
                                                }
                                            } else {
                                                Icon(
                                                    Icons.Default.Menu,
                                                    contentDescription = "Menu",
                                                    modifier = Modifier
                                                        .size(30.dp)
                                                        .clickable {
                                                            isShowMenu = true
                                                        })
                                            }
                                        }
                                    }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = item.date, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = item.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}