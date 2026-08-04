package com.example.reactiontime

//android imports for the project
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reactiontime.ui.theme.ReactionTimeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Random
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle
import kotlin.time.Duration.Companion.milliseconds

//lambda function for random & Java rand function
val random = Random()
fun rand(from: Int, to: Int) : Int {
    return random.nextInt(to - from) + from
}

//mutable state variables (for UI updates when they change)
var buttonMode by mutableStateOf("START")
var lastTime by mutableIntStateOf(0)
var displayInstructions by mutableStateOf(false)
var displayPrevious by mutableStateOf(false)
var pastScores = mutableListOf<Int>()


//standard variables
var signalTimeMsStart: Long = 0L
var signalTimeMsEnd: Long = 0L

//to create instance of app, automatically inserted by IntelliJ
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


//function behind app
@Preview(showBackground = true)
@Composable
fun Go() {
    val context = LocalContext.current //to see context from other apps or services on the users device when needed.
    val coroutineScope = rememberCoroutineScope() //allows co-routines

    val dynamicBackgroundBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha=0.7f),
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha=0.5f),
            MaterialTheme.colorScheme.background
        )
    ) //sets material you theme in the app

    Box(modifier = Modifier.fillMaxSize().background(brush = dynamicBackgroundBrush)){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Reaction Time Tester",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = TextStyle(fontSize = 30.sp)
            )
            TextButton(onClick = { displayInstructions = !displayInstructions }) { Text("See Instructions") }
                if (displayInstructions){ //toggleable button logic
                    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                        Text(text = "1. Press START to start the test.", style = TextStyle(fontSize = 12.sp, fontStyle = FontStyle.Italic))
                        Text(text = "2. Wait a random number of milliseconds.", style = TextStyle(fontSize = 12.sp, fontStyle = FontStyle.Italic))
                        Text(text = "3. Press STOP when it appears to end the test.", style = TextStyle(fontSize = 12.sp, fontStyle = FontStyle.Italic))
                        Text(text = "4. Review your score next to 'Last Reaction Time'.", style = TextStyle(fontSize = 12.sp, fontStyle = FontStyle.Italic))
                        Text(text = "5. Toggle 'See History' to see past attempts.", style = TextStyle(fontSize = 12.sp, fontStyle = FontStyle.Italic))
                        Text(text = "Note: 'See History' is available after 3 attempts.", style = TextStyle(fontSize = 12.sp, fontStyle = FontStyle.Italic))
                    }
                }
            Text(
                text = "Last reaction time: ${lastTime}ms",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = TextStyle(fontSize = 25.sp)
            )
            if ((pastScores.lastIndex)>=2)
                TextButton(onClick = { displayPrevious = !displayPrevious }) { Text("See History") }
                if (displayPrevious){ //history toggleable
                    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                        Text(text = "Lowest: ${pastScores.minWith(Comparator.comparingInt {it})}ms")//finds and displays min result
                        Text(text = "Average: ${findAverage()}ms")
                        Text(text = "Highest: ${pastScores.maxWith(Comparator.comparingInt {it})}ms") //finds and displays max result
                        Text(text = " ")
                        Text(text = "Attempt ${(pastScores.lastIndex)+1}: ${pastScores[(pastScores.lastIndex)]}ms")
                        Text(text = "Attempt ${(pastScores.lastIndex)}: ${pastScores[(pastScores.lastIndex)-1]}ms")
                        Text(text = "Attempt ${(pastScores.lastIndex)-1}: ${pastScores[(pastScores.lastIndex)-2]}ms")
                    }
                }
            when (buttonMode) {
                "START" -> {
                    FilledTonalButton(
                        onClick = { coroutineScope.launch { wait1() } },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("START")
                    }
                }
                "WAIT" -> {
                    OutlinedButton(
                        { Toast.makeText(context, "Wait for end button.", Toast.LENGTH_LONG).show() }, //shows text tip at the bottom asking user to wait until "END" appears
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("WAIT")
                    }
                }
                "END" -> {
                    Button(onClick = { end() }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text("END")
                    }
                }
            }
        }
    }
}

suspend fun wait1(){
    buttonMode = "WAIT"
    signalTimeMsStart = System.currentTimeMillis()
    val waitDuration = rand(1000, 3000).toLong()
    delay(waitDuration.milliseconds)
    signalTimeMsStart += waitDuration
    buttonMode = "END"
}

fun end(){
    buttonMode = "START"
    signalTimeMsEnd = System.currentTimeMillis()
    lastTime = (signalTimeMsEnd - signalTimeMsStart).toInt()
    pastScores.addLast(lastTime)
}

fun findAverage(): Int {
    val average = pastScores.average().toInt()
    return average
}