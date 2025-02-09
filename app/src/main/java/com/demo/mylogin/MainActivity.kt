package com.demo.mylogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.demo.mylogin.ui.screens.NavigationRoutes
import com.demo.mylogin.ui.screens.authenticatedGraph
import com.demo.mylogin.ui.screens.unauthenticatedGraph
import com.demo.mylogin.ui.theme.ComposeLoginTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLoginTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainApp() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                testTagsAsResourceId = true
            }
        ,
        color = MaterialTheme.colorScheme.background
    ) {
        MainAppNavHost()
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier.semantics {
            testTagsAsResourceId = true
        },
        navController = navController,
        startDestination = NavigationRoutes.Unauthenticated.NavigationRoute.route
    ) {
        // Unauthenticated user flow screens
        unauthenticatedGraph(navController = navController)

        // Authenticated user flow screens
        authenticatedGraph(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeLoginTheme {
        MainApp()
    }
}