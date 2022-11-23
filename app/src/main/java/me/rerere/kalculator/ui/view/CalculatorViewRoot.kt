package me.rerere.kalculator.ui.view

import android.os.Parcelable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.composable.childrenAsState
import com.bumble.appyx.core.composable.visibleChildrenAsState
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.core.node.node
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.push
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import me.rerere.kalculator.R
import me.rerere.kalculator.ui.activity.RootNode
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
        val state = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerState = state,
            drawerContent = {
                ModalDrawerSheet {
                    Column(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                        )
                        NavigationDrawerItem(
                            label = { Text("计算器") },
                            selected = true,
                            icon = {
                                Icon(Icons.Outlined.Calculate, contentDescription = null)
                            },
                            onClick = {
                                output(RootNode.NavTarget.Calculator)
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text("设置") },
                            selected = false,
                            icon = {
                                Icon(Icons.Outlined.Settings, contentDescription = null)
                            },
                            onClick = {
                                output(RootNode.NavTarget.Setting)
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text("关于") },
                            icon = {
                                Icon(Icons.Outlined.Info, contentDescription = null)
                            },
                            selected = false,
                            onClick = {
                                output(RootNode.NavTarget.About)
                            }
                        )
                    }
                }
            }
        ) {
            val children = backStack.childrenAsState()
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = when(children.value.last().key.navTarget) {
                                    NavTarget.Math -> "计算器"
                                    NavTarget.Programmer -> "程序员"
                                }
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        state.open()
                                    }
                                }
                            ) {
                                Icon(Icons.Outlined.Menu, "Menu")
                            }
                        }
                    )
                }
            ) {
                Children(
                    navModel = backStack,
                    modifier = Modifier.padding(it)
                )
            }
        }
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