package com.melonlemon.rentcalendar.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.R
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
fun InputInfoCard(
    modifier: Modifier = Modifier,
    textFirstR: String = "",
    inputNumber: String = "",
    onNumberChanged: (String) -> Unit,
    onAddButtonClicked: () -> Unit,
    content: @Composable () -> Unit = { }
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
            Text(
                modifier = Modifier.weight(1f),
                text = textFirstR,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3
            )
            Spacer(modifier = Modifier.width(2.dp))
            NameInputPlus(
                modifier = Modifier.widthIn(min = 100.dp, max = 150.dp),
                name = inputNumber,
                onNameChanged = onNumberChanged,
                onAddButtonClicked = onAddButtonClicked,
                placeholder = stringResource(R.string.number_placeholder)
            )
        }

        content()
    }
}

@Composable
fun DisplayInfoCard(
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



@Preview(showBackground = true)
@Composable
fun InputInfoCardPreview() {
    RentCalendarTheme {
        InputInfoCard(
            textFirstR = "Housing and communal services",
            onNumberChanged = { },
            inputNumber = "",
            onAddButtonClicked = { }
        )
    }
}
