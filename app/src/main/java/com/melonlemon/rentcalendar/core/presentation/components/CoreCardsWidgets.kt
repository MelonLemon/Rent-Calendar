package com.melonlemon.rentcalendar.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.ui.theme.RentCalendarTheme

@Composable
fun BasicCardIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Filled.MoreVert,
    isSelected: Boolean = false
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.secondaryContainer
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            tint = if(isSelected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSecondaryContainer,
            contentDescription = "Chosen Icon")
    }
}

@Composable
fun InfoCardInput(
    modifier: Modifier = Modifier,
    textFirstR: String = "",
    textSecondR: @Composable() () -> Unit,
    inputNumber: String = "",
    onNumberChanged: (String) -> Unit,
    onAddButtonClicked: () -> Unit,
    content: @Composable() () -> Unit
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ){
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = textFirstR,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                textSecondR()

            }
            Spacer(modifier = Modifier.width(16.dp))
            NameInputPlus(
                modifier = Modifier.weight(1f),
                name = inputNumber,
                onNameChanged = onNumberChanged,
                onAddButtonClicked = onAddButtonClicked
            )
        }

        content()
    }
}

@Composable
fun ArchiveCard(
    textFirstR: String = "",
    textSecondR: String = "",
    onDoubleTap: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { onDoubleTap() },
                )
            },
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = textFirstR,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = textSecondR,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
fun RentCard() {
    
}

@Preview(showBackground = true)
@Composable
fun BasicCardIconPreview() {
    RentCalendarTheme {
        BasicCardIcon(
            modifier = Modifier.size(56.dp),
            icon = Icons.Filled.Add,
            isSelected = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InfoCardInputPreview() {
    RentCalendarTheme {
        InfoCardInput(
            textFirstR = "Safety Net - 1m Expenses ddddddddddd",
            textSecondR = {
                Text(
                    text = "$1500",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                ) },
            inputNumber = "300",
            onNumberChanged =  { },
            onAddButtonClicked = { },
            content = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ArchiveCardPreview() {
    RentCalendarTheme {
        ArchiveCard(
            textFirstR = "Safety Net - 1m Expenses ddddddddddd",
            textSecondR = "dsfsdfds",
            onDoubleTap = { }
        )
    }
}