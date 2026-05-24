package com.example.restomasrafli

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.restomasrafli.ui.screen.*
import com.example.restomasrafli.ui.screen.cartItemsState // Import global state keranjang
import com.example.restomasrafli.ui.theme.RestoMasRafliTheme

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Beranda")
    object Menu : BottomNavItem("menu", Icons.Default.RestaurantMenu, "Menu")
    object Cart : BottomNavItem("cart", Icons.Default.ShoppingCart, "Pesanan")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profil")
}

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
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                // Menentukan kapan BottomBar harus muncul
                val showBottomBar = currentDestination?.route in listOf("home", "menu", "cart", "profile")

                Scaffold(
                    bottomBar = {
                        AnimatedVisibility(
                            visible = showBottomBar,
                            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                        ) {
                            NavigationBar {
                                val items = listOf(
                                    BottomNavItem.Home,
                                    BottomNavItem.Menu,
                                    BottomNavItem.Cart,
                                    BottomNavItem.Profile
                                )
                                items.forEach { item ->
                                    NavigationBarItem(
                                        icon = { 
                                            BadgedBox(
                                                badge = {
                                                    if (item == BottomNavItem.Cart && cartItemsState.isNotEmpty()) {
                                                        Badge {
                                                            val totalItems = cartItemsState.sumOf { it.quantity }
                                                            Text(totalItems.toString())
                                                        }
                                                    }
                                                }
                                            ) {
                                                Icon(item.icon, contentDescription = item.label)
                                            }
                                        },
                                        label = { Text(item.label) },
                                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                                        onClick = {
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "splash",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("splash") {
                            SplashScreen(
                                isDarkMode = isDarkMode,
                                onNavigateToHome = {
                                    navController.navigate("home") {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                }
                            )
                        }
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
                        composable("cart") {
                            CartScreen(
                                onBack = { navController.popBackStack() },
                                onCheckout = {
                                    // Logic for checkout can be added here
                                    // For now, just clear cart and maybe show success?
                                }
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
                                onNavigateToEditProfile = { navController.navigate("edit-profile") },
                                isDarkMode = isDarkMode,
                                onToggleDarkMode = { darkMode ->
                                    isDarkMode = darkMode
                                    sharedPreferences.edit().putBoolean("is_dark_mode", darkMode).apply()
                                }
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
}
