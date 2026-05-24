package com.example.restomasrafli.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restomasrafli.R
import com.example.restomasrafli.ui.theme.RestoMasRafliTheme

enum class MenuCategory {
    MAKANAN, MINUMAN
}

data class MenuItem(
    val id: String,
    val name: String,
    val price: String,
    val imageRes: Int,
    val category: MenuCategory
)

val menuList = listOf(
    MenuItem("1", "Nasi Goreng Spesial", "Rp 25.000", R.drawable.nasgor, MenuCategory.MAKANAN),
    MenuItem("2", "Mie Ayam Bakso", "Rp 18.000", R.drawable.mieayambakso, MenuCategory.MAKANAN),
    MenuItem("3", "Ayam Bakar Madu", "Rp 30.000", R.drawable.ayambakarmadu, MenuCategory.MAKANAN),
    MenuItem("4", "Es Teh Manis", "Rp 5.000", R.drawable.esteh, MenuCategory.MINUMAN),
    MenuItem("5", "Jus Jeruk Segar", "Rp 12.000", R.drawable.jusjeruk, MenuCategory.MINUMAN),
    MenuItem("6", "Kopi Susu", "Rp 15.000", R.drawable.kopsus, MenuCategory.MINUMAN),
    MenuItem("7", "Sate Ayam", "Rp 22.000", R.drawable.sate, MenuCategory.MAKANAN),
    MenuItem("8", "Soto Ayam", "Rp 20.000", R.drawable.soto, MenuCategory.MAKANAN),
    MenuItem("9", "Gado-Gado", "Rp 18.000", R.drawable.gado, MenuCategory.MAKANAN),
    MenuItem("10", "Nasi Padang", "Rp 28.000", R.drawable.naspad, MenuCategory.MAKANAN),
    MenuItem("11", "Rendang Daging", "Rp 35.000", R.drawable.rendang, MenuCategory.MAKANAN),
    MenuItem("12", "Gurame Bakar", "Rp 40.000", R.drawable.gurame, MenuCategory.MAKANAN),
    MenuItem("13", "Jus Alpukat", "Rp 15.000", R.drawable.alpukat, MenuCategory.MINUMAN),
    MenuItem("14", "Es Campur", "Rp 12.000", R.drawable.escampur, MenuCategory.MINUMAN),

)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(onBack: () -> Unit, onNavigateToDetail: (String) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    
    val filteredMenu = menuList.filter { it.name.contains(searchQuery, ignoreCase = true) }
    
    val makanan = filteredMenu.filter { it.category == MenuCategory.MAKANAN }
    val minuman = filteredMenu.filter { it.category == MenuCategory.MINUMAN }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Menu Restoran", 
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleLarge
                    ) 
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Cari menu favoritmu...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            // Tab Kategori untuk pengelompokan yang lebih jelas
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = {},
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Makanan", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Minuman", fontWeight = FontWeight.Bold) }
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val currentList = if (selectedTabIndex == 0) makanan else minuman
                
                items(currentList) { item ->
                    MenuListItem(
                        item = item,
                        onClick = { onNavigateToDetail(item.id) }
                    )
                }
                
                if (currentList.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                if (searchQuery.isEmpty()) "Belum ada menu di kategori ini" else "Menu tidak ditemukan",
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(16.dp)),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
            ) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.price,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            
            Surface(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    RestoMasRafliTheme {
        MenuScreen(onBack = {}, onNavigateToDetail = {})
    }
}
