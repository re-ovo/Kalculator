package me.rerere.kalculator.ui.view.calculator

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.bumble.appyx.navmodel.backstack.BackStack
import me.rerere.kalculator.ui.view.CalculatorViewRoot

@Composable
fun MathCalculatorView(
    onNavigate: (CalculatorViewRoot.NavTarget) -> Unit,
){
    TextButton(onClick = { onNavigate(CalculatorViewRoot.NavTarget.Programmer) }) {
        Text("Math")
    }
}