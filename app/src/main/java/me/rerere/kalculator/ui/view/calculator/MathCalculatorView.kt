package me.rerere.kalculator.ui.view.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.rerere.kalculator.ui.view.CalculatorViewRoot
import me.rerere.kalculator.ui.widgets.VerticalSpace
import me.rerere.kalculator.util.eval

@Composable
fun MathCalculatorView(
    onNavigate: (CalculatorViewRoot.NavTarget) -> Unit,
) {
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    LaunchedEffect(expression) {
        kotlin.runCatching {
            result = eval(expression).toPlainString()
        }
    }
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(8.dp)
        ) {
            VerticalSpace {
                Text(
                    text = expression,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.End
                )
                Text(
                    text = result,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.End
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row {
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = "1",
                    onClick = {
                        expression += "1"
                    }
                )
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = "2",
                    onClick = {
                        expression += "2"
                    }
                )
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = "3",
                    onClick = {
                        expression += "3"
                    }
                )
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = "+",
                    color = MaterialTheme.colorScheme.primary,
                    onClick = {
                        expression += "+"
                    }
                )
            }
            Row {
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = "4",
                    onClick = {
                        expression += "4"
                    }
                )
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = "5",
                    onClick = {
                        expression += "5"
                    }
                )
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = "6",
                    onClick = {
                        expression += "6"
                    }
                )
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    color = MaterialTheme.colorScheme.primary,
                    text = "-",
                    onClick = {
                        expression += "-"
                    }
                )
            }
            Row {
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = "7",
                    onClick = {
                        expression += "7"
                    }
                )
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = "8",
                    onClick = {
                        expression += "8"
                    }
                )
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = "9",
                    onClick = {
                        expression += "9"
                    }
                )
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    color = MaterialTheme.colorScheme.primary,
                    text = "×",
                    onClick = {
                        expression += "×"
                    }
                )
            }
            Row {
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = "0",
                    onClick = {
                        expression += "0"
                    }
                )
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = ".",
                    onClick = {
                        expression += "."
                    }
                )
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = "C",
                    onClick = {
                        expression = ""
                        result = ""
                    }
                )
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    color = MaterialTheme.colorScheme.primary,
                    text = "÷",
                    onClick = {
                        expression += "÷"
                    }
                )
            }
            Row {
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = "(",
                    onClick = {
                        expression += "("
                    }
                )
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = ")",
                    onClick = {
                        expression += ")"
                    }
                )
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = "←",
                    onClick = {
                        expression = expression.dropLast(1)
                    }
                )
                CalButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = "=",
                    onClick = {
                        kotlin.runCatching {
                            result = eval(expression).toPlainString()
                        }.onFailure {
                            result = "Error"
                            it.printStackTrace()
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun CalButton(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.tertiaryContainer,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .aspectRatio(1f)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(8.dp)
        )
    }
}