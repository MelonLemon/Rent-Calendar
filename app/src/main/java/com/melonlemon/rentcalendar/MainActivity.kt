package com.melonlemon.rentcalendar

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
import com.melonlemon.rentcalendar.feature_onboarding.presentation.OnBoardingScreen
import com.melonlemon.rentcalendar.feature_onboarding.presentation.OnBoardingViewModel
import com.melonlemon.rentcalendar.feature_onboarding.presentation.SplashViewModel
import com.melonlemon.rentcalendar.feature_transaction.presentation.TransactionScreen
import com.melonlemon.rentcalendar.feature_transaction.presentation.TransactionViewModel
import com.melonlemon.rentcalendar.ui.theme.RentCalendarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    private val splashViewModel: SplashViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition{
            splashViewModel.isLoading.value
        }
        setContent {
            RentCalendarTheme {

                val startScreen by splashViewModel.startDestination.collectAsState()

                val items = listOf<Screens>(
                    Screens.HomeScreen,
                    Screens.TransactionsScreen,
                    Screens.AnalyticsScreen
                )
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination

                        if (currentDestination != null) {
                            if(currentDestination.route != Screens.OnBoardingScreen.route){
                                BottomNavigation(
                                    backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
                                ) {

                                    val iconsList = listOf<ImageVector>(
                                        Icons.Filled.Home,
                                        ImageVector.vectorResource(id = R.drawable.ic_baseline_assignment_24),
                                        ImageVector.vectorResource(id = R.drawable.ic_baseline_assessment_24))
                                    items.forEachIndexed { index, screen ->
                                        BottomNavigationItem(
                                            icon = { Icon(
                                                iconsList[index],
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                                            ) },
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
                        }

                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startScreen,
                        modifier = Modifier.padding(innerPadding)
                    ){

                        composable(route = Screens.OnBoardingScreen.route){
                            val viewModel = hiltViewModel<OnBoardingViewModel>()
                            OnBoardingScreen(
                                onFinish = {
                                    navController.popBackStack()
                                    navController.navigate(Screens.HomeScreen.route)
                                },
                                viewModel = viewModel
                            )
                        }
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

//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    RentCalendarTheme {
//        Greeting("Android")
//    }
//}


