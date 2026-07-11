package com.example.reactiontime

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reactiontime.ui.theme.ReactionTimeTheme
import java.util.Random

val random = Random()
var timer: Int = 0
var testActive: Boolean = false
var button: String = "StartTest"
var lastReaction: Int? = null
fun rand(from: Int, to: Int) : Int {
    return random.nextInt(to - from) + from
}

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReactionTimeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    PreTest()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreTest() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Reaction Time Tester",
            modifier = Modifier.padding(16.dp),
            style = TextStyle(fontSize = 20.sp)
        )

        Text(
            text = "Press the button below to start a test!",
            modifier = Modifier.padding(16.dp),
            style = TextStyle(fontSize = 12.sp)
        )

        if (button == "StartTest") {
            FilledTonalButton(onClick = { startTest() },
                modifier = Modifier.padding(16.dp))
            {
                Text("Start")
            }
        } else if (button == "WaitTest") {
            OutlinedButton(onClick = {},
                modifier = Modifier.padding(16.dp))
            {
                Text("Wait")
            }
        } else if (button == "StopTest") {
            Button(onClick = { stopTest() },
                modifier = Modifier.padding(16.dp))
            {
                Text("Stop")
            }
        }
        Text(
            text = "Last Reaction Time: $lastReaction milliseconds.",
            modifier = Modifier.padding(16.dp),
            style = TextStyle(fontSize = 12.sp)
        )
    }
}

fun StartTest() {
    val waittime = rand(0, 5000)
    timer = 0
    button = "WhileTest"

    while(testActive){
        timer += 1
        if (timer == waittime) {
            button = "StopTest"
        }
    }
}