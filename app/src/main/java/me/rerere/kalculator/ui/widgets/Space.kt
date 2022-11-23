package me.rerere.kalculator.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class SpaceSize(val value: Dp) {
    Small(4.dp),
    Normal(8.dp),
    Large(16.dp),
}

@Composable
fun VerticalSpace(
    modifier: Modifier = Modifier,
    size: SpaceSize = SpaceSize.Normal,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(size.value)
    ) {
        content()
    }
}

@Composable
fun HorizontalSpace(
    modifier: Modifier = Modifier,
    size: SpaceSize = SpaceSize.Normal,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(size.value)
    ) {
        content()
    }
}