package me.rerere.kalculator.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeActivity
import me.rerere.kalculator.ui.theme.KalculatorTheme
import me.rerere.kalculator.ui.view.RootNode

class MainActivity : NodeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContent {
            KalculatorTheme {
                NodeHost(
                    integrationPoint = appyxIntegrationPoint,
                ) {
                    RootNode(it)
                }
            }
        }
    }
}

