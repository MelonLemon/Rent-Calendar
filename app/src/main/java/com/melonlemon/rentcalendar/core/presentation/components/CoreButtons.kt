package com.melonlemon.rentcalendar.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SectionButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onBtnClick: (Boolean) -> Unit
) {
    Button(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = if(isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp),
            contentColor = if(isSelected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(width = 1.dp, color = if(isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.outlineVariant),
        onClick = { onBtnClick(!isSelected) }
    ) {
        Text(
            text=text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun FilterButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onBtnClick: (Boolean) -> Unit
) {
    Button(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        colors = ButtonDefaults.buttonColors(
            containerColor = if(isSelected) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.background,
            contentColor = if(isSelected) MaterialTheme.colorScheme.onSecondaryContainer
            else MaterialTheme.colorScheme.primary
        ),
        onClick = { onBtnClick(!isSelected) }
    ) {
        Text(
            text=text,
            style = MaterialTheme.typography.labelLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )

    }
}

@Composable
fun SFilterButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onBtnClick: (Boolean) -> Unit = { }
) {
    Button(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = if(isSelected) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp),
            contentColor = if(isSelected) MaterialTheme.colorScheme.onSecondaryContainer
            else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        border = BorderStroke(width = 1.dp, color = if(isSelected) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.outlineVariant),
        onClick = { onBtnClick(!isSelected) }
    ) {
        Text(
            text=text,
            style = MaterialTheme.typography.labelLarge,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun SegmentedTwoBtns(
    firstBtnName: String,
    secondBtnName: String,
    isFirstBtnSelected: Boolean = true,
    onBtnClick: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Button(
            onClick = {onBtnClick(true) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if(isFirstBtnSelected)
                    MaterialTheme.colorScheme.secondaryContainer else
                    MaterialTheme.colorScheme.surface,
                contentColor = if(isFirstBtnSelected)
                    MaterialTheme.colorScheme.onSecondaryContainer else
                    MaterialTheme.colorScheme.onSurface,
            ),
            shape = RoundedCornerShape(
                50,
                0,
                0,
                50
            ),
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline)
        ){
            if(isFirstBtnSelected){
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done"
                )
            }
            Text(text = firstBtnName)
        }
        Button(
            onClick = {onBtnClick(false) },
            shape = RoundedCornerShape(
                0,
                50,
                50,
                0
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = if(isFirstBtnSelected)
                    Color.Transparent else
                    MaterialTheme.colorScheme.secondaryContainer,
                contentColor = if(isFirstBtnSelected)
                    MaterialTheme.colorScheme.onSurface else
                    MaterialTheme.colorScheme.onSecondaryContainer,
            ),
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline)
        ){
            if(!isFirstBtnSelected){
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done"
                )
            }

            Text(text = secondBtnName)
        }

    }
}

