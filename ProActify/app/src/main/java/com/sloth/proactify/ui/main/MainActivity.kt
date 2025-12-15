package com.sloth.proactify.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.sloth.proactify.ui.main.components.BottomSheetContent
import com.sloth.proactify.ui.main.components.PomodoroScreen
import com.sloth.proactify.ui.main.components.ProfileScreen
import com.sloth.proactify.ui.main.components.TaskScreen
import com.sloth.proactify.ui.theme.ProActifyTheme
import com.sloth.proactify.utils.TestTags
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProActifyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainCompose()
                }
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Task : Screen("task", "Task", Icons.Default.Home)
    data object Pomodoro : Screen("pomodoro", "Pomodoro", Icons.Default.Settings)
    data object Profile : Screen("profile", "Profile", Icons.Default.Person)
}

@Composable
fun MainCompose() {
    val viewModel = koinViewModel<MainViewModel>()
    val stateSave by viewModel.stateSave.collectAsState()
    val stateIsEdit by viewModel.stateIsEdit.collectAsState()
    val navController = rememberNavController()
    var isFabClicked by remember { mutableStateOf(false) }
    val scale = remember { Animatable(1f) }
    val fabShape by animateIntAsState(targetValue = if (isFabClicked) 10 else 50, animationSpec = tween(durationMillis = 500),
        label = ""
    )

    LaunchedEffect(isFabClicked) {
        if (isFabClicked) {
            scale.animateTo(50f, animationSpec = tween(durationMillis = 500))
        } else {
            scale.animateTo(1f, animationSpec = tween(durationMillis = 500))
        }
    }



    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(stateSave) {
        if (stateSave.isSuccess) {
            isFabClicked = false
            scope.launch {
                snackBarHostState.showSnackbar(
                    message = stateSave.message
                )
            }
        } else if (stateSave.isFailure) {
            scope.launch {
                snackBarHostState.showSnackbar(
                    message = stateSave.message
                )
            }
        }
    }

    LaunchedEffect(stateIsEdit) {
        Log.d("EDIT",stateIsEdit.isEdit.toString())
        if (stateIsEdit.isEdit) {
            isFabClicked = true
        }
    }



    Scaffold(
        backgroundColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.offset(y = (-10).dp)
            )
        },
        bottomBar = {
            BottomNavBar(navController = navController)
        },
        floatingActionButton = {
            if (navController.currentBackStackEntryAsState().value?.destination?.route == Screen.Task.route) {
                Box(
                    contentAlignment = if (isFabClicked && scale.value > 2) Alignment.TopCenter
                                        else Alignment.Center,
                    modifier = Modifier
                        .size(70.dp * scale.value)
                        .offset(x =  when {
                            isFabClicked && scale.value > 2 -> (0).dp
                            scale.value < 3 -> (-70).dp
                            else -> 0.dp // Optional: handle other cases if needed
                        })
                        .clip(MaterialTheme.shapes.small.copy(CornerSize(percent = fabShape)))
                        .clickable {
                            isFabClicked = true
                        }
                        .testTag(TestTags.FAB)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    if (isFabClicked && scale.value > 1) {
                        BottomSheetContent(
                            stateSave = stateSave,
                            stateIsEdit = stateIsEdit,
                            onEvent = viewModel::onEvent,
                            onClose = {
                                isFabClicked = false
                            })
                    } else if (scale.value < 3) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add",
                            modifier = Modifier
                                .size(30.dp))
                    }
                }
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        NavGraph(navController = navController, paddingValues = innerPadding, viewModel = viewModel)
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(Screen.Task, Screen.Pomodoro, Screen.Profile)
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.secondary,
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .shadow(8.dp, RoundedCornerShape(24.dp))
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(
                    screen.icon,
                    contentDescription = screen.title,
                ) },
                label = {
                    Text(
                        screen.title,
                        color = if (currentRoute == screen.route) MaterialTheme.colorScheme.background
                                else Color.White
                    )
                },
                selected = navController.currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                alwaysShowLabel = true,
                selectedContentColor = MaterialTheme.colorScheme.background,
                unselectedContentColor = Color.White,
            )
        }
    }
}

@Composable
fun NavGraph(navController: NavHostController, paddingValues: PaddingValues, viewModel: MainViewModel) {
    Box(modifier = Modifier.padding(paddingValues)) {
        NavHost(navController, startDestination = Screen.Task.route) {
            composable(Screen.Task.route, deepLinks = listOf(navDeepLink { uriPattern = "app://task" })) {
                TaskScreen(viewModel = viewModel)
            }
            composable(Screen.Pomodoro.route, deepLinks = listOf(navDeepLink { uriPattern = "app://pomodoro" })) {
                PomodoroScreen()
            }
            composable(Screen.Profile.route, deepLinks = listOf(navDeepLink { uriPattern = "app://profile" })) {
                ProfileScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    ProActifyTheme {
        MainCompose()
    }
}