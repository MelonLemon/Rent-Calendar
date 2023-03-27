package com.melonlemon.rentcalendar

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.melonlemon.rentcalendar.feature_analytics.presentation.AnalyticsScreen
import com.melonlemon.rentcalendar.feature_analytics.presentation.AnalyticsViewModel
import com.melonlemon.rentcalendar.feature_home.presentation.HomeScreen
import com.melonlemon.rentcalendar.feature_home.presentation.HomeViewModel
import com.melonlemon.rentcalendar.feature_transaction.presentation.TransactionScreen
import com.melonlemon.rentcalendar.feature_transaction.presentation.TransactionViewModel
import com.melonlemon.rentcalendar.ui.theme.RentCalendarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RentCalendarTheme {
                val items = listOf<Screens>(
                    Screens.HomeScreen,
                    Screens.TransactionsScreen,
                    Screens.AnalyticsScreen
                )
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigation {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            val iconsList = listOf<ImageVector>(
                                Icons.Filled.Home,
                                ImageVector.vectorResource(id = R.drawable.ic_baseline_assignment_24),
                                ImageVector.vectorResource(id = R.drawable.ic_baseline_assessment_24))
                            items.forEachIndexed { index, screen ->
                                BottomNavigationItem(
                                    icon = { Icon(iconsList[index], contentDescription = null) },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            // Pop up to the start destination of the graph to
                                            // avoid building up a large stack of destinations
                                            // on the back stack as users select items
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            // Avoid multiple copies of the same destination when
                                            // reselecting the same item
                                            launchSingleTop = true
                                            // Restore state when reselecting a previously selected item
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                            
                        }
                    }
                ) { innerPadding ->


                    NavHost(
                        navController = navController,
                        startDestination = Screens.HomeScreen.route,
                        modifier = Modifier.padding(innerPadding)
                    ){
                        composable(route = Screens.HomeScreen.route){
                            val viewModel = hiltViewModel<HomeViewModel>()
                            HomeScreen(
                                viewModel = viewModel
                            )
                        }
                        composable(route = Screens.AnalyticsScreen.route){
                            val viewModel = hiltViewModel<AnalyticsViewModel>()
                            AnalyticsScreen(
                                viewModel = viewModel
                            )
                        }
                        composable(route = Screens.TransactionsScreen.route){
                            val viewModel = hiltViewModel<TransactionViewModel>()
                            TransactionScreen(
                                viewModel = viewModel
                            )
                        }
                    }


                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RentCalendarTheme {
        Greeting("Android")
    }
}