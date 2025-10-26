package com.example.daily

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.daily.ui.screen.MainScreen
import com.example.daily.ui.component.TopDailyBar
import com.example.daily.ui.theme.DailyTheme
import com.example.daily.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.ui.unit.dp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyTheme {
                val viewModel: MainViewModel = hiltViewModel()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    topBar = {
                        TopDailyBar(
                            viewModel = viewModel,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                ) { innerPadding ->
                    MainScreen(
                        viewModel = viewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}