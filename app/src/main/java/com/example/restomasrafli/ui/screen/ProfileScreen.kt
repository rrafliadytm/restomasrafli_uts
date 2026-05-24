package com.example.restomasrafli.ui.screen

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restomasrafli.ui.theme.RestoMasRafliTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBack: () -> Unit, onNavigateToEditProfile: () -> Unit) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("resto_prefs", Context.MODE_PRIVATE)

    val name = sharedPreferences.getString("name", "Resto Mas Rafli") ?: "Resto Mas Rafli"
    val address = sharedPreferences.getString("address", "Jl. Masakan Lezat No. 123, Jakarta") ?: "Jl. Masakan Lezat No. 123, Jakarta"
    val description = sharedPreferences.getString("description", "Menyajikan hidangan terbaik dengan cinta dan bahan berkualitas.") ?: "Menyajikan hidangan terbaik dengan cinta dan bahan berkualitas."
    val hours = sharedPreferences.getString("hours", "10:00 - 22:00") ?: "10:00 - 22:00"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil Restoran") },
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
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileItem(label = "Nama Restoran", value = name)
            ProfileItem(label = "Alamat", value = address)
            ProfileItem(label = "Jam Buka", value = hours)
            ProfileItem(label = "Deskripsi Singkat", value = description)
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = onNavigateToEditProfile,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Profil")
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    RestoMasRafliTheme {
        ProfileScreen(onBack = {}, onNavigateToEditProfile = {})
    }
}
