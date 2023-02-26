package com.melonlemon.rentcalendar.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameInputPlus(
    modifier: Modifier = Modifier,
    name: String = "",
    onNameChanged: (String) -> Unit,
    onAddButtonClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ){
        OutlinedTextField(
            value = name,
            onValueChange = onNameChanged,
            placeholder = { Text(text= stringResource(R.string.name)) },
            modifier = Modifier.weight(1f)
        )
        BasicCardIcon(
            icon = Icons.Filled.Add,
            modifier = Modifier
                .size(56.dp)
                .clickable {
                    onAddButtonClicked()
                }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameValueInputPlus(
    modifier: Modifier = Modifier,
    name: String = "",
    onNameChanged: (String) -> Unit,
    number: String = "",
    onNumberChanged: (String) -> Unit,
    onAddButtonClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ){
        OutlinedTextField(
            value = name,
            onValueChange = onNameChanged,
            placeholder = { Text(text= stringResource(R.string.name)) },
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = number,
            onValueChange = onNumberChanged,
            placeholder = { Text(text= stringResource(R.string.number_placeholder)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
        BasicCardIcon(
            icon = Icons.Filled.Add,
            modifier = Modifier
                .size(56.dp)
                .clickable {
                    onAddButtonClicked()
                }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputContainer(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String,
    content: @Composable() () -> Unit
) {
    var selected by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ElevatedFilterChip(
            onClick = { selected = !selected },
            selected = selected,
            label = { Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )},
            leadingIcon = {
                if(icon!=null){
                    Icon(
                        icon,
                        contentDescription = "Localized description",
                        Modifier.size(AssistChipDefaults.IconSize)
                    )
                }

            },
            colors  = FilterChipDefaults.elevatedFilterChipColors(
                containerColor = MaterialTheme.colorScheme.background,
                labelColor = MaterialTheme.colorScheme.outline,
                iconColor = MaterialTheme.colorScheme.outline,
                selectedContainerColor = MaterialTheme.colorScheme.surface,
                selectedLabelColor = MaterialTheme.colorScheme.primary,
                selectedLeadingIconColor = MaterialTheme.colorScheme.primary,
            ),
            elevation = FilterChipDefaults.filterChipElevation(
                defaultElevation = 0.dp
            )
        )
        if(selected){
            content()
        }

    }
}
