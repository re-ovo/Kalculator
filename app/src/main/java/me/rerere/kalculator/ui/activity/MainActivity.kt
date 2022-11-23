package me.rerere.kalculator.ui.activity

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeActivity
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.core.node.node
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.transitionhandler.rememberBackstackSlider
import kotlinx.parcelize.Parcelize
import me.rerere.kalculator.ui.theme.KalculatorTheme
import me.rerere.kalculator.ui.view.CalculatorViewRoot

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

class RootNode(
    buildContext: BuildContext,
    private val backStack: BackStack<NavTarget> = BackStack(
        initialElement = NavTarget.Calculator,
        savedStateMap = buildContext.savedStateMap
    )
) : ParentNode<RootNode.NavTarget>(
    buildContext = buildContext,
    navModel = backStack,
) {
    sealed class NavTarget : Parcelable {
        @Parcelize
        object Calculator : NavTarget()

        @Parcelize
        object Setting : NavTarget()

        @Parcelize
        object About : NavTarget()
    }

    @Composable
    override fun View(modifier: Modifier) {
        Children(
            navModel = backStack,
            modifier = Modifier.fillMaxSize(),
            transitionHandler = rememberBackstackSlider()
        )
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            is NavTarget.Calculator -> CalculatorViewRoot(buildContext)

            is NavTarget.Setting -> node(buildContext) {
                Text("Child2")
            }

            is NavTarget.About -> node(buildContext) {
                Text("Child3")
            }
        }
    }
}

