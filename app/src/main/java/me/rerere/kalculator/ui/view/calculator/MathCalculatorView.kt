package me.rerere.kalculator.ui.view.calculator

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bumble.appyx.navmodel.backstack.BackStack
import me.rerere.kalculator.ui.view.CalculatorViewRoot
import me.rerere.kalculator.ui.widgets.VerticalSpace

@Composable
fun MathCalculatorView(
    onNavigate: (CalculatorViewRoot.NavTarget) -> Unit,
){
    var counter by remember { mutableStateOf(0) }
    VerticalSpace {
        TextButton(onClick = { onNavigate(CalculatorViewRoot.NavTarget.Programmer) }) {
            Text("Math")
        }
        Button(onClick = { counter++ }) {
            Text("Counter: $counter")
        }
    }
}