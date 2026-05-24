package com.example.restomasrafli

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.restomasrafli.ui.screen.DetailMenuScreen
import com.example.restomasrafli.ui.screen.EditProfileScreen
import com.example.restomasrafli.ui.screen.HomeScreen
import com.example.restomasrafli.ui.screen.MenuScreen
import com.example.restomasrafli.ui.screen.ProfileScreen
import com.example.restomasrafli.ui.theme.RestoMasRafliTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val sharedPreferences = remember {
                getSharedPreferences("resto_prefs", Context.MODE_PRIVATE)
            }
            var isDarkMode by remember {
                mutableStateOf(sharedPreferences.getBoolean("is_dark_mode", false))
            }

            RestoMasRafliTheme(darkTheme = isDarkMode, dynamicColor = false) {
                val navController = rememberNavController()
                
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(
                            onNavigateToMenu = { navController.navigate("menu") },
                            onNavigateToProfile = { navController.navigate("profile") },
                            isDarkMode = isDarkMode,
                            onToggleDarkMode = { darkMode -> 
                                isDarkMode = darkMode
                                sharedPreferences.edit().putBoolean("is_dark_mode", darkMode).apply()
                            }
                        )
                    }
                    composable("menu") {
                        MenuScreen(
                            onBack = { navController.popBackStack() },
                            onNavigateToDetail = { id -> navController.navigate("detail/$id") }
                        )
                    }
                    composable(
                        route = "detail/{menuId}",
                        arguments = listOf(navArgument("menuId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val menuId = backStackEntry.arguments?.getString("menuId") ?: ""
                        DetailMenuScreen(
                            menuItemId = menuId,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("profile") {
                        ProfileScreen(
                            onBack = { navController.popBackStack() },
                            onNavigateToEditProfile = { navController.navigate("edit-profile") }
                        )
                    }
                    composable("edit-profile") {
                        EditProfileScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
