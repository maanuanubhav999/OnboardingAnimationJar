package com.asraven.jaranimationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.asraven.jaranimationapp.ui.component.LandingScreen
import com.asraven.jaranimationapp.ui.component.OnboardingScreen
import com.asraven.jaranimationapp.ui.theme.JarAnimationAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JarAnimationAppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "onboarding",
            modifier = Modifier.fillMaxSize()
        ) {
            composable("onboarding") {
                OnboardingScreen(
                    paddingValues = innerPadding,
                    onNavigateToLanding = { navController.navigate("landing") }
                )
            }
            composable("landing") {
                LandingScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
