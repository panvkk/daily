package com.example.daily

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.daily.ui.component.TopDailyBar
import com.example.daily.ui.screen.MainScreen
import com.example.daily.ui.screen.SpecCreatorScreen
import com.example.daily.ui.viewmodel.MainViewModel
import com.example.daily.ui.viewmodel.SpecCreatorVM
import kotlinx.coroutines.launch

@Composable
fun App(
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    // Navigation Drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val screenHierarchy = mapOf(
        "home" to 1,
        "spec_creator" to 2
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopDailyBar(
                viewModel = mainViewModel,
                openDrawer = {
                    scope.launch {
                        if(drawerState.isClosed) drawerState.open() else drawerState.close()
                    } },
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    ) { innerPadding ->
        NavHost(
            startDestination = "home",
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                val from = screenHierarchy[initialState.destination.route] ?: 0
                val to = screenHierarchy[targetState.destination.route] ?: 0
                if (to > from) {
                    slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(200)) +
                            fadeIn(animationSpec = tween(200))
                } else {
                    slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(200)) +
                            fadeIn(animationSpec = tween(200))
                }
            },
            exitTransition = {
                val from = screenHierarchy[initialState.destination.route] ?: 0
                val to = screenHierarchy[targetState.destination.route] ?: 0
                if(to > from) {
                    slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(200)) +
                            fadeOut(animationSpec = tween(200) )
                } else {
                    slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(200)) +
                            fadeOut(animationSpec = tween(200) )
                }
            }
        ) {
            composable(route = "home", ) {
                MainScreen(
                    viewModel = mainViewModel,
                    drawerState = drawerState,
                    navigateSpecCreator = { navController.navigate("spec_creator") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            composable(route = "spec_creator") { backStackEntry ->
                val owner = remember(backStackEntry) {
                    navController.getBackStackEntry("spec_creator")
                }
                val specCreatorVM: SpecCreatorVM = hiltViewModel(
                    viewModelStoreOwner = owner
                )
                SpecCreatorScreen(
                    viewModel = specCreatorVM,
                    onCrateSpec = { topicSpec ->
                        mainViewModel.addTopicSpec(topicSpec)
                    },
                    navigateHome = { navController.navigate("home") }
                )
            }
        }
    }
}