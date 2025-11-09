package com.example.medisupplyapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.medisupplyapp.R
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchInput(
    modifier: Modifier = Modifier,
    initialQuery: String = "",
    onSearch: (String) -> Unit,
    placeholderText: String
) {
    var searchQuery by remember { mutableStateOf(initialQuery) }

    val searchInputColors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
        unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        focusedPlaceholderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
        focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,
    )

    MediSupplyTheme {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = modifier.fillMaxWidth(),
            placeholder = { Text(placeholderText) },
            trailingIcon = {
                IconButton(onClick = { onSearch(searchQuery) }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search),
                    )
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch(searchQuery) }),
            shape = RoundedCornerShape(28.dp),
            colors = searchInputColors
        )
    }
}