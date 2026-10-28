package com.example.ruhe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MeditationScreen()
            }
        }
    }
}

@Composable
fun MeditationScreen() {
    var minutes by remember { mutableIntStateOf(5) }
    var secondsLeft by remember { mutableIntStateOf(0) }
    var running by remember { mutableStateOf(false) }
    var finished by remember { mutableStateOf(false) }

    LaunchedEffect(running) {
        while (running && secondsLeft > 0) {
            delay(1_000)
            secondsLeft--
        }
        if (secondsLeft == 0 && running) {
            running = false
            finished = true
        }
    }

    val displayedTime =
        if (running || secondsLeft > 0) secondsLeft else minutes * 60

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Ruhe", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(12.dp))
            Text(if (finished) "Gut gemacht!" else "Atme ein. Atme aus.")
            Spacer(Modifier.height(32.dp))

            Surface(
                modifier = Modifier.size(210.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        "%02d:%02d".format(displayedTime / 60, displayedTime % 60),
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            if (!running && secondsLeft == 0) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    listOf(3, 5, 10).forEach { option ->
                        TextButton(onClick = {
                            minutes = option
                            finished = false
                        }) {
                            Text(
                                if (minutes == option) "[$option Min.]" 
                                else "$option Min."
                            )
                        }
                    }
                }
            }

            Button(onClick = {
                if (running) {
                    running = false
                } else {
                    if (secondsLeft == 0) secondsLeft = minutes * 60
                    finished = false
                    running = true
                }
            }) {
                Text(
                    when {
                        running -> "Pause"
                        secondsLeft > 0 -> "Weiter"
                        else -> "Meditation starten"
                    }
                )
            }

            if (!running && (secondsLeft > 0 || finished)) {
                TextButton(onClick = {
                    running = false
                    secondsLeft = 0
                    finished = false
                }) {
                    Text("Zurücksetzen")
                }
            }
        }
    }
}
