package com.melonlemon.rentcalendar.feature_home.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentWidget(
    nights: Int,
    oneNightMoney: Int,
    allMoney: Int,
    onNightMChange: (String) -> Unit,
    onAllMoneyChange: (String) -> Unit,
) {
    Column {
        Text(
            text = stringResource(R.string.payment) + ":",
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.nights) + ": $nights",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = if(oneNightMoney==0) "" else "$oneNightMoney",
            onValueChange = onNightMChange,
            placeholder = { Text(text= stringResource(R.string.for_one_night)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            textStyle = MaterialTheme.typography.titleMedium,
            prefix = { Text(text="~") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                textColor = MaterialTheme.colorScheme.onSurface
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = if(allMoney==0) "" else "$allMoney",
            onValueChange = onAllMoneyChange,
            placeholder = { Text(text= stringResource(R.string.for_all_nights)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            textStyle = MaterialTheme.typography.titleMedium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                textColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

