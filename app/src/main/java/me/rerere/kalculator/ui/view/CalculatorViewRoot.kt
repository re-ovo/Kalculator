package me.rerere.kalculator.ui.view

import android.os.Parcelable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.core.node.node
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.push
import kotlinx.parcelize.Parcelize
import me.rerere.kalculator.ui.view.calculator.MathCalculatorView

class CalculatorViewRoot(
    buildContext: BuildContext,
    private val parentBackStack: BackStack<RootNode.NavTarget>,
    private val backStack: BackStack<NavTarget> = BackStack(
        initialElement = NavTarget.Math,
        savedStateMap = buildContext.savedStateMap
    ),
    private val output: (RootNode.NavTarget) -> Unit
) : ParentNode<CalculatorViewRoot.NavTarget>(
    buildContext = buildContext,
    navModel = backStack,
) {
    sealed class NavTarget : Parcelable {
        @Parcelize
        object Math : NavTarget()

        @Parcelize
        object Programmer : NavTarget()
    }

    @Composable
    override fun View(modifier: Modifier) {
        Children(
            navModel = backStack
        )
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            NavTarget.Math -> node(buildContext) {
                MathCalculatorView(
                    onNavigate = {
                        backStack.push(it)
                    }
                )
            }

            NavTarget.Programmer -> node(buildContext) {
                Text(text = "Programmer")
            }
        }
    }
}