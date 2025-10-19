package com.tuempresa.medisupply.ui.components

import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import com.example.medisupplyapp.R

@Composable
fun FooterNavigation(selectedRoute: String, onNavigate: (String) -> Unit) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = stringResource(R.string.home)
                )
            },
            label = { Text(stringResource(R.string.home)) },
            selected = selectedRoute == "home",
            onClick = { onNavigate("home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White,
                indicatorColor = MaterialTheme.colorScheme.secondary
            )
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location_on),
                    contentDescription = stringResource(R.string.routes)
                )
            },
            label = { Text(stringResource(R.string.routes)) },
            selected = selectedRoute == "routes",
            onClick = { onNavigate("routes") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White,
                indicatorColor = MaterialTheme.colorScheme.secondary
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = stringResource(R.string.visits)) },
            label = { Text(stringResource(R.string.visits)) },
            selected = selectedRoute == "visits",
            onClick = { onNavigate("visits") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White,
                indicatorColor = MaterialTheme.colorScheme.secondary
            )
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_box),
                    contentDescription = stringResource(R.string.orders)
                )
            },
            label = { Text(stringResource(R.string.orders)) },
            selected = selectedRoute == "orders",
            onClick = { onNavigate("orders") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White,
                indicatorColor = MaterialTheme.colorScheme.secondary
            )
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_users),
                    contentDescription = stringResource(R.string.users)
                )
            },
            label = { Text(stringResource(R.string.users)) },
            selected = selectedRoute == "users",
            onClick = { onNavigate("users") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White,
                indicatorColor = MaterialTheme.colorScheme.secondary
            )
        )
    }
}
