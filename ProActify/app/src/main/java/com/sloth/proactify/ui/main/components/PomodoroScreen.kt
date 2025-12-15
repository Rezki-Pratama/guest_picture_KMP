package com.sloth.proactify.ui.main.components

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sloth.proactify.R
import com.sloth.proactify.ui.main.POMODORO_TIMER_SECONDS
import com.sloth.proactify.ui.main.REST_TIMER_SECONDS
import com.sloth.proactify.ui.main.SECONDS_IN_A_MINUTE
import com.sloth.proactify.ui.main.TimerType
import com.sloth.proactify.ui.main.TimerViewModel
import com.sloth.proactify.ui.theme.ProActifyTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun PomodoroScreen() {
    val viewModel = koinViewModel<TimerViewModel>()
    val timerState by viewModel.timerState.collectAsState()
    val timerSeconds =
        if (timerState.lastTimer == TimerType.POMODORO)
            POMODORO_TIMER_SECONDS
        else
            REST_TIMER_SECONDS
    val mediaPlayer = MediaPlayer.create(LocalContext.current, R.raw.sound)

    LaunchedEffect(key1 = timerState.remainingSeconds) {
        if (timerState.remainingSeconds == 0L)
            mediaPlayer.start()
    }

    Scaffold(

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            val progress =
                timerState.remainingSeconds.toFloat() / (timerSeconds.toFloat())
            val minutes = timerState.remainingSeconds / SECONDS_IN_A_MINUTE
            val seconds = timerState.remainingSeconds - (minutes * SECONDS_IN_A_MINUTE)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 10.dp,
                        modifier = Modifier
                            .width(180.dp)
                            .height(180.dp),
                        progress = progress
                    )
                    Text(
                        text = "Timer\n$minutes : $seconds",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        modifier = Modifier.padding(bottom = 20.dp),
                        onClick = {
                            if (timerState.isPaused) {
                                viewModel.startTimer(timerState.remainingSeconds)
                            } else {
                                viewModel.stopTimer()
                            }
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = if (timerState.isPaused) Icons.Filled.PlayArrow
                            else ImageVector.vectorResource(
                                id = R.drawable.baseline_pause_24
                            ),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    IconButton(
                        modifier = Modifier.padding(bottom = 20.dp),
                        onClick = {
                            viewModel.resetTimer(POMODORO_TIMER_SECONDS)
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProActifyTheme {
        PomodoroScreen()
    }
}