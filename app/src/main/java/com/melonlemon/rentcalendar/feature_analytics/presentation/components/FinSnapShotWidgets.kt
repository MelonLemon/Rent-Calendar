package com.melonlemon.rentcalendar.feature_analytics.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.presentation.components.NameCommentFields
import com.melonlemon.rentcalendar.ui.theme.RentCalendarTheme

@Composable
fun FinSnapShotContainer(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable() () -> Unit
) {
    OutlinedCard(
        modifier = modifier,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            content()
        }
    }
}

@Composable
fun PaybackVariant(
    modifier:Modifier = Modifier,
    result: String = "",
    description: String,
    firstValue: Int,
    onFirstVChange: (String) -> Unit,
    secondValue: Int,
    onSecondVChange: (String) -> Unit,
    nameFirstV: String,
    nameSecondV: String,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text="~$result",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Icon(
                modifier = Modifier.size(56.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.payback),
                contentDescription = "Payback",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text=description,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextFieldWithTitle(
                modifier = modifier.weight(1f),
                value = firstValue,
                onValueChanged = onFirstVChange,
                title = nameFirstV
            )
            Icon(
                modifier = modifier.padding(top = 20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.divide_sign),
                contentDescription = "Divide",
                tint = MaterialTheme.colorScheme.primary)
            TextFieldWithTitle(
                modifier = modifier.weight(1f),
                value = secondValue,
                onValueChanged = onSecondVChange,
                title = nameSecondV
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithTitle(
    modifier: Modifier = Modifier,
    value: Int,
    onValueChanged: (String) -> Unit,
    title: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = if (value == 0) "" else "$value",
            onValueChange = onValueChanged,
            placeholder = { Text(text= stringResource(R.string.number_placeholder)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            textStyle = MaterialTheme.typography.headlineSmall,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                textColor = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title.uppercase(),
            style =  MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CompareVariant(
    modifier:Modifier = Modifier,
    firstResult: Int,
    firstResTitle: String,
    secondResult: Int,
    secondResTitle: String,
    firstValue: Int,
    onFirstVChange: (String) -> Unit,
    secondValue: Int,
    nameFirstV: String
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            TextWithTitle(
                modifier = modifier.weight(1f),
                value = firstResult,
                title = firstResTitle
            )
            TextWithTitle(
                modifier = modifier.weight(1f),
                value = secondResult,
                title = secondResTitle
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextFieldWithTitle(
                modifier = modifier.weight(1f),
                value = firstValue,
                onValueChanged = onFirstVChange,
                title = nameFirstV
            )
            Icon(
                modifier = modifier.padding(top = 20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.multiply_sign),
                contentDescription = "Multiply",
                tint = MaterialTheme.colorScheme.primary)
            Text(
                modifier = modifier.weight(1f).padding(top = 4.dp),
                text = "$secondValue%",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun TextWithTitle(
    modifier: Modifier = Modifier,
    value: Int,
    title: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text="~$value",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            style =  MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FinSnapShotPaybackPreview() {
    RentCalendarTheme {
        FinSnapShotContainer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            title = "Gross Rent Multiplier",
        ){
            PaybackVariant(
                result = "12 years",
                description = "The higher the GRM, the less attractive " +
                        "an investment property becomes.",
                firstValue = 140000,
                onFirstVChange = { },
                secondValue = 12000,
                onSecondVChange = { },
                nameFirstV = "TOTAL PURCHASE PRICE",
                nameSecondV = "YEARLY GROSS RENT"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinSnapShotComparePreview() {
    RentCalendarTheme {
        FinSnapShotContainer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            title = "The 1% Rule",
        ){
            CompareVariant(
                firstResult=1200,
                firstResTitle="Good Monthly Gross Rent",
                secondResult=1200,
                secondResTitle="Your Monthly Gross Rent",
                firstValue = 140000,
                onFirstVChange = { },
                secondValue = 1,
                nameFirstV = "TOTAL PURCHASE PRICE",
            )
        }
    }
}