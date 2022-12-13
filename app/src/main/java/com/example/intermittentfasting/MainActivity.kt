package com.example.intermittentfasting

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.intermittentfasting.currentfast.CurrentFastScreen
import com.example.intermittentfasting.manualentry.ManualEntryScreen
import com.example.intermittentfasting.pastfasts.PastFastsScreen
import com.example.intermittentfasting.ui.theme.IntermittentFastingTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

enum class IFTabs(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    MANUAL_ENTRY("Manual", Icons.Default.List, IFDestinations.MANUAL_ENTRY),
    FAST("Fast", Icons.Default.DateRange, IFDestinations.FAST),
    PAST_FASTS("Past Fasts", Icons.Default.ShoppingCart, IFDestinations.PAST_FASTS)
}

object IFDestinations {
    const val MANUAL_ENTRY = "manual_entry"
    const val FAST = "fast"
    const val PAST_FASTS = "past_fasts"
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IntermittentFastingTheme {
                // A surface container using the 'background' color from the theme
                IMApp()
            }
        }
    }
}

@Composable
fun IMApp() {
    val navController = rememberNavController()
    val tabs = remember {
        IFTabs.values()
    }
    Scaffold(
        bottomBar = {
            IFBottomBar(navController = navController, tabs = tabs)

        }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = IFTabs.FAST.route
        ) {
            composable(IFTabs.FAST.route) {
                CurrentFastScreen()
            }
            composable(IFTabs.MANUAL_ENTRY.route) {
                ManualEntryScreen()
            }
            composable(IFTabs.PAST_FASTS.route) {
                PastFastsScreen()

            }

        }
    }
}


@Composable
fun IFBottomBar(
    navController: NavController,
    tabs: Array<IFTabs>
) {

//    val routes = remember { IFTabs.values().map { it.route } }
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: IFTabs.FAST.route
//            val currentDestination = navBackStackEntry?.destination
        tabs.forEach { tab ->
            BottomNavigationItem(
                icon = { Icon(tab.icon, contentDescription = null) },
                selected = currentRoute == tab.route,
                onClick = {
                    navController.navigate(tab.route) {
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
                },
                alwaysShowLabel = true,
                label = {
                    Text(text = tab.title)
                },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Red
            )
        }
    }
}