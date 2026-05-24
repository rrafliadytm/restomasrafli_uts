package com.example.restomasrafli.ui.screen

import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.restomasrafli.ui.theme.RestoMasRafliTheme

@Composable
fun HomeScreen(
    onNavigateToMenu: () -> Unit,
    onNavigateToProfile: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("resto_prefs", Context.MODE_PRIVATE)
    }
    
    LaunchedEffect(Unit) {
        if (!sharedPreferences.contains("name")) {
            sharedPreferences.edit().putString("name", "Resto Mas Rafli").apply()
        }
    }

    val restoName = remember {
        mutableStateOf(sharedPreferences.getString("name", "Resto Mas Rafli") ?: "Resto Mas Rafli")
    }
    val logoUri = remember {
        mutableStateOf(sharedPreferences.getString("logo_uri", "") ?: "")
    }

    // Refresh data whenever the screen is re-composed or when returning from other screens
    LaunchedEffect(key1 = Unit) {
        restoName.value = sharedPreferences.getString("name", "Resto Mas Rafli") ?: "Resto Mas Rafli"
        logoUri.value = sharedPreferences.getString("logo_uri", "") ?: ""
    }

    val backgroundColor1 by animateColorAsState(
        if (isDarkMode) MaterialTheme.colorScheme.surface
        else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        animationSpec = tween(500),
        label = "bg1"
    )
    val backgroundColor2 by animateColorAsState(
        if (isDarkMode) MaterialTheme.colorScheme.surface 
        else MaterialTheme.colorScheme.surface,
        animationSpec = tween(500),
        label = "bg2"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (isDarkMode) Modifier.background(backgroundColor1)
                else Modifier.background(
                    brush = Brush.verticalGradient(
                        colors = listOf(backgroundColor1, backgroundColor2)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Logo Section
            Surface(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape),
                color = if (isDarkMode) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primary,
                tonalElevation = 8.dp,
                shadowElevation = 12.dp
            ) {
                Crossfade(targetState = isDarkMode, animationSpec = tween(500), label = "logo") { dark ->
                    if (logoUri.value.isNotEmpty()) {
                        AsyncImage(
                            model = logoUri.value,
                            contentDescription = "Logo Restoran",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Icon(
                                imageVector = if (dark) Icons.Default.Fastfood else Icons.Default.Storefront,
                                contentDescription = "Logo Restoran",
                                modifier = Modifier.size(100.dp),
                                tint = if (dark) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Welcome Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Selamat Datang di",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = restoName.value,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Rasakan sensasi kuliner terbaik dengan cita rasa autentik dan pelayanan istimewa.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Action Buttons
            Button(
                onClick = onNavigateToMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Icon(Icons.Default.RestaurantMenu, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Eksplorasi Menu", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            FilledTonalButton(
                onClick = onNavigateToProfile,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Tentang Kami", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }

        // Theme Toggle Button moved to the end of Box to be on top of Column
        IconButton(
            onClick = { onToggleDarkMode(!isDarkMode) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp, end = 16.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
        ) {
            Icon(
                imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                contentDescription = "Toggle Dark Mode",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    RestoMasRafliTheme {
        HomeScreen(
            onNavigateToMenu = {},
            onNavigateToProfile = {},
            isDarkMode = false,
            onToggleDarkMode = {}
        )
    }
}
