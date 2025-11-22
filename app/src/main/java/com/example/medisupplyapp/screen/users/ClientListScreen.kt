package com.example.medisupplyapp.screen.users

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medisupplyapp.R
import com.example.medisupplyapp.components.SearchInput
import com.example.medisupplyapp.components.SimpleTopBar
import com.example.medisupplyapp.data.model.Client
import com.example.medisupplyapp.screen.LoadingScreen
import com.example.medisupplyapp.screen.orders.ErrorOrdersScreen
import com.tuempresa.medisupply.ui.components.FooterNavigation
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController

@Composable
fun ClientListScreen(
    viewModel: ClientViewmodel = viewModel(),
    onNavigate: (String) -> Unit,
    selectedRoute: String,
    onBack: () -> Unit,
    navController: NavController
) {
    val uiState = viewModel.clientsState
    var searchQuery by remember { mutableStateOf("") }

    MediSupplyTheme {
        Scaffold(
            topBar = {
                SimpleTopBar(
                    title = stringResource(R.string.users),
                    onBack = onBack
                )
            },
            bottomBar = { FooterNavigation(selectedRoute, onNavigate) },
            containerColor = MaterialTheme.colorScheme.surface
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.client_list),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                SearchInput(
                    modifier = Modifier.padding(bottom = 16.dp),
                    onSearch = { query -> searchQuery = query },
                    placeholderText = stringResource(R.string.search_placeholder_product)
                )

                when (uiState) {
                    is UsersUiState.Loading -> {
                        LoadingScreen()
                    }

                    is UsersUiState.Success -> {
                        val filteredClients = uiState.users.filter {
                            it.name.contains(searchQuery, ignoreCase = true)
                        }

                        if (filteredClients.isEmpty()) {
                            Text(text = stringResource(R.string.users))
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(filteredClients) { client ->
                                    ClientItem(
                                        client = client,
                                        onClick = { selectedClient ->
                                            navController.currentBackStackEntry?.savedStateHandle?.set("client", selectedClient)
                                            navController.navigate("clientDetail")
                                        }
                                    )
                                }
                            }
                        }
                    }

                    is UsersUiState.Empty -> {
                        ErrorUsersScreen(
                            paddingValues = PaddingValues(0.dp),
                            stringResource(R.string.no_user_history)
                        )
                    }

                    is UsersUiState.Error -> {
                        ErrorUsersScreen(
                            paddingValues = PaddingValues(0.dp),
                            stringResource(R.string.error_user_history)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ClientItem(client: Client, onClick: (Client) -> Unit) {
    MediSupplyTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color(0xFFD9D9D9),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Button(
                onClick = { onClick(client) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 28.dp,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = client.name,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.users)
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorUsersScreen(
    paddingValues: PaddingValues,
    message: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(paddingValues)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(id = R.drawable.ic_users),
            contentDescription = stringResource(R.string.users),
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text =  message,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}
