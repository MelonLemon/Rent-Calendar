package com.melonlemon.rentcalendar.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.Greeting
import com.melonlemon.rentcalendar.ui.theme.RentCalendarTheme

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

@Composable
fun StandardIconBtn(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onBtnClick: (Boolean) -> Unit,
    imageVector: ImageVector
) {
    Button(
        modifier = modifier.defaultMinSize(
            minWidth = 56.dp,
            minHeight = 56.dp
        ),
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = if(isSelected) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.surface,
            contentColor = if(isSelected) MaterialTheme.colorScheme.onSecondaryContainer
            else MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(width = 1.dp, color = if(isSelected) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.outlineVariant),
        onClick = { onBtnClick(!isSelected) }
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null)
    }
}

@Preview(showBackground = true)
@Composable
fun SectionButtonPreview() {
    RentCalendarTheme {
        SectionButton(
            text = "Schedule",
            isSelected = true,
            onBtnClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SectionButtonFalsePreview() {
    RentCalendarTheme {
        SectionButton(
            text = "Schedule",
            isSelected = false,
            onBtnClick = { }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun FilterButtonFalsePreview() {
    RentCalendarTheme {
        FilterButton(
            text = "All",
            isSelected = false,
            onBtnClick = { }
        )
    }
}



@Preview(showBackground = true)
@Composable
fun FilterButtonPreview() {
    RentCalendarTheme {
        FilterButton(
            text = "All",
            isSelected = true,
            onBtnClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SegmentedTwoButtonsPreview() {
    RentCalendarTheme {
        SegmentedTwoBtns(
            firstBtnName = "Regular",
            secondBtnName = "Irregular",
            onBtnClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StandardIconBtnPreview() {
    RentCalendarTheme {
        StandardIconBtn(
            isSelected = true,
            onBtnClick = { },
            imageVector = Icons.Filled.Add
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StandardIconBtnFalsePreview() {
    RentCalendarTheme {
        StandardIconBtn(
            isSelected = false,
            onBtnClick = { },
            imageVector = Icons.Filled.Add
        )
    }
}