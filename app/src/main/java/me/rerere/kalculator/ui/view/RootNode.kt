package me.rerere.kalculator.ui.view

import android.os.Parcelable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.bumble.appyx.navmodel.backstack.operation.pop
import com.bumble.appyx.navmodel.backstack.operation.push
import com.bumble.appyx.navmodel.backstack.transitionhandler.rememberBackstackSlider
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import me.rerere.kalculator.R

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
        val state = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val children = backStack.childrenAsState()
        val currentKey by remember {
            derivedStateOf { children.value.last().key.navTarget }
        }
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
                            selected = currentKey == NavTarget.Calculator,
                            icon = {
                                Icon(Icons.Outlined.Calculate, contentDescription = null)
                            },
                            onClick = {
                                scope.launch {
                                    state.close()
                                    backStack.push(NavTarget.Calculator)
                                }
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text("设置") },
                            selected = currentKey == NavTarget.Setting,
                            icon = {
                                Icon(Icons.Outlined.Settings, contentDescription = null)
                            },
                            onClick = {
                                scope.launch {
                                    state.close()
                                    backStack.push(NavTarget.Setting)
                                }
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text("关于") },
                            icon = {
                                Icon(Icons.Outlined.Info, contentDescription = null)
                            },
                            selected = currentKey == NavTarget.About,
                            onClick = {
                                scope.launch {
                                    state.close()
                                    backStack.push(NavTarget.About)
                                }
                            }
                        )
                    }
                }
            }
        ) {

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = when (children.value.last().key.navTarget) {
                                    NavTarget.Calculator -> "计算器"
                                    NavTarget.Setting -> "设置"
                                    NavTarget.About -> "关于"
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
                    modifier = Modifier.padding(it),
                    transitionHandler = rememberBackstackSlider(
                        transitionSpec = { spring(stiffness = Spring.StiffnessMedium) }
                    ),
                )
            }
        }
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            is NavTarget.Calculator -> CalculatorViewRoot(buildContext, backStack) {
                backStack.push(it)
            }

            is NavTarget.Setting -> node(buildContext) {
                SettingView()
            }

            is NavTarget.About -> node(buildContext) {
                AboutView()
            }
        }
    }
}