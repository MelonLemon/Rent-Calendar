package com.melonlemon.rentcalendar.feature_home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.presentation.components.FilterButton
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.ui.theme.RentCalendarTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyDialog(
    modifier: Modifier = Modifier,
    currencySign: String,
    onCancel: () -> Unit,
    onSave: (String) -> Unit
) {
    var newCurrencySign by remember{ mutableStateOf(currencySign) }
    Dialog(
        onDismissRequest = onCancel
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text= stringResource(R.string.currency_dialog_title),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            )
            OutlinedTextField(
                value = newCurrencySign,
                onValueChange = { sign ->
                    newCurrencySign =  sign
                },
                placeholder = { Text(text= stringResource(R.string.currency_sign)) },
                textStyle = MaterialTheme.typography.titleMedium,
                prefix = { Text(text="~") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    textColor = MaterialTheme.colorScheme.onSurface
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End

            ){
                FilterButton(
                    text = stringResource(R.string.cancel),
                    isSelected = false,
                    onBtnClick = {
                        onCancel()
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
                FilterButton(
                    text = stringResource(R.string.save),
                    isSelected = true,
                    onBtnClick = {
                        onSave(newCurrencySign)
                    }

                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FixAmountDialog(
    modifier: Modifier = Modifier,
    expensesCategoryInfo: ExpensesCategoryInfo,
    onCancel: () -> Unit,
    onAgree: () -> Unit
) {

    Dialog(
        onDismissRequest = onCancel
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text= stringResource(R.string.change_fix_amount) + " ${expensesCategoryInfo.amount}"
                        + stringResource(R.string.for_category) + " ${expensesCategoryInfo.header}?",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End

            ){
                FilterButton(
                    text = stringResource(R.string.cancel),
                    isSelected = false,
                    onBtnClick = {
                        onCancel()
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
                FilterButton(
                    text = stringResource(R.string.agree),
                    isSelected = true,
                    onBtnClick = {
                        onAgree()
                    }

                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyDialogPreview() {
    RentCalendarTheme {
        CurrencyDialog(
            currencySign = "",
            onCancel = { },
            onSave = { }
        )
    }
}