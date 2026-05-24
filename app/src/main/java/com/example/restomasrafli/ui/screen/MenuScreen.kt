package com.example.restomasrafli.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restomasrafli.ui.theme.RestoMasRafliTheme

data class MenuItem(
    val id: String,
    val name: String,
    val price: String,
    val icon: ImageVector
)

val menuList = listOf(
    MenuItem("1", "Nasi Goreng Spesial", "Rp 25.000", Icons.Default.Fastfood),
    MenuItem("2", "Mie Ayam Bakso", "Rp 18.000", Icons.Default.Fastfood),
    MenuItem("3", "Ayam Bakar Madu", "Rp 30.000", Icons.Default.Fastfood),
    MenuItem("4", "Es Teh Manis", "Rp 5.000", Icons.Default.LocalDrink),
    MenuItem("5", "Jus Jeruk Segar", "Rp 12.000", Icons.Default.LocalDrink),
    MenuItem("6", "Kopi Susu", "Rp 15.000", Icons.Default.LocalDrink)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(onBack: () -> Unit, onNavigateToDetail: (String) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menu Restoran") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(menuList) { item ->
                MenuListItem(
                    item = item,
                    onClick = { onNavigateToDetail(item.id) }
                )
            }
        }
    }
}

@Composable
fun MenuListItem(item: MenuItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        ListItem(
            headlineContent = { Text(item.name) },
            supportingContent = { Text(item.price) },
            leadingContent = {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    RestoMasRafliTheme {
        MenuScreen(onBack = {}, onNavigateToDetail = {})
    }
}
