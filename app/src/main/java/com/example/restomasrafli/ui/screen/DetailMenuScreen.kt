package com.example.restomasrafli.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restomasrafli.ui.theme.RestoMasRafliTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMenuScreen(menuItemId: String, onBack: () -> Unit) {
    val menuItem = menuList.find { it.id == menuItemId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Menu") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (menuItem != null) {
                Icon(
                    imageVector = menuItem.icon,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = menuItem.name,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = menuItem.price,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Deskripsi lezat untuk ${menuItem.name} akan tampil di sini. Nikmati hidangan terbaik dari Resto Mas Rafli.",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text("Menu tidak ditemukan")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailMenuScreenPreview() {
    RestoMasRafliTheme {
        DetailMenuScreen(menuItemId = "1", onBack = {})
    }
}
