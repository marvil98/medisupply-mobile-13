package com.example.medisupplyapp.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import com.example.medisupplyapp.R
import kotlinx.coroutines.delay

@Composable
fun CustomDropdown(
    label: String,
    options: List<String>,
    selected: String?,
    onSelect: (String) -> Unit,
    hasError: Boolean? = false,
    placeholder: String = stringResource(R.string.placeholder_default),
    errorMessage: String = stringResource(R.string.required_field),
    onDismissWithoutSelection: (() -> Unit)? = null,
    enableSearch: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    var selector by remember { mutableStateOf(false) }
    var selectWidthPx by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf(selected ?: "") }
    var textFieldWidth by remember { mutableStateOf(0) }
    var textFieldHeight by remember { mutableStateOf(0) }
    val focusRequester = remember { FocusRequester() }
    var suppressFocus by remember { mutableStateOf(false) }
    var isManualClose by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val density = LocalDensity.current

    val filteredOptions = remember(searchText, options) {
        if (searchText.isBlank()) options
        else options.filter { it.contains(searchText, ignoreCase = true) }
    }

    LaunchedEffect(suppressFocus) {
        if (suppressFocus) {
            delay(150)
            suppressFocus = false
        }
    }

    LaunchedEffect(isManualClose) {
        if (isManualClose) {
            delay(100)
            isManualClose = false
        }
    }

    LaunchedEffect(selector) {
        if (selector) {
            delay(100)
            selector = false
        }
    }

    if (!enableSearch) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                modifier = Modifier.padding(bottom = 8.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                .background(MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(8.dp))
                .clickable { expanded = !expanded }
                .onGloballyPositioned { coordinates ->
                    selectWidthPx = coordinates.size.width
                },
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                    .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selected.isNullOrBlank()) placeholder else selected,
                        color = if (selected.isNullOrBlank()) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily(Font(R.font.league_spartan_regular)),
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_down),
                        contentDescription = null,
                        modifier = Modifier
                        .size(16.dp)
                            .rotate(if (expanded) 180f else 0f),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
            modifier = Modifier
                .width(with(LocalDensity.current) { selectWidthPx.toDp() })
                .background(Color.White)
                .fillMaxWidth()
                .heightIn(max = 200.dp),
                onDismissRequest = {
                    expanded = false
                    if (selected.isNullOrBlank()) onDismissWithoutSelection?.invoke()
                },
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            fontWeight = if (option == selected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                        onClick = {
                            onSelect(option)
                            expanded = false
                        }
                    )
                }
            }

            if (hasError == true) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                modifier = Modifier.padding(bottom = 8.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            DisposableEffect(Unit) {
                onDispose {
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        textFieldWidth = it.size.width
                        textFieldHeight = it.size.height
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            expanded = true
                        },
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.league_spartan_regular))
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester)
                            .onFocusChanged { state ->
                                if (state.isFocused && !suppressFocus) {
                                    expanded = true
                                }
                            },
                        decorationBox = { innerTextField ->
                            if (searchText.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    fontFamily = FontFamily(Font(R.font.league_spartan_regular))
                                )
                            }
                            innerTextField()
                        }
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_down),
                        contentDescription = stringResource(id = R.string.open_close),
                        modifier = Modifier
                            .size(20.dp)
                            .rotate(if (expanded) 180f else 0f)
                            .clickable {
                                if (expanded || selector) {
                                    isManualClose = true
                                    expanded = false
                                    suppressFocus = true
                                    focusManager.clearFocus(force = true)
                                } else {
                                    expanded = true
                                    selector = false
                                    focusRequester.requestFocus()
                                }
                            },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                if (expanded) {
                    Popup(
                        alignment = Alignment.TopStart,
                        offset = IntOffset(0, textFieldHeight + with(density) { 4.dp.roundToPx() }),
                        onDismissRequest = {
                            if (!isManualClose) {
                                expanded = false
                                focusManager.clearFocus(force = true)
                                selector = true
                                if (selected.isNullOrBlank()) {
                                    onDismissWithoutSelection?.invoke()
                                }
                            }
                        },
                        properties = PopupProperties(focusable = false)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(with(density) { textFieldWidth.toDp() })
                                .background(Color.White,
                                    RoundedCornerShape(
                                        topStart = 0.dp,
                                        topEnd = 0.dp,
                                        bottomStart = 8.dp,
                                        bottomEnd = 8.dp
                                    ))
                                .heightIn(max = 200.dp)
                                .verticalScroll(rememberScrollState())
                                .padding(vertical = 4.dp)
                        ) {
                            if (filteredOptions.isEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.no_results),
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .align(Alignment.CenterStart),
                                    fontFamily = FontFamily(Font(R.font.league_spartan_semibold)),
                                    fontSize = 14.sp
                                )
                            } else {
                                Column {
                                    filteredOptions.forEach { option ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = option,
                                                    fontWeight = if (option == selected) FontWeight.Bold else FontWeight.Normal,
                                                    fontFamily = FontFamily(Font(R.font.league_spartan_regular))
                                                )
                                            },
                                            onClick = {
                                                onSelect(option)
                                                searchText = option
                                                expanded = false
                                                focusManager.clearFocus(force = true)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (hasError == true) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}