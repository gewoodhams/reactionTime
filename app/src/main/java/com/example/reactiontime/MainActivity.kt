package com.example.reactiontime

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reactiontime.ui.theme.ReactionTimeTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.util.Random
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

//fixed
val random = Random()

//variable
var buttonMode by mutableStateOf("START") //PINK
var lastTime by mutableIntStateOf(0) //BLUE


//runtime
fun rand(from: Int, to: Int) : Int {
    return random.nextInt(to - from) + from
}
var signalTimeMsStart: Long = 0L
var signalTimeMsEnd: Long = 0L
var signalTimeMsRandEnd: Long = 0L

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReactionTimeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Go()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Go() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Reaction Time Tester",
            modifier = Modifier.padding(16.dp),
            style = TextStyle(fontSize = 20.sp)
        )
        Text(
            text = "Last reaction time: $lastTime",
            modifier = Modifier.padding(16.dp),
            style = TextStyle(fontSize = 12.sp)
        )
        if (buttonMode == "START") {
            FilledTonalButton(onClick = { coroutineScope.launch { wait1() } }) {
                Text("START")
            }
        } else if (buttonMode == "WAIT") {
            OutlinedButton({ Toast.makeText(context, "Wait for end button.", Toast.LENGTH_LONG).show() }) {
                Text("WAIT")
            }
        } else if (buttonMode == "END") {
            Button(onClick = { end() }) {
                Text("END")
            }
        }
    }
}

suspend fun wait1(){
    buttonMode = "WAIT"
    signalTimeMsStart = System.currentTimeMillis()
    val waitDuration = rand(1000, 3000).toLong()
    delay(waitDuration)
    buttonMode = "END"
}

fun end(){
    buttonMode = "START"
    signalTimeMsEnd = System.currentTimeMillis()
    lastTime = (signalTimeMsEnd - signalTimeMsStart).toInt()
}