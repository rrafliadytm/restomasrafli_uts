package com.example.restomasrafli.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.restomasrafli.R
import com.example.restomasrafli.ui.theme.RestoMasRafliTheme

data class CartItem(
    val menuItem: MenuItem,
    var quantity: Int
)

// Simple global cart state for demonstration purposes in this project
val cartItemsState = mutableStateListOf<CartItem>()

@Composable
fun CartScreen(onBack: () -> Unit, onCheckout: () -> Unit) {
    CartContent(
        cartItems = cartItemsState,
        onBack = onBack,
        onCheckout = {
            cartItemsState.clear()
            onCheckout()
        },
        onIncrease = { item ->
            val index = cartItemsState.indexOf(item)
            if (index != -1) cartItemsState[index] = item.copy(quantity = item.quantity + 1)
        },
        onDecrease = { item ->
            val index = cartItemsState.indexOf(item)
            if (index != -1) {
                if (item.quantity > 1) {
                    cartItemsState[index] = item.copy(quantity = item.quantity - 1)
                } else {
                    cartItemsState.removeAt(index)
                }
            }
        },
        onRemove = { item -> cartItemsState.remove(item) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartContent(
    cartItems: List<CartItem>,
    onBack: () -> Unit,
    onCheckout: () -> Unit,
    onIncrease: (CartItem) -> Unit,
    onDecrease: (CartItem) -> Unit,
    onRemove: (CartItem) -> Unit
) {
    var isOrdered by remember { mutableStateOf(false) }
    val totalPrice = cartItems.sumOf { 
        val priceInt = it.menuItem.price.replace("Rp ", "").replace(".", "").toInt()
        priceInt * it.quantity
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        if (isOrdered) "Status Pesanan" else "Keranjang Pesanan", 
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleLarge
                    ) 
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (isOrdered) isOrdered = false else onBack()
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty() && !isOrdered) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = 8.dp,
                    shadowElevation = 16.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Total Pembayaran",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                            Text(
                                "Rp ${formatPrice(totalPrice)}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Button(
                            onClick = {
                                isOrdered = true
                                onCheckout()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Bayar Sekarang", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (isOrdered) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(120.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.DeliveryDining,
                                contentDescription = null,
                                modifier = Modifier.size(70.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "Pesanan Berhasil!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Pesananmu sedang dalam pengantaran. Mohon tunggu sebentar ya!",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Kembali ke Beranda")
                    }
                }
            } else if (cartItems.isEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = MaterialTheme.colorScheme.outlineVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Keranjangmu masih kosong",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemCard(
                            item = item,
                            onIncrease = { onIncrease(item) },
                            onDecrease = { onDecrease(item) },
                            onRemove = { onRemove(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.menuItem.imageRes),
                contentDescription = item.menuItem.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.menuItem.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.menuItem.price,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilledTonalIconButton(
                        onClick = onDecrease,
                        modifier = Modifier.size(32.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                    
                    Text(
                        text = item.quantity.toString(),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        fontWeight = FontWeight.Bold
                    )
                    
                    FilledIconButton(
                        onClick = onIncrease,
                        modifier = Modifier.size(32.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
            
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Hapus",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private fun formatPrice(price: Int): String {
    return String.format("%,d", price).replace(',', '.')
}

@Preview(showBackground = true)
@Composable
fun CartScreenEmptyPreview() {
    RestoMasRafliTheme {
        CartContent(
            cartItems = emptyList(),
            onBack = {},
            onCheckout = {},
            onIncrease = {},
            onDecrease = {},
            onRemove = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenWithItemsPreview() {
    RestoMasRafliTheme {
        CartContent(
            cartItems = listOf(
                CartItem(menuList[0], 1),
                CartItem(menuList[1], 2)
            ),
            onBack = {},
            onCheckout = {},
            onIncrease = {},
            onDecrease = {},
            onRemove = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CartItemCardPreview() {
    RestoMasRafliTheme {
        CartItemCard(
            item = CartItem(
                menuItem = menuList[0],
                quantity = 2
            ),
            onIncrease = {},
            onDecrease = {},
            onRemove = {}
        )
    }
}
