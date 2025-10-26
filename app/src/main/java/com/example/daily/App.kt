package com.example.daily

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.daily.ui.component.TopDailyBar
import com.example.daily.ui.screen.MainScreen
import com.example.daily.ui.screen.SpecCreatorScreen
import com.example.daily.ui.screen.TopicSpecsScreen
import com.example.daily.ui.viewmodel.MainViewModel
import com.example.daily.ui.viewmodel.SpecCreatorVM

@Composable
fun App(
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopDailyBar(
                viewModel = mainViewModel,
                navigateSpecs = { navController.navigate("topic_specs") },
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    ) { innerPadding ->
        NavHost(
            startDestination = "home",
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = "home") {
                MainScreen(
                    viewModel = mainViewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                )
            }
            composable(route = "topic_specs") {
                TopicSpecsScreen(
                    viewModel = mainViewModel,
                    navigateToSpecCreator = { navController.navigate("spec_creator") },
                    navigateHome = { navController.navigate("home") }
                )
            }
            composable(route = "spec_creator") { backStackEntry ->
                val owner = remember(backStackEntry)  {
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
                    navigateTopicSpecs = { navController.navigate("topic_specs")}
                )
            }
        }
    }
}